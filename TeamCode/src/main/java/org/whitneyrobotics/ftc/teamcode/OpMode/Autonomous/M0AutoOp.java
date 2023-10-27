package org.whitneyrobotics.ftc.teamcode.OpMode.Autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.whitneyrobotics.ftc.teamcode.Constants.Alliance;
import org.whitneyrobotics.ftc.teamcode.Constants.AutoPaths;
import org.whitneyrobotics.ftc.teamcode.Constants.FieldConstants;
import org.whitneyrobotics.ftc.teamcode.Extensions.OpModeEx.OpModeEx;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.AutoSetupTesting.TestManager;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.AutoSetupTesting.Tests;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.KeyValueLine;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.LineItem;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.MultipleChoicePoll;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.TelemetryPro;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.TextLine;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.TextLineLambda;
import org.whitneyrobotics.ftc.teamcode.Roadrunner.trajectorysequence.TrajectorySequence;
import org.whitneyrobotics.ftc.teamcode.Subsystems.ColorSubsystem;
import org.whitneyrobotics.ftc.teamcode.Subsystems.RobotImpl;

import java.util.List;

@Autonomous(name="M0 Auto")
public class M0AutoOp extends OpModeEx {
    RobotImpl robot;
    MultipleChoicePoll tileSelector;
    @Override
    public void initInternal() {
        robot = RobotImpl.getInstance(hardwareMap);
        telemetryPro.useTestManager()
                .addTest("Gamepad 1 Initialization", () -> Tests.assertGamepadSetup(gamepad1, "Gamepad 1"))
                .addTest("Gamepad 2 Initialization", () -> Tests.assertGamepadSetup(gamepad2, "Gamepad 2"))
                .addTest("Battery voltage test", () -> Tests.assertBatteryCharged(hardwareMap.get(LynxModule.class, "Control Hub")));
        telemetryPro.addItem(new KeyValueLine("Alliance", true, robot.alliance::name, (robot.alliance == Alliance.RED ? LineItem.Color.RED : LineItem.Color.BLUE)));
        tileSelector = new MultipleChoicePoll("Select Tile", false,
                new MultipleChoicePoll.MultipleChoiceOption("Backstage" , 1),
                new MultipleChoicePoll.MultipleChoiceOption("Audience",0));
        gamepad1.CIRCLE.onPress(robot::switchAlliance);
    }


    @Override
    public void initInternalLoop(){
        List<TestManager.Test> results =  telemetryPro.getTestManager().run();
        ColorSubsystem.Colors desiredColor = ColorSubsystem.Colors.GREEN_PIXEL;
        if(results.stream().anyMatch(test -> test.getWarning() || test.getFailed())){
            desiredColor = ColorSubsystem.Colors.ERROR_FLASHING;
        }
        if(gamepad1.TRIANGLE.value()){ //Press and hold triangle to check alliance
            desiredColor = robot.alliance == Alliance.RED ? ColorSubsystem.Colors.RED : ColorSubsystem.Colors.BLUE;
        }
        robot.colorSubsystem.requestColor(desiredColor);
    }

    @Override
    public void startInternal() {
        gamepad1.CIRCLE.disconnectAllHandlers();
        TrajectorySequence desiredTrajectory = null;
        switch (robot.alliance){
            case RED:
                if(tileSelector.getSelected()[0].getValue() == FieldConstants.FieldSide.AUDIENCE){
                    desiredTrajectory = AutoPaths.buildRedAudience(robot.drive);
                } else desiredTrajectory = AutoPaths.buildRedBackstage(robot.drive);
                break;
            case BLUE:
                if(tileSelector.getSelected()[0].getValue() == FieldConstants.FieldSide.AUDIENCE){
                    desiredTrajectory = AutoPaths.buildBlueAudience(robot.drive);
                } else desiredTrajectory = AutoPaths.buildBlueBackstage(robot.drive);
                break;
        }
        if(desiredTrajectory != null) robot.drive.followTrajectorySequenceAsync(desiredTrajectory);
    }

    @Override
    protected void loopInternal() {
        robot.update();
    }
}
