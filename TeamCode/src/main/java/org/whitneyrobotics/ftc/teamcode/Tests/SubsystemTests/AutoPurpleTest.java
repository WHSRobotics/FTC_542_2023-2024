package org.whitneyrobotics.ftc.teamcode.Tests.SubsystemTests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whitneyrobotics.ftc.teamcode.Extensions.OpModeEx.OpModeEx;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Auto.PurpleServo;


@TeleOp(name = "purpleTest", group = "test")
public class AutoPurpleTest extends OpModeEx {
    PurpleServo purpleServo;

    @Override
    public void initInternal() {
        purpleServo = new PurpleServo(hardwareMap);
        gamepad1.TRIANGLE.onPress(() ->  purpleServo.setState(PurpleServo.PurplePositions.CLOSED));
        gamepad1.CIRCLE.onPress(() ->  purpleServo.setState(PurpleServo.PurplePositions.OPEN));



    }

    @Override
    protected void loopInternal() {
        //purpleServo.run();
        purpleServo.update();
    }
}
