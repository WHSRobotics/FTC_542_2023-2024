package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whitneyrobotics.ftc.teamcode.Extensions.GamepadEx.GamepadEx;

// Created By Sanjay Ganapathy on 10/23/2023

public class LiftingMechanism {
    private static final double KP = 0.01; // Proportional constant for PID control
    private static final double MAX_POWER = 1.0; // Maximum power for the motors

    private boolean fullyExtended = false;
    private double targetPosition = 0.0;
    private final DcMotorEx Motor1;
    private final DcMotorEx Motor2;
    public double errorMargin = 0.5;

    public LiftingMechanism(HardwareMap hardwareMap, String motorName1, String motorName2, GamepadEx gamepad2) {
        Motor1 = hardwareMap.get(DcMotorEx.class, "LiftingMotorLeft");
        Motor2 = hardwareMap.get(DcMotorEx.class, "LiftingMotorRight");
        Motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); // Ensure the motors don't move when power is 0
        Motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void extendLift(double targetPosition) {
        fullyExtended = false;
        this.targetPosition = targetPosition;
        double currentPosition = (Motor1.getCurrentPosition() + Motor2.getCurrentPosition()) / 2.0;
        double error = targetPosition - currentPosition;
        double power = error * KP;

        if (error < errorMargin) {
            power = 0;
            fullyExtended = true;
        } else {
            if (power > MAX_POWER) {
                power = MAX_POWER;
            } else if (power < -MAX_POWER) {
                power = -MAX_POWER;
            }
        }

        Motor1.setPower(power);
        Motor2.setPower(power);
    }

    public void liftRobot(double targetPosition) {
        this.targetPosition = targetPosition;
        double currentPosition = (Motor1.getCurrentPosition() + Motor2.getCurrentPosition()) / 2.0;
        if (currentPosition <= targetPosition) {
            Motor1.setPower(0.0);
            Motor2.setPower(0.0);
            fullyExtended = true;
        } else {
            Motor1.setPower(-MAX_POWER);
            Motor2.setPower(-MAX_POWER);
        }
    }

    public boolean isFullyExtended() {
        return fullyExtended;
    }
}
