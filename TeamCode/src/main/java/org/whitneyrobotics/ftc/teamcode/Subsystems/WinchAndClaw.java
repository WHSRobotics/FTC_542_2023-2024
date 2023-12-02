package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class WinchAndClaw {

    public enum Targets {
        ON_SLIDES(24),
        LIFTED(18);
        public double target;
        Targets(double target) {
            this.target = target;
        }
    }

    private Targets currentTarget = Targets.ON_SLIDES;
    private double position;
    private DcMotorEx winchMotor;
    private Servo releaseServo;
    private final double SPOOL_DIAMETER = 36/25.4; //inches;
    private final double PPR = 7;
    private final double GEAR_RATIO = 104;
    private final double TICKS_PER_INCH = PPR * GEAR_RATIO / (Math.PI * SPOOL_DIAMETER);

    private final double MAX_RPM = 6600/GEAR_RATIO;

    public static double V_MAX = 2;
    public static double DEADBAND = 0.5;

    public double getVelocity(){
        return winchMotor.getVelocity(AngleUnit.RADIANS)*SPOOL_DIAMETER/2;
    }

    public double getPosition(){
        return position+winchMotor.getCurrentPosition()/TICKS_PER_INCH;
    }

    public double RELEASE_POS = 1.0, CLOSED_POS = 0.0;

    public WinchAndClaw(HardwareMap hardwareMap){
        winchMotor = hardwareMap.get(DcMotorEx.class, "winchMotor");
        //TODO: Test motor direction
        winchMotor.setDirection(DcMotorEx.Direction.REVERSE);
        winchMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        winchMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        winchMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        releaseServo = hardwareMap.get(Servo.class, "releaseServo");
        position = currentTarget.target;
    }

    public void setTarget(Targets target){
        currentTarget = target;
    }

    public void update(){
        if(Math.abs(getPosition()-currentTarget.target)<DEADBAND){
            winchMotor.setPower(0);
        } else {
            if (currentTarget.target < getPosition()) {
                winchMotor.setPower(-V_MAX / (MAX_RPM * SPOOL_DIAMETER / 2 * Math.PI / 30));
            } else {
                winchMotor.setPower(V_MAX / (MAX_RPM * SPOOL_DIAMETER / 2 * Math.PI / 30));
            }
        }
    }

    public void close(){
        releaseServo.setPosition(CLOSED_POS);
    }

    public void release(){
        releaseServo.setPosition(RELEASE_POS);
    }
}
