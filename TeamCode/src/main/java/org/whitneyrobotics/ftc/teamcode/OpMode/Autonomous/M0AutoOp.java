package org.whitneyrobotics.ftc.teamcode.OpMode.Autonomous;

import static org.whitneyrobotics.ftc.teamcode.Constants.Alliance.BLUE;
import static org.whitneyrobotics.ftc.teamcode.Constants.Alliance.RED;
import static org.whitneyrobotics.ftc.teamcode.Constants.FieldConstants.StartingTiles.BLUE_A2;
import static org.whitneyrobotics.ftc.teamcode.Constants.FieldConstants.StartingTiles.BLUE_A4;
import static org.whitneyrobotics.ftc.teamcode.Constants.FieldConstants.StartingTiles.RED_F2;
import static org.whitneyrobotics.ftc.teamcode.Constants.FieldConstants.StartingTiles.RED_F4;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.whitneyrobotics.ftc.teamcode.Constants.Alliance;
import org.whitneyrobotics.ftc.teamcode.Constants.AutoPaths;
import org.whitneyrobotics.ftc.teamcode.Constants.FieldConstants;
import org.whitneyrobotics.ftc.teamcode.Extensions.OpModeEx.OpModeEx;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.AutoSetupTesting.TestManager;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.AutoSetupTesting.Tests;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.LineItem;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.MultipleChoicePoll;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.NumberSliderPoll;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.TelemetryPro;
import org.whitneyrobotics.ftc.teamcode.Roadrunner.trajectorysequence.TrajectorySequence;
import org.whitneyrobotics.ftc.teamcode.Subsystems.AllianceSensor;
//import org.whitneyrobotics.ftc.teamcode.Subsystems.ColorSubsystem;
import org.whitneyrobotics.ftc.teamcode.Subsystems.ColorSubsystem;
import org.whitneyrobotics.ftc.teamcode.Subsystems.RobotImpl;

import java.util.List;

@Autonomous(name="M0 Auto", preselectTeleOp="Centerstage TeleOp")
public class M0AutoOp extends OpModeEx {
    RobotImpl robot;
    MultipleChoicePoll tileSelector;
    NumberSliderPoll delaySelector;
    String selectedTrajectory;
    AllianceSensor allianceSensor;

    AutoPaths paths;


    private int numeric_path;



    @Override
    public void initInternal() {
        RobotImpl.init(hardwareMap);
        robot = RobotImpl.getInstance();
        robot.colorSubsystem.bindGamepads(gamepad1, gamepad2);
        robot.drive.enableRobotDrawing();
        robot.drone.init();

        telemetryPro.useTestManager()
                .addTest("Gamepad 1 Initialization", () -> Tests.assertGamepadSetup(gamepad1, "Gamepad 1"))
                .addTest("Gamepad 2 Initialization", () -> Tests.assertGamepadSetup(gamepad2, "Gamepad 2"))
                .addTest("Battery voltage test", () -> Tests.assertBatteryCharged(hardwareMap.get(LynxModule.class, "Control Hub")));
        tileSelector = new MultipleChoicePoll("Select Tile", false,
                new MultipleChoicePoll.MultipleChoiceOption<>("Backstage" , FieldConstants.FieldSide.BACKSTAGE),
                new MultipleChoicePoll.MultipleChoiceOption<>("Audience",FieldConstants.FieldSide.AUDIENCE));
        delaySelector = new NumberSliderPoll.NumberSliderPollBuilder("Select delay (for audience side)", LineItem.Color.ROBOTICS, LineItem.RichTextFormat.ITALICS)
                .allowWrap(true)
                .setLargeStep(1)
                .setLargeStep(3)
                .setMin(0)
                .setInitial(3)
                .setMax(10)
                .build();
        telemetryPro.setInteractingGamepad(gamepad1);
        telemetryPro.addItem(tileSelector);
        telemetryPro.addItem(delaySelector);
        telemetryPro.useDashboardTelemetry(dashboardTelemetry);
        dashboardTelemetry.setMsTransmissionInterval(25);
        allianceSensor = new AllianceSensor(hardwareMap);
        delaySelector.onChange(AutoPaths::setDelay);
        robot.alliance = allianceSensor.isRedAlliance() ? RED : BLUE;

        telemetryPro.addData("Initial Alliance", robot.alliance.name(), robot.alliance == RED ? LineItem.Color.RED : LineItem.Color.BLUE).persistent();
        allianceSensor.onChange(isRed -> {
            robot.alliance = isRed ? RED : BLUE;
            telemetryPro.addData("Alliance changed to", robot.alliance.name(), robot.alliance == RED ? LineItem.Color.RED : LineItem.Color.BLUE).persistent();
            playSound("chime");
        });
        gamepad1.CIRCLE.onPress(() -> {
            robot.switchAlliance();
            telemetryPro.addData("Alliance manually changed to", robot.alliance.name(), robot.alliance == RED ? LineItem.Color.RED : LineItem.Color.BLUE, LineItem.RichTextFormat.ITALICS).persistent();
        });
        if (robot.alliance == RED){
            OpenCVRed.initalizeCamCV(hardwareMap);
        }else if (robot.alliance == BLUE){
            OpenCVBlue.initalizeCamCV(hardwareMap);
        }



    }

    @Override
    public void initInternalLoop(){
        allianceSensor.update();

        if (robot.alliance == RED){
            numeric_path = OpenCVRed.Pipeline.convertEnumToInteger();
            telemetryPro.addData("Numeric Path",numeric_path);
            telemetryPro.addData(" Path",OpenCVRed.Pipeline.getPosition());
        }else if (robot.alliance == BLUE){
            numeric_path = OpenCVBlue.Pipeline.convertEnumToInteger();
            telemetryPro.addData("Numeric Path",numeric_path);
            telemetryPro.addData(" Path",OpenCVBlue.Pipeline.getPosition());        }

        //numeric_path = cameraView.path();
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
        robot.intake.raisedPosition();
        robot.intake.update();
        telemetryPro.addData("Alliance", robot.alliance.name(), (robot.alliance == Alliance.RED ? LineItem.Color.RED : LineItem.Color.BLUE));
        robot.drive.sendPacket(packet);


    }

    @Override
    public void startInternal() {
        if (robot.alliance == RED){
            OpenCVRed.stopCamera();
        }else if (robot.alliance == BLUE){
            OpenCVBlue.stopCamera();
        }
        gamepad1.CIRCLE.disconnectAllHandlers();
        //cameraView.updateAprilTagDetections();
        TrajectorySequence desiredTrajectory = null;
        switch (robot.alliance){
            case RED:

                if(tileSelector.getSelected()[0].getValue() == FieldConstants.FieldSide.AUDIENCE){
                    if (numeric_path == 1){
                        //do THIS ONEEEEEEEEE
                        desiredTrajectory = AutoPaths.RedAudienceLeft(robot.drive);
                    }else if  (numeric_path == 2){
                        desiredTrajectory = AutoPaths.RedAudienceCenter(robot.drive);
                    } else if (numeric_path == 3){
                        desiredTrajectory = AutoPaths.RedAudienceRight(robot.drive);

                    }
                    robot.drive.getLocalizer().setPoseEstimate(RED_F2.pose);
                    selectedTrajectory = "RED AUDIENCE";
                } else {
                    if (numeric_path == 1){
                        desiredTrajectory = AutoPaths.RedBackstageLeft(robot.drive);
                    }else if  (numeric_path == 2){
                        desiredTrajectory = AutoPaths.RedBackstageCenter(robot.drive);
                    } else if (numeric_path == 3){
                        desiredTrajectory = AutoPaths.RedBackstageRight(robot.drive);

                    }
                    robot.drive.getLocalizer().setPoseEstimate(RED_F4.pose);
                    selectedTrajectory = "RED BACKSTAGE";
                }
                break;
            case BLUE:
                if(tileSelector.getSelected()[0].getValue() == FieldConstants.FieldSide.AUDIENCE){
                    if (numeric_path == 1){
                        desiredTrajectory = AutoPaths.BlueAudienceLeft(robot.drive);
                    }else if  (numeric_path == 2){
                        desiredTrajectory = AutoPaths.BlueAudienceCenter(robot.drive);
                    } else if (numeric_path == 3){
                        desiredTrajectory = AutoPaths.BlueAudienceRight(robot.drive);
                    }

                    robot.drive.getLocalizer().setPoseEstimate(BLUE_A2.pose);
                    selectedTrajectory = "BLUE AUDIENCE";
                } else {
                    if (numeric_path == 1){
                        desiredTrajectory = AutoPaths.BlueBackstageLeft(robot.drive);
                    }else if  (numeric_path == 2){
                        desiredTrajectory = AutoPaths.BlueBackstageCenter(robot.drive);
                    } else if (numeric_path == 3){
                        desiredTrajectory = AutoPaths.BlueBackstageRight(robot.drive);
                    }
                    robot.drive.getLocalizer().setPoseEstimate(BLUE_A4.pose);
                    selectedTrajectory = "BLUE BACKSTAGE";
                }

            /*
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

             */
        }
        if(desiredTrajectory != null) robot.drive.followTrajectorySequenceAsync(desiredTrajectory);

    }

    @Override
    protected void loopInternal() {
        AutoPaths.setAutoSubsystems(robot.purpleAuto,robot.elbowWrist,robot.gate, robot.intake);
        robot.drive.sendPacket(packet);
        robot.update();
        telemetryPro.addData("Trajectory",selectedTrajectory);
        RobotImpl.poseMemory = robot.drive.getPoseEstimate();
    }
}
