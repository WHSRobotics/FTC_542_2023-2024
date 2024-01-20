package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Config
public class HookAndWinch implements SubsystemIterative {
    private DcMotorEx winch;
    private Servo hook;
    private HookPositions targetPosition = HookPositions.DEFAULT;

    public static double GEAR_RATIO = 1.0/104;
    public static double RPM = 6600;
    public static double TICKS_PER_REV = 7 / GEAR_RATIO;

    public static double SPOOL_RADIUS = 1.27/2; //inches

    public void setServoPosition(HookPositions hookPositions) {
        this.targetPosition = hookPositions;
    }

    public void toggleServoPosition(){
        targetPosition = HookPositions.values()[(targetPosition.ordinal() + 1) % 2];
    }

    private double motorPower;
    public void setPower(double motorPower) {
        this.motorPower = motorPower;
    }

    private enum HookPositions{
        DEFAULT(0.38),
        ACTIVE(0.24);
        private double position;
         HookPositions(double pos){
            this.position = pos;
        }
    }

    public HookAndWinch(HardwareMap hardwareMap){
        winch = hardwareMap.get(DcMotorEx.class, "lift");
        hook = hardwareMap.get(Servo.class, "hook");
    }

    @Override
    public void init() {

        reset();
    }



    @Override
    public void update() {
        winch.setPower(motorPower);
        hook.setPosition(targetPosition.position);
    }

    @Override
    public void reset() {

        winch.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        winch.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        winch.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        hook.setPosition(HookPositions.DEFAULT.position);
    }

    public double getRevolutions(){
        return winch.getCurrentPosition()/TICKS_PER_REV;
    }

    public double getDistanceTravelled(){
        return getRevolutions() * 2 * Math.PI * SPOOL_RADIUS;
    }

    public double getLinearVelocity(){
        return winch.getVelocity(AngleUnit.RADIANS) * SPOOL_RADIUS;
    }
    public String getServoPosition(){
        return targetPosition.name();
    }

    public void toggleHook(){
        targetPosition = HookPositions.values()[(targetPosition.ordinal() + 1) % 2];
    }

    public boolean hookReleased(){
        return targetPosition.ordinal() == 1;
    }
}
