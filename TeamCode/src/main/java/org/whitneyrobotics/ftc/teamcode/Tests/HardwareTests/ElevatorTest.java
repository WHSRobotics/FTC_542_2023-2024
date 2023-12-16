package org.whitneyrobotics.ftc.teamcode.Tests.HardwareTests;

import static org.whitneyrobotics.ftc.teamcode.Subsystems.Odometry.ArmElevator.Target.*;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whitneyrobotics.ftc.teamcode.Extensions.OpModeEx.OpModeEx;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Odometry.ArmElevator;

@TeleOp(name="Elevator Test", group="Hardware Tests")
public class ElevatorTest extends OpModeEx {
    ArmElevator elevator;
    @Override
    public void initInternal() {
        elevator = new ArmElevator(hardwareMap);
        telemetryPro.useDashboardTelemetry(dashboardTelemetry);
        dashboardTelemetry.setMsTransmissionInterval(25);
        gamepad1.BUMPER_LEFT.onPress(elevator::slowModeOn);
        gamepad1.BUMPER_LEFT.onRelease(elevator::slowModeOff);
        gamepad1.CROSS.onPress(() -> elevator.setTargetPosition(RETRACT));
        gamepad1.SQUARE.onPress(() -> elevator.setTargetPosition(ONE));
        gamepad1.TRIANGLE.onPress(() -> elevator.setTargetPosition(TWO));
        gamepad1.CIRCLE.onPress(() -> elevator.setTargetPosition(THREE));
        gamepad1.START.onPress(elevator::resetEncoders);
    }

    @Override
    protected void loopInternal() {
        elevator.inputPower(gamepad1.LEFT_STICK_Y.value());
        elevator.update();
        telemetryPro.addData("Elevator State", elevator.getState());
        telemetryPro.addData("Elevator Position", elevator.getPosition());
        telemetryPro.addData("Elevator Velocity", elevator.getVelocity());
        telemetryPro.addData("Desired Position", elevator.getDesiredPosition());
        telemetryPro.addData("Desired Velocity", elevator.getDesiredVelocity());
        telemetryPro.addData("Elevator Acceleration", elevator.getDesiredAcceleration());
        telemetryPro.addData("Target Position", elevator.getTargetPosition());
    }
}
