package org.whitneyrobotics.ftc.teamcode.Tests.SubsystemTests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whitneyrobotics.ftc.teamcode.Extensions.OpModeEx.OpModeEx;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Intake;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Outtake.Elbow;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Outtake.Gate;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Outtake.Wrist;

@TeleOp (name = "Claw Plus Elbow Test")
public class ClawAndElbowTest extends OpModeEx {
    Elbow elbow;
    Wrist wrist;
    Gate gate;

    Meet3Intake intake;
    private int intakeState = 0;
    private boolean unPressed = true;

    @Override
    public void initInternal() {
        elbow = new Elbow(hardwareMap);
        wrist = new Wrist(hardwareMap);
        gate = new Gate(hardwareMap);

        intake = new Meet3Intake(hardwareMap);

        gamepad1.TRIANGLE.onPress(() -> elbow.update());
        gamepad1.CIRCLE.onPress(() -> wrist.update());
        gamepad1.CROSS.onPress(() -> gate.update());
    }

    @Override
    protected void loopInternal() {
        elbow.run();
        wrist.run();
        gate.run();
        intake.update();

        intake.stackPosition();

        if (gamepad1.SQUARE.value() && unPressed){
            intakeState = (intakeState + 1) % 2;
            unPressed = false;
        }

        if (!gamepad1.SQUARE.value()){
            unPressed = true;
        }

        if (!gamepad1.BUMPER_LEFT.value()){
            intake.setReversed(false);
            intake.setRPM(312 * intakeState);
        } else {
            intake.setReversed(true);
        }
    }
}
