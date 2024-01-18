package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class HookAndWinch implements SubsystemIterative {
    private DcMotorEx winch;
    private Servo hook;
    private HookPositions targetPosition = HookPositions.DEFAULT;

    public void setServoPosition(HookPositions hookPositions) {
        this.targetPosition = hookPositions;
    }

    private double motorPower;
    public void setPower(double motorPower) {
        this.motorPower = motorPower;
    }

    private enum HookPositions{
        DEFAULT(0.5),
        ACTIVE(0.24);
        private double position;
        private HookPositions(double positon){
            this.position = position;
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

    public double getPosition(){
        return winch.getCurrentPosition();
    }
    public String getServoPosition(){
        return targetPosition.name();
    }

    public void toggleHook(){
        targetPosition = HookPositions.values()[(targetPosition.ordinal() + 1) % 2];
    }
}
