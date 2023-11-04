package org.whitneyrobotics.ftc.teamcode.Subsystems.Odometry;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.whitneyrobotics.ftc.teamcode.Libraries.Controllers.ControlConstants;
import org.whitneyrobotics.ftc.teamcode.Libraries.Controllers.PIDController;
import org.whitneyrobotics.ftc.teamcode.Libraries.Motion.MotionProfileTrapezoidal;
import org.whitneyrobotics.ftc.teamcode.Libraries.StateForge.StateForge;
import org.whitneyrobotics.ftc.teamcode.Libraries.StateForge.StateMachine;
import org.whitneyrobotics.ftc.teamcode.Libraries.Utilities.NanoStopwatch;
import org.whitneyrobotics.ftc.teamcode.Subsystems.RobotImpl;

import java.util.function.Consumer;

@Config
public class ArmElevator {
    private DcMotorEx lSlides;

    private double requestedPower;
    private boolean slowed;

    private StateMachine<ElevatorStates> elevatorStatesStateMachine;
    public static double V_MAX = 5, A_MAX = 5; //in/s, in/s^2

    //TODO: Potentially add gain scheduling for a gravitational constant
    public static ControlConstants EXTENSION_PID= new ControlConstants(0.5,0,0.01);
    public static MotionProfileTrapezoidal motionProfile = new MotionProfileTrapezoidal(V_MAX, A_MAX);
    private final double SPOOL_RADIUS = 1.27/2; //in

    private PIDController controller = new PIDController(EXTENSION_PID);

    private double calibrationOffset = 0; //for resetting

    public final double TICKS_PER_REV =	384.5;

    public static double kV = 0.65;

    public static double kA = 0.0;

    private NanoStopwatch stopwatch = new NanoStopwatch();

    public final double totalTicksToInches(double ticks){
        return (ticks / TICKS_PER_REV) * (2 * Math.PI * SPOOL_RADIUS);
    }

    public final double angularToLinear(double angle){
        return angle * SPOOL_RADIUS;
    }

    private final static double NOMINAL_VOLTAGE = 12.0;

    public final static double TIMEOUT = 0.5; //seconds

    public final static double ACCEPTABLE_ERROR = 0.1; //inches

    VoltageSensor voltageSensor;

    enum ElevatorStates {
        IDLE,
        RISING
    }

    public enum Target {
        RETRACT(0),
        ONE(4),
        TWO(6),
        THREE(8);
        Target(double pos){
            this.pos = pos;
        }
        double pos;
    }

    private Double currentTargetPositionInches; //inches
    private double error, initialPosition;

    public static double INTAKE_CUTOFF = Target.ONE.pos;
    private boolean underIntakeCutoff;

    private Consumer<Boolean> onZoneChangeConsumer;

    public ArmElevator(HardwareMap hardwareMap){
        lSlides = hardwareMap.get(DcMotorEx.class, "slidesMotor");
        if(RobotImpl.getInstance() != null) voltageSensor = RobotImpl.getInstance().voltageSensor;
        else voltageSensor = hardwareMap.getAll(VoltageSensor.class).iterator().next();
        lSlides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lSlides.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lSlides.setDirection(DcMotorSimple.Direction.REVERSE);
        elevatorStatesStateMachine = new StateForge.StateMachineBuilder<ElevatorStates>()
                .state(ElevatorStates.IDLE)
                    .onEntry(() -> {
                        lSlides.setPower(0);
                    })
                    .periodic(() -> {
                        lSlides.setPower(requestedPower * (slowed ? 0.5 : 1));
                    })
                    .transition(() -> currentTargetPositionInches != null, ElevatorStates.RISING)
                    .fin()
                .state(ElevatorStates.RISING)
                    .onEntry(() -> {
                        calculateError();
                        initialPosition = getPosition();
                        controller.init(0);
                        motionProfile.setMaxAccel(A_MAX);
                        motionProfile.setMaxVelocity(V_MAX);
                        motionProfile.setGoal(error);
                        stopwatch.reset();
                    })
                    .periodic(() -> {
                        //Find expected position from motion profile
                        double errorFromExpected = motionProfile.positionAt(stopwatch.seconds())+initialPosition-getPosition();
                        controller.calculate(errorFromExpected);
                        lSlides.setPower(
                                kV*motionProfile.velocityAt(stopwatch.seconds())/V_MAX * NOMINAL_VOLTAGE/voltageSensor.getVoltage()+
                                        kA*motionProfile.accelerationAt(stopwatch.seconds())/A_MAX+
                                        controller.getOutput());
                    })
                    .onExit(() -> currentTargetPositionInches= null)
                    .transition(() -> {
                        calculateError();
                        return motionProfile.isFinished(stopwatch.seconds()) && (Math.abs(
                                motionProfile.positionAt(stopwatch.seconds())+initialPosition-getPosition() //error from designed position
                        )<=ACCEPTABLE_ERROR || stopwatch.seconds()>=TIMEOUT+motionProfile.getDuration());
                    }, ElevatorStates.IDLE)
                    .transitionLinear(() -> Math.abs(requestedPower)>0 || currentTargetPositionInches == null)
                    .fin()
                .build();
        elevatorStatesStateMachine.start();
    }

    public void calculateError(){
        if(currentTargetPositionInches == null) return;
        error = currentTargetPositionInches-getPosition();
    }

    public void resetEncoders(){
        lSlides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lSlides.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void breakLifting(){
        currentTargetPositionInches = null;
    }

    public boolean isBusy(){
        return currentTargetPositionInches != null;
    }

    public void update(){
        boolean zoneState = getPosition() <= INTAKE_CUTOFF;
        if(onZoneChangeConsumer != null && zoneState != underIntakeCutoff){
            onZoneChangeConsumer.accept(zoneState);
            underIntakeCutoff = zoneState;
        }
        elevatorStatesStateMachine.update();
    }

    public void setTargetPosition(Target target){
        setTargetPosition(target.pos);
    }

    public void setTargetPosition(double pos){
        currentTargetPositionInches = pos;
        update();
    }

    public void inputPower(double power){
        requestedPower = power;
    }

    public void slowModeOn(){slowed=true;} //Bind these to onPress and onRelease methods

    public void slowModeOff(){slowed=false;}

    public void setCalibrationOffset(double offset){
        calibrationOffset = offset;
    }

    public double getDesiredPosition(){
        if(!isBusy()) return getPosition();
        return motionProfile.positionAt(stopwatch.seconds())+initialPosition;
    }

    public double getDesiredVelocity(){
        if(!isBusy()) return 0;
        return motionProfile.velocityAt(stopwatch.seconds());
    }

    public double getDesiredAcceleration(){
        if(!isBusy()) return 0;
        return motionProfile.accelerationAt(stopwatch.seconds());
    }

    public double getPosition(){
        return totalTicksToInches(lSlides.getCurrentPosition())+calibrationOffset;
    }

    public double getVelocity(){
        return angularToLinear(lSlides.getVelocity(AngleUnit.RADIANS));
    }

    public String getState(){
        return elevatorStatesStateMachine.getMachineState().name();
    }

    public void cancel(){
        currentTargetPositionInches = null;
    }

    public void forceManualControl(){
        elevatorStatesStateMachine.transitionTo(ElevatorStates.IDLE);
    }

    public double getTargetPosition(){
        if(currentTargetPositionInches == null) return 0;
        return currentTargetPositionInches;
    }

    public void onZoneChange(Consumer<Boolean> consumer){
        onZoneChangeConsumer = consumer;
    }
}
