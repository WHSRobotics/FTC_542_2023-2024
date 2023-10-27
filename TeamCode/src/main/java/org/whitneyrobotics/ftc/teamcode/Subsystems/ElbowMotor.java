package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ElbowMotor extends LinearOpMode {
    private DcMotorEx motor;
    private boolean clockwise = true; // To track the direction

    public ElbowMotor(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        this.initInternal();
    }

    public void initInternal() {
        motor = hardwareMap.get(DcMotorEx.class, "elbowMotor");
    }


    private void handleButtonPress() {
        if (clockwise) {
            moveMotorClockwise(180);
        } else {
            moveMotorCounterClockwise(180);
        }
        clockwise = !clockwise; // Toggle direction
    }

    private void moveMotorClockwise(int degrees) {
        motor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        motor.setTargetPosition(motor.getCurrentPosition() + degrees);
        motor.setPower(0.5); // Adjust power as needed
        motor.setDirection(DcMotorEx.Direction.FORWARD);
        motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

        while (motor.isBusy()) {
            // Wait for the motor to reach the target position
        }

        motor.setPower(0); // Stop the motor
    }

    private void moveMotorCounterClockwise(int degrees) {
        motor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        motor.setTargetPosition(motor.getCurrentPosition() - degrees);
        motor.setPower(0.5); // Adjust power as needed
        motor.setDirection(DcMotorEx.Direction.REVERSE);
        motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

        while (motor.isBusy()) {
            // Wait for the motor to reach the target position
        }

        motor.setPower(0); // Stop the motor
    }

    @Override
    public void runOpMode() throws InterruptedException {

    }
}