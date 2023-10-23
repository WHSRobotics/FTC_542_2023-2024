package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class LiftingMechanism {
    private static final double KP = 0.01; // Proportional constant for PID control
    private static final double MAX_POWER = 1.0; // Maximum power for the motors

    private boolean fullyExtended = false;
    private double targetPosition = 0.0;
    private final DcMotorEx Motor1;
    private final DcMotorEx Motor2;
    public double errorMargin = 0.5;

    public LiftingMechanism(HardwareMap hardwareMap, String motorName1, String motorName2) {
        Motor1 = hardwareMap.get(DcMotorEx.class, "LiftingMotorLeft");
        Motor2 = hardwareMap.get(DcMotorEx.class, "LiftingMotorRight");
    }

    public void extendLift(double targetPosition) {
        fullyExtended = false;
        this.targetPosition = targetPosition;

        while (!fullyExtended) {
            double currentPosition = (Motor1.getCurrentPosition() + Motor2.getCurrentPosition()) / 2.0;
            double error = targetPosition - currentPosition;
            double power = error * KP;

            if (error < errorMargin) {
                power = 0;
            } else {
                if (power > MAX_POWER) {
                    power = MAX_POWER;
                } else if (power < -MAX_POWER) {
                    power = -MAX_POWER;
                }
            }
            Motor1.setPower(power);
            Motor2.setPower(power);

            if (Math.abs(error) < 10.0) {
                fullyExtended = true;
                Motor1.setPower(0.0); // Stop the motors
                Motor2.setPower(0.0);
            }
        }
    }

    public void liftRobot(double targetPosition) {
        Motor1.setPower(-MAX_POWER); // Reverse power to lift the robot
        Motor2.setPower(-MAX_POWER);
        fullyExtended = false;

        while (!fullyExtended) {
            double currentPosition = (Motor1.getCurrentPosition() + Motor2.getCurrentPosition()) / 2.0;
            if (currentPosition <= targetPosition) {
                fullyExtended = true;
                Motor1.setPower(0.0); // Stop the motors
                Motor2.setPower(0.0);
            }
        }
    }
}
