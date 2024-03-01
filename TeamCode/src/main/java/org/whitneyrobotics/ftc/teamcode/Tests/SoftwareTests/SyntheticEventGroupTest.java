package org.whitneyrobotics.ftc.teamcode.Tests.SoftwareTests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whitneyrobotics.ftc.teamcode.Extensions.GamepadEx.SyntheticCaptureGroup;
import org.whitneyrobotics.ftc.teamcode.Extensions.OpModeEx.OpModeEx;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.LineItem;

@TeleOp(name="Synthetic Capture Group Test", group="Software Tests")
public class SyntheticEventGroupTest extends OpModeEx {
    @Override
    public void initInternal() {
        gamepad1.CROSS.onPress(() -> telemetryPro.addLine("A pressed", LineItem.Color.BLUE).persistent());
        gamepad1.CIRCLE.onPress(() -> telemetryPro.addLine("B pressed").persistent());
        gamepad1.SQUARE.onPress(telemetryPro::clear);
        SyntheticCaptureGroup group = gamepad1.addSyntheticCaptureGroup(gamepad1.A,gamepad1.B);
        group.onPress(() -> telemetryPro.addLine("Event group triggered").persistent());
    }

    @Override
    protected void loopInternal() {

    }
}
