package org.whitneyrobotics.ftc.teamcode.OpMode.TeleOp;

import static org.whitneyrobotics.ftc.teamcode.Constants.FieldConstants.BACKDROP_EDGE_FROM_WALL;
import static org.whitneyrobotics.ftc.teamcode.Constants.FieldConstants.PIXEL_WIDTH;
import static org.whitneyrobotics.ftc.teamcode.Constants.RobotDetails.ROBOT_LENGTH;
import static org.whitneyrobotics.ftc.teamcode.Constants.RobotDetails.ROBOT_WIDTH;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.path.Path;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.whitneyrobotics.ftc.teamcode.Constants.Alliance;
import org.whitneyrobotics.ftc.teamcode.Constants.FieldConstants;
import org.whitneyrobotics.ftc.teamcode.Extensions.OpModeEx.OpModeEx;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.LineItem;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.MultipleChoicePoll;
import org.whitneyrobotics.ftc.teamcode.Libraries.Utilities.Functions;
import org.whitneyrobotics.ftc.teamcode.Libraries.Utilities.UnitConversion.DistanceUnit;
import org.whitneyrobotics.ftc.teamcode.Roadrunner.drive.CenterstageMecanumDrive;
import org.whitneyrobotics.ftc.teamcode.Roadrunner.util.DashboardUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

@TeleOp()
public class Driver1Test extends OpModeEx {
    private CenterstageMecanumDrive drivetrain;
    private boolean robotCentric = false;
    private Alliance alliance = Alliance.BLUE;

    List<Pose2d> poseHistory = new ArrayList<>();

    private MultipleChoicePoll tileSelector;
    private UnaryOperator<Float> scalingFunctionDefault = x -> (float)Math.pow(x, 3);
    private String navPath = "";
    private FieldConstants.StartingTiles startingTile = FieldConstants.StartingTiles.BLUE_A4;

    @Override
    public void initInternal() {
        drivetrain = new CenterstageMecanumDrive(hardwareMap);
        drivetrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        gamepad1.START.onPress(() -> {
            Pose2d previousPosition = drivetrain.getPoseEstimate();
            drivetrain.getLocalizer().setPoseEstimate(new Pose2d(
                    previousPosition.getX(),
                    previousPosition.getY(),
                    0 + (alliance == Alliance.RED ? Math.PI : 0)
            ));
        });
        gamepad1.BUMPER_RIGHT.onPress(() -> robotCentric = !robotCentric);
        gamepad1.SELECT.onPress(poseHistory::clear);

        tileSelector = new MultipleChoicePoll("Select Tile", false,
                new MultipleChoicePoll.MultipleChoiceOption("Backstage" , 1),
                new MultipleChoicePoll.MultipleChoiceOption("Audience",0));

        telemetryPro.useDashboardTelemetry(dashboardTelemetry);
        telemetryPro.setInteractingGamepad(gamepad1, g->g.DPAD_DOWN, g->g.DPAD_UP);
        gamepad1.CIRCLE.onPress(drivetrain::cancelTrajectory);
        gamepad1.LEFT_STICK_X.onInteraction(drivetrain::cancelTrajectory);
        gamepad1.LEFT_STICK_Y.onInteraction(drivetrain::cancelTrajectory);
        gamepad1.RIGHT_STICK_X.onInteraction(drivetrain::cancelTrajectory);
        telemetryPro.addItem(tileSelector);
    }

    @Override
    public void initInternalLoop(){
        switch((int)tileSelector.getSelected()[0].getValue()){
            case 0:
                startingTile = alliance == Alliance.RED ? FieldConstants.StartingTiles.RED_F2 : FieldConstants.StartingTiles.BLUE_A2;
                break;
            case 1:
                startingTile = alliance == Alliance.RED ? FieldConstants.StartingTiles.RED_F4 : FieldConstants.StartingTiles.BLUE_A4;
                break;
        }
        telemetryPro.addLine("Starting at " + startingTile.name(), LineItem.Color.LIME);
        telemetryPro.addData("Alliance", (alliance == Alliance.RED ? "RED" : "BLUE"), (alliance == Alliance.RED ? LineItem.Color.RED : LineItem.Color.BLUE));
    }

    @Override
    public void startInternal() {
        drivetrain.getLocalizer().setPoseEstimate(startingTile.pose);
        telemetryPro.removeLineByReference(tileSelector);
    }

    @Override
    protected void loopInternal() {
        float brakePower = gamepad1.LEFT_TRIGGER.value();
        UnaryOperator<Float> scaling = scalingFunctionDefault;
        if(gamepad1.BUMPER_LEFT.value()) scaling = x -> x/2;
        drivetrain.update();
        if (!drivetrain.isBusy()) {
            drivetrain.setWeightedDrivePower(
                    //We use Y for our X movement as our drivers are aligned to a side of the field
                    Functions.rotateVectorCounterclockwise(new Pose2d(
                            scaling.apply(gamepad1.LEFT_STICK_Y.value()),
                            scaling.apply(-gamepad1.LEFT_STICK_X.value()), //When on the left side of the field, our strafe controls are inverted
                            scaling.apply(-gamepad1.RIGHT_STICK_X.value())
                    ).times(1-brakePower), robotCentric ? -drivetrain.getLocalizer().getPoseEstimate().getHeading() : 0)
            );
        }
        if(poseHistory.isEmpty() || (poseHistory.size() > 0 && !poseHistory.get(poseHistory.size()-1).equals(drivetrain.getLocalizer().getPoseEstimate()))){
            poseHistory.add(drivetrain.getLocalizer().getPoseEstimate());
        }

        if(drivetrain.isBusy()){
            telemetryPro.addLine("Autonomous Navigation Enabled", LineItem.Color.FUCHSIA, LineItem.RichTextFormat.BOLD);
            telemetryPro.addData("Path", navPath);
        }
        else if(gamepad1.BUMPER_LEFT.value()) telemetryPro.addLine("SLOW MODE ENABLED", LineItem.Color.LIME, LineItem.RichTextFormat.BOLD);
        if(robotCentric) telemetryPro.addLine("ROBOT CENTRIC ENABLED", LineItem.Color.YELLOW, LineItem.RichTextFormat.BOLD);
        telemetryPro.addData("Alliance", (alliance == Alliance.RED ? "RED" : "BLUE"), (alliance == Alliance.RED ? LineItem.Color.RED : LineItem.Color.BLUE));
        telemetryPro.addData("x", drivetrain.getPoseEstimate().getX());
        telemetryPro.addData("y", drivetrain.getPoseEstimate().getY());
        telemetryPro.addData("thetaDeg", Math.toDegrees(drivetrain.getPoseEstimate().getHeading()));
        telemetryPro.addData("brake", brakePower);
        packet.fieldOverlay()
                .setStrokeWidth(2)
                .setStroke(LineItem.Color.AQUA.getHexCode())
                .strokeCircle(drivetrain.getPoseEstimate().getX(), drivetrain.getPoseEstimate().getY(), ROBOT_WIDTH/2)
                .setStroke(LineItem.Color.ROBOTICS.getHexCode() + "7a");

        DashboardUtil.drawPoseHistory(packet.fieldOverlay(), poseHistory);

    }
}
