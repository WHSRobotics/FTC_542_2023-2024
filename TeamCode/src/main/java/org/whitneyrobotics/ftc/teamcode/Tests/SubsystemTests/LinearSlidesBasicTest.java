package org.whitneyrobotics.ftc.teamcode.Tests.SubsystemTests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whitneyrobotics.ftc.teamcode.Extensions.OpModeEx.OpModeEx;
import org.whitneyrobotics.ftc.teamcode.Subsystems.newLinearSlides;

@TeleOp (name = "Basic Linear Slides Test")
public class LinearSlidesBasicTest extends OpMode {
    newLinearSlides linearSlides;

    @Override
    public void init() {
        newLinearSlides linearSlides = new newLinearSlides(hardwareMap);

    }

    @Override
    public void loop() {
        linearSlides.setTarget(gamepad1.right_stick_y);
        linearSlides.operate(gamepad1.left_stick_y, gamepad1.left_bumper);
    }
}
