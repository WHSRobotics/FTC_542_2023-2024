package org.whitneyrobotics.ftc.teamcode.OpMode.Autonomous;

import static org.whitneyrobotics.ftc.teamcode.Constants.Alliance.BLUE;
import static org.whitneyrobotics.ftc.teamcode.Constants.Alliance.RED;
import static org.whitneyrobotics.ftc.teamcode.Constants.FieldConstants.StartingTiles.*;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
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
import org.whitneyrobotics.ftc.teamcode.Subsystems.AllianceSensor;
import org.whitneyrobotics.ftc.teamcode.Subsystems.ColorSubsystem;
import org.whitneyrobotics.ftc.teamcode.Subsystems.RobotImpl;
import org.whitneyrobotics.ftc.teamcode.Tests.ML.ML.TensorFlowM1;
import org.whitneyrobotics.ftc.teamcode.VisionImpl.AprilTagScanner23_24;

import java.util.List;

@Autonomous(name="M0 Auto", preselectTeleOp="Centerstage TeleOp")
public class M0AutoOp extends OpModeEx {
    RobotImpl robot;
    MultipleChoicePoll tileSelector;
    String selectedTrajectory;
    AllianceSensor allianceSensor;

    TensorFlowM1 cameraView;


    private int numeric_path;



    @Override
    public void initInternal() {
        RobotImpl.init(hardwareMap);
        robot = RobotImpl.getInstance();
        robot.colorSubsystem.bindGamepads(gamepad1, gamepad2);
        robot.drive.enableRobotDrawing();
        telemetryPro.useTestManager()
                .addTest("Gamepad 1 Initialization", () -> Tests.assertGamepadSetup(gamepad1, "Gamepad 1"))
                .addTest("Gamepad 2 Initialization", () -> Tests.assertGamepadSetup(gamepad2, "Gamepad 2"))
                .addTest("Battery voltage test", () -> Tests.assertBatteryCharged(hardwareMap.get(LynxModule.class, "Control Hub")));
        tileSelector = new MultipleChoicePoll("Select Tile", false,
                new MultipleChoicePoll.MultipleChoiceOption<>("Backstage" , FieldConstants.FieldSide.BACKSTAGE),
                new MultipleChoicePoll.MultipleChoiceOption<>("Audience",FieldConstants.FieldSide.AUDIENCE));
        telemetryPro.setInteractingGamepad(gamepad1);
        gamepad1.CIRCLE.onPress(robot::switchAlliance);
        telemetryPro.addItem(tileSelector);
        telemetryPro.useDashboardTelemetry(dashboardTelemetry);
        dashboardTelemetry.setMsTransmissionInterval(25);
        allianceSensor = new AllianceSensor(hardwareMap);

        cameraView = new TensorFlowM1();
        cameraView.initTfod();

        robot.alliance = allianceSensor.isRedAlliance() ? RED : BLUE;
        telemetryPro.addData("Initial Alliance", robot.alliance.name(), robot.alliance == RED ? LineItem.Color.RED : LineItem.Color.BLUE).persistent();
        allianceSensor.onChange(isRed -> {
            robot.alliance = isRed ? RED : BLUE;
            telemetryPro.addData("Alliance changed to", robot.alliance.name(), robot.alliance == RED ? LineItem.Color.RED : LineItem.Color.BLUE).persistent();
            playSound("chime");
        });
        gamepad1.A.onPress(() -> {
            robot.alliance = robot.alliance == RED ? BLUE : RED;
            telemetryPro.addData("Alliance manually changed to", robot.alliance.name(), robot.alliance == RED ? LineItem.Color.RED : LineItem.Color.BLUE, LineItem.RichTextFormat.ITALICS).persistent();
        });
    }

    @Override
    public void initInternalLoop(){
        allianceSensor.update();
        //numeric_path = cameraView.path();
        numeric_path = 1;
        List<TestManager.Test> results =  telemetryPro.getTestManager().run();
        ColorSubsystem.Colors desiredColor = ColorSubsystem.Colors.GREEN_PIXEL;
        if(results.stream().anyMatch(test -> test.getWarning() || test.getFailed())){
            desiredColor = ColorSubsystem.Colors.BUSY;
        }
        if(gamepad1.TRIANGLE.value()){ //Press and hold triangle to check alliance
            desiredColor = robot.alliance == Alliance.RED ? ColorSubsystem.Colors.RED : ColorSubsystem.Colors.BLUE;
        }
        robot.colorSubsystem.requestColor(desiredColor);
        robot.colorSubsystem.update();
        telemetryPro.addData("Alliance", robot.alliance.name(), (robot.alliance == Alliance.RED ? LineItem.Color.RED : LineItem.Color.BLUE));
        robot.drive.sendPacket(packet);
    }

    @Override
    public void startInternal() {
        gamepad1.CIRCLE.disconnectAllHandlers();
        cameraView.updateAprilTagDetections();
        TrajectorySequence desiredTrajectory = null;
        switch (robot.alliance){
            case RED:
                if(tileSelector.getSelected()[0].getValue() == FieldConstants.FieldSide.AUDIENCE){
                    if (numeric_path == 1){
                        desiredTrajectory = AutoPaths.RedAudienceLeft(robot.drive, cameraView);
                    }else if  (numeric_path == 2){
                        desiredTrajectory = AutoPaths.RedAudienceCenter(robot.drive, cameraView);
                    } else if (numeric_path == 3){
                        desiredTrajectory = AutoPaths.RedAudienceRight(robot.drive, cameraView);

                    }
                    robot.drive.getLocalizer().setPoseEstimate(RED_F2.pose);
                    selectedTrajectory = "RED AUDIENCE";
                } else {
                    if (numeric_path == 1){
                        desiredTrajectory = AutoPaths.RedBackstageLeft(robot.drive, cameraView);
                    }else if  (numeric_path == 2){
                        desiredTrajectory = AutoPaths.RedBackstageCenter(robot.drive, cameraView);
                    } else if (numeric_path == 3){
                        desiredTrajectory = AutoPaths.RedBackstageRight(robot.drive, cameraView);

                    }
                    robot.drive.getLocalizer().setPoseEstimate(RED_F4.pose);
                    selectedTrajectory = "RED BACKSTAGE";
                }
                break;
            case BLUE:
                if(tileSelector.getSelected()[0].getValue() == FieldConstants.FieldSide.AUDIENCE){
                    if (numeric_path == 1){
                        desiredTrajectory = AutoPaths.BlueAudienceLeft(robot.drive, cameraView);
                    }else if  (numeric_path == 2){
                        desiredTrajectory = AutoPaths.BlueAudienceCenter(robot.drive, cameraView);
                    } else if (numeric_path == 3){
                        desiredTrajectory = AutoPaths.BlueAudienceRight(robot.drive, cameraView);
                    }

                    robot.drive.getLocalizer().setPoseEstimate(BLUE_A2.pose);
                    selectedTrajectory = "BLUE AUDIENCE";
                } else {
                    if (numeric_path == 1){
                        desiredTrajectory = AutoPaths.BlueBackstageLeft(robot.drive, cameraView);
                    }else if  (numeric_path == 2){
                        desiredTrajectory = AutoPaths.BlueBackstageCenter(robot.drive, cameraView);
                    } else if (numeric_path == 3){
                        desiredTrajectory = AutoPaths.BlueBackstageRight(robot.drive, cameraView);
                    }
                    robot.drive.getLocalizer().setPoseEstimate(BLUE_A4.pose);
                    selectedTrajectory = "BLUE BACKSTAGE";
                }
                break;
        }
        if(desiredTrajectory != null) robot.drive.followTrajectorySequenceAsync(desiredTrajectory);

    }

    @Override
    protected void loopInternal() {
        robot.drive.sendPacket(packet);
        robot.update();
        telemetryPro.addData("Trajectory",selectedTrajectory);
        RobotImpl.poseMemory = robot.drive.getPoseEstimate();
    }
}
