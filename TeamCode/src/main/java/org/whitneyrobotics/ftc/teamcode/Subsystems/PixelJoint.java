package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.whitneyrobotics.ftc.teamcode.Libraries.Controllers.ControlConstants;
import org.whitneyrobotics.ftc.teamcode.Libraries.Controllers.PIDController;
import org.whitneyrobotics.ftc.teamcode.Libraries.Motion.MotionProfileTrapezoidal;
import org.whitneyrobotics.ftc.teamcode.Libraries.StateForge.StateForge;
import org.whitneyrobotics.ftc.teamcode.Libraries.StateForge.StateMachine;
import org.whitneyrobotics.ftc.teamcode.Libraries.Utilities.NanoStopwatch;

import java.util.function.Supplier;

@Config
public class PixelJoint {
    private DcMotorEx slidesJoint;
    private Servo wristJoint;

    private final double TICKS_PER_REV = 384.5;

    public static double V_MAX = 1, A_MAX = 1; //in/s, in/s^2

    public static ControlConstants ROTATION_PID = new ControlConstants(0.6,0,0.08);

    private NanoStopwatch stopwatch = new NanoStopwatch();

    private MotionProfileTrapezoidal motionProfile = new MotionProfileTrapezoidal(V_MAX, A_MAX);

    private PIDController controller = new PIDController(ROTATION_PID);
    private final static double NOMINAL_VOLTAGE = 12.0;

    private VoltageSensor voltageSensor;

    public double angularOffset = 0.0d; //radians;

    public final double totalTicksToRadians(double ticks){
        return (ticks / TICKS_PER_REV) * (2 * Math.PI);
    }

    public enum States {
        HOLD_POSITION,
        ROTATING
    }

    public static double kV = 0.6;
    public static double kA = 0.002;
    public static double kStatic = 0;

    public static ControlConstants HOLD_PID = new ControlConstants(0.7,0,0.02);

    public PIDController fixedController = new PIDController(HOLD_PID);

    public static double INTAKE_SERVO_POSITION = -0.6, OUTTAKE_SERVO_POSITION = -1;

    public static double ACCEPTABLE_ERROR = Math.toRadians(1); //radians
    public static double TIMEOUT = 0.5; //seconds

    public enum ArmPositions {
        INTAKE(0,() -> INTAKE_SERVO_POSITION),
        OUTTAKE(2*Math.PI/3,() -> OUTTAKE_SERVO_POSITION);
        public final double angle;
        public final Supplier<Double> servoPositionFunction;
        ArmPositions(double p, Supplier<Double> s){
            angle = p;
            servoPositionFunction = s;
        }
    }

    private double error;

    private StateMachine<States> pixelJointStateMachine;

    public ArmPositions target = null;

    private double lastTarget;
    private double initialPosition;

    private boolean targetChanged;

    private double targetServoPosition = INTAKE_SERVO_POSITION;

    public PixelJoint(HardwareMap hardwareMap){
        slidesJoint = hardwareMap.get(DcMotorEx.class, "slidesJoint");
        wristJoint = hardwareMap.get(Servo.class, "wristJoint");
        if(RobotImpl.getInstance() != null) voltageSensor = RobotImpl.getInstance().voltageSensor;
        else voltageSensor = hardwareMap.getAll(VoltageSensor.class).iterator().next();
        slidesJoint.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slidesJoint.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pixelJointStateMachine = new StateForge.StateMachineBuilder<States>()
                .state(States.HOLD_POSITION)
                    .onEntry(()->{
                        fixedController.init(0);
                    })
                    .periodic(() -> {
                        fixedController.calculate(lastTarget-getPosition());
                        slidesJoint.setPower(fixedController.getOutput());
                    })
                    .transitionLinear(() -> target != null)
                .fin()
                .state(States.ROTATING)
                .onEntry(() -> {
                    initialPosition = getPosition();
                    controller.init(0);
                    motionProfile.setMaxAccel(A_MAX);
                    motionProfile.setMaxVelocity(V_MAX);
                    motionProfile.setGoal(error);
                    calculateError();
                    stopwatch.reset();
                    targetChanged = false; //will ignore target changes from null to a target while resetting a previous transition
                    targetServoPosition = target.servoPositionFunction.get();
                })
                .periodic(() -> {
                    double errorFromExpected = motionProfile.positionAt(stopwatch.seconds())+initialPosition-getPosition();
                    controller.calculate(errorFromExpected);
                    slidesJoint.setPower(
                            kV*motionProfile.velocityAt(stopwatch.seconds())/V_MAX * NOMINAL_VOLTAGE/voltageSensor.getVoltage()+
                                    kA*motionProfile.accelerationAt(stopwatch.seconds())/A_MAX+
                                    controller.getOutput() + kStatic);
                    calculateError();
                })
                .onExit(() -> {
                    target = null;
                    slidesJoint.setPower(0);
                })
                .transitionLinear(() -> {
                    calculateError();
                    return motionProfile.isFinished(stopwatch.seconds()) && ((Math.abs(
                            motionProfile.positionAt(stopwatch.seconds())+initialPosition-getPosition() //error from designed position
                    )<=ACCEPTABLE_ERROR) || (stopwatch.seconds()>=TIMEOUT+motionProfile.getDuration()));
                })
                //.transitionLinear(() -> targetChanged) // In case of a target changes midway through a motion profile
                .transitionLinear(() -> target == null)
                .fin().build();
        pixelJointStateMachine.start();
        slidesJoint.setPower(0);
    }

    public void update(){
        pixelJointStateMachine.update();
        wristJoint.setPosition(targetServoPosition); //continuously providing power to servo
    }

    public void setTarget(ArmPositions target){
        cancel();
        this.target = target;
        targetChanged = true;
        lastTarget = target.angle;
        update();
        //weird bug forcing you to double press
    }

    public void setAngularOffset(double offset){
        angularOffset = offset;
    }

    public void resetEncoders(){
        slidesJoint.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        slidesJoint.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public double getPosition(){
        return totalTicksToRadians(slidesJoint.getCurrentPosition())+angularOffset;
    }

    public void calculateError(){
        if(target == null) return;
        error = target.angle - getPosition();
    }

    public void cancel(){
        target = null;
    }

    public boolean isBusy(){
        return target != null;
    }

    public double getVelocity(){
        return slidesJoint.getVelocity(AngleUnit.RADIANS);
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

    public String getState(){
        return pixelJointStateMachine.getMachineState().name();
    }

    public double getTargetPosition(){
        if(target == null) return getPosition();
        return target.angle;
    }

    public String getTarget(){
        return target.name();
    }

    public double getServoPosition(){
        return wristJoint.getPosition();
    }

    public double getMotionProfileDuration(){
        return motionProfile.getDuration();
    }
}
