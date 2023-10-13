package org.whitneyrobotics.ftc.teamcode.Tests.SoftwareTests;

import org.whitneyrobotics.ftc.teamcode.Extensions.OpModeEx.OpModeEx;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.LineItem;
import org.whitneyrobotics.ftc.teamcode.Subsystems.ColorSubsystem;
import org.whitneyrobotics.ftc.teamcode.Tests.Test;

import static org.whitneyrobotics.ftc.teamcode.Subsystems.ColorSubsystem.Colors;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Test(name="LED Test", description = "Tests the ability of the LED Controller to maintain parity between controller LEDs and Rev Blinkin LEDs. Press square to increment color.")
@TeleOp(name="LED Test", group="Software Tests")
public class LEDTest extends OpModeEx {
    ColorSubsystem leds;
    Colors currentColor = Colors.values()[0];
    @Override
    public void initInternal() {
        leds = new ColorSubsystem(hardwareMap);
        leds.bindGamepads(gamepad1, gamepad2);
        gamepad1.SQUARE.onPress(this::nextColor);
    }

    @Override
    protected void loopInternal() {
        leds.update();
        telemetryPro.addData("Current State", currentColor.name(), LineItem.Color.LIME);
    }

    void nextColor() {
        currentColor = Colors.values()[(currentColor.ordinal()+1) % Colors.values().length];
        leds.requestColor(currentColor);
    }
}
