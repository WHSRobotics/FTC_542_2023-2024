package org.whitneyrobotics.ftc.teamcode.Subsystems;

import static org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.MM;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.whitneyrobotics.ftc.teamcode.Libraries.Controllers.ControlConstants;
import org.whitneyrobotics.ftc.teamcode.Libraries.Controllers.PIDController;
import org.whitneyrobotics.ftc.teamcode.Libraries.Motion.MotionProfileTrapezoidal;
import org.whitneyrobotics.ftc.teamcode.Libraries.StateForge.StateForge;
import org.whitneyrobotics.ftc.teamcode.Libraries.StateForge.StateMachine;
import org.whitneyrobotics.ftc.teamcode.Libraries.Utilities.NanoStopwatch;

import java.util.function.Consumer;

@Config
public class ArmElevator {
    private DcMotorEx lSlides;

    private double requestedPower;
    private boolean slowed;

    private double GEAR_RATIO = 2;
    private double coefficient = -1;

    private StateMachine<ElevatorStates> elevatorStatesStateMachine;
    public static double V_MAX = 8, A_MAX = 10; //in/s, in/s^2

    //TODO: Potentially add gain scheduling for a gravitational constant
    public static ControlConstants EXTENSION_PID= new ControlConstants(0.9,0,0.002);

    public static MotionProfileTrapezoidal motionProfile = new MotionProfileTrapezoidal(V_MAX, A_MAX);

    public static double CORRECTION_FACTOR = 0.71;
    private final double SPOOL_RADIUS = MM.toInches(38.2/2d); //in

    private PIDController controller = new PIDController(EXTENSION_PID);

    private double calibrationOffset = 0; //for resetting

    public final double TICKS_PER_REV =	384.5;

    public static double kStatic = 0.21;

    public static double kV = 1.5;

    public static double kA = 0.01;



    private NanoStopwatch stopwatch = new NanoStopwatch();

    public final double totalTicksToInches(double ticks){
        return (ticks / TICKS_PER_REV) * (2 * Math.PI * SPOOL_RADIUS) * (1/GEAR_RATIO) * CORRECTION_FACTOR;
    }

    public final double angularToLinear(double angle){
        return angle * SPOOL_RADIUS * GEAR_RATIO;
    }

    private final static double NOMINAL_VOLTAGE = 12.0;

    public static double TIMEOUT = 0.5; //seconds

    public final static double ACCEPTABLE_ERROR = 0.2; //inches

    private boolean newwTarget = false;

    public Double targetPos = null;

    @NonNull
    public Target target = Target.NONE;

    VoltageSensor voltageSensor;

    enum ElevatorStates {
        IDLE,
        RISING,
        STATE_CHANGED_INTERMITTENTLY
    }

    public enum Target {
        NONE(0),
        RETRACT(0),
        AUTO_MINI(0),

        ONE(8),
        TWO(16),
        AUTO_ENTER(5),

        THREE(21);
        Target(double pos){
            this.pos = pos;
        }
        double pos;
    }
    private double error, initialPosition;

    public static double INTAKE_CUTOFF = Target.ONE.pos;
    private boolean underIntakeCutoff;

    private Consumer<Boolean> onZoneChangeConsumer;

    public ArmElevator(HardwareMap hardwareMap){
        lSlides = hardwareMap.get(DcMotorEx.class, "linearSlides");
        if(RobotImpl.getInstance() != null) voltageSensor = RobotImpl.getInstance().voltageSensor;
        else voltageSensor = hardwareMap.getAll(VoltageSensor.class).iterator().next();
        lSlides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lSlides.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //lSlides.setDirection(DcMotorSimple.Direction.REVERSE);
        elevatorStatesStateMachine = new StateForge.StateMachineBuilder<ElevatorStates>()
                .state(ElevatorStates.IDLE)
                    .onEntry(() -> {
                        lSlides.setPower(0);
                    })
                    .periodic(() -> {
                        lSlides.setPower(requestedPower * (slowed ? 0.5 : 1));
                    })

                    .transitionLinear(() -> target != Target.NONE)
                     .transitionLinear(() -> targetPos != null)
                .onExit(stopwatch::reset)
                .fin()
                .state(ElevatorStates.RISING)
                    .onEntry(() -> {
                        newwTarget = false;
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
                        double errorPosition = motionProfile.positionAt(stopwatch.seconds())+initialPosition-getPosition();
                        controller.calculate(errorPosition);
                        lSlides.setPower(
                                kV*motionProfile.velocityAt(stopwatch.seconds())/V_MAX * NOMINAL_VOLTAGE/voltageSensor.getVoltage()+
                                        kA*motionProfile.accelerationAt(stopwatch.seconds())/A_MAX+
                                        controller.getOutput()+kStatic);
                    })
                    .transition(() -> {
                        calculateError();
                        return motionProfile.isFinished(stopwatch.seconds()) && (Math.abs(
                                motionProfile.positionAt(stopwatch.seconds())+initialPosition-getPosition() //error from designed position
                        )<=ACCEPTABLE_ERROR || stopwatch.seconds()>=TIMEOUT+motionProfile.getDuration());
                    }, ElevatorStates.IDLE)
                    .transitionWithAction(() -> newwTarget, ElevatorStates.STATE_CHANGED_INTERMITTENTLY, ()->{})
                    .transitionLinear(() -> Math.abs(requestedPower) > 0)
                    .transitionLinear(() -> (target == Target.NONE) && (targetPos == null))
                    .transitionLinear(() -> target == Target.NONE && targetPos == null)
                    .onExit(() -> {
                        target = Target.NONE;
                        targetPos = null;
                    })
                    .fin()
                    .nonLinearState(ElevatorStates.STATE_CHANGED_INTERMITTENTLY)
                        .transition(()->true,ElevatorStates.RISING)
                    .fin()
                .build();
        elevatorStatesStateMachine.start();
    }

    /**
     * Sets the global error of the slides. If the target position is not set, the error will be 0 (no PID).
     */
    public void calculateError(){
        Double t = getTargetPosition();
        error = t != null ? t-getPosition() : 0;
    }

    public void resetEncoders(){
        lSlides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lSlides.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void cancel(){
        target = Target.NONE;
        targetPos = null;
    }

    public boolean isBusy(){
        return target != Target.NONE;
    }

    public void update(){
        boolean zoneState = getPosition() <= INTAKE_CUTOFF;
        if(onZoneChangeConsumer != null && zoneState != underIntakeCutoff){
            onZoneChangeConsumer.accept(zoneState);
            underIntakeCutoff = zoneState;
        }
        elevatorStatesStateMachine.update();
    }

    public void setTargetPosition(@NonNull Target target){
        this.target = target;
        newwTarget = true;
    }

    public void setTargetPosition(double pos){
        targetPos = pos;
        newwTarget = true;
    }

    public void  inputPower(double power){
        requestedPower = power * coefficient;
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


    public void forceManualControl(){
        elevatorStatesStateMachine.transitionTo(ElevatorStates.IDLE);
    }

    /**
     * Returns the target position. A defined target position will override the target enum.
     * @return Target position. If the return value is `null`, the target is not set, and therefore automatic following will cancel.
     */
    public Double getTargetPosition(){
        return  targetPos != null ? targetPos : (target != Target.NONE ? target.pos : null);
    }

    public void onZoneChange(Consumer<Boolean> consumer){
        onZoneChangeConsumer = consumer;
    }
}