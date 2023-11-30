package org.whitneyrobotics.ftc.teamcode.Constants;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.whitneyrobotics.ftc.teamcode.OpMode.Autonomous.M0AutoOp;
import org.whitneyrobotics.ftc.teamcode.Roadrunner.drive.CenterstageMecanumDrive;
import org.whitneyrobotics.ftc.teamcode.Roadrunner.trajectorysequence.TrajectorySequence;
import org.whitneyrobotics.ftc.teamcode.Tests.ML.ML.TensorFlowM1;

import static org.whitneyrobotics.ftc.teamcode.Constants.FieldConstants.StartingTiles.*;
import static org.whitneyrobotics.ftc.teamcode.Libraries.Utilities.UnitConversion.DistanceUnit.TILE_WIDTH;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

public class AutoPaths {

    private double offset;
    public static final TrajectorySequence BlueBackstageLeft(CenterstageMecanumDrive drivetrain,
                                                             TensorFlowM1 camera) {
        return drivetrain.trajectorySequenceBuilder(BLUE_A4.pose)
                .lineToLinearHeading(new Pose2d(32.9, 28.5, Math.toRadians(180)))
                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })

                .lineToLinearHeading(new Pose2d(42, 42.5,Math.toRadians(180)))
                .strafeLeft(camera.getDesiredTagX())
                .addTemporalMarker(() ->{

                })
                .waitSeconds(1)

                .lineToLinearHeading(new Pose2d(42, 12.7,Math.toRadians(180)))
                .back(15)
                .build();
    }
    public static final TrajectorySequence BlueBackstageCenter(CenterstageMecanumDrive drivetrain,
                                                               TensorFlowM1 camera) {
        return drivetrain.trajectorySequenceBuilder(BLUE_A4.pose)
                .lineToLinearHeading(new Pose2d(23.3, 24, Math.toRadians(180)))
                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .lineToLinearHeading(new Pose2d(42, 34.8, Math.toRadians(180)))
                .addTemporalMarker(() ->{

                })
                .waitSeconds(1)

                .lineToLinearHeading(new Pose2d(42, 12.7,Math.toRadians(180)))
                .back(15)
                .build();
    }
    public static final TrajectorySequence BlueBackstageRight(CenterstageMecanumDrive drivetrain,
                                                              TensorFlowM1 camera) {
        return drivetrain.trajectorySequenceBuilder(BLUE_A4.pose)
                .lineToLinearHeading(new Pose2d(12.5, 30.8, Math.toRadians(180)))
                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .lineToLinearHeading(new Pose2d(42, 28, Math.toRadians(180)))
                .strafeLeft(camera.getDesiredTagX())
                .addTemporalMarker(() ->{

                })
                .waitSeconds(1)

                .lineToLinearHeading(new Pose2d(42, 12.7,Math.toRadians(180)))
                .back(15)
                .build();
    }

    public static final TrajectorySequence RedBackstageLeft(CenterstageMecanumDrive drivetrain,
                                                            TensorFlowM1 camera){
        return drivetrain.trajectorySequenceBuilder(RED_F4.pose)
                .lineToLinearHeading(new Pose2d(12.5, -28.5, Math.toRadians(180)))
                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .lineToLinearHeading(new Pose2d(42, -27.5, Math.toRadians(180)))
                .strafeLeft(camera.getDesiredTagX())
                .addTemporalMarker(() ->{

                })
                .waitSeconds(1)

                .lineToLinearHeading(new Pose2d(42, -11,Math.toRadians(180)))
                .back(15)
                .build();
    }
    public static final TrajectorySequence RedBackstageCenter(CenterstageMecanumDrive drivetrain,
                                                              TensorFlowM1 camera){
        return drivetrain.trajectorySequenceBuilder(RED_F4.pose)
                .lineToLinearHeading(new Pose2d(23.3, -24, Math.toRadians(180)))
                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .lineToLinearHeading(new Pose2d(42, -35, Math.toRadians(180)))
                .strafeLeft(camera.getDesiredTagX())
                .addTemporalMarker(() ->{

                })
                .waitSeconds(1)

                .lineToLinearHeading(new Pose2d(42, -11,Math.toRadians(180)))
                .back(15)
                .build();
    }
    public static final TrajectorySequence RedBackstageRight(CenterstageMecanumDrive drivetrain,
                                                             TensorFlowM1 camera){
        return drivetrain.trajectorySequenceBuilder(RED_F4.pose)
                .lineToLinearHeading(new Pose2d(12.5, -28.5, Math.toRadians(180)))
                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .lineToLinearHeading(new Pose2d(42, -27.5, Math.toRadians(180)))
                .strafeLeft(camera.getDesiredTagX())
                .addTemporalMarker(() ->{

                })
                .waitSeconds(1)
                .lineToLinearHeading(new Pose2d(42, -11,Math.toRadians(180)))
                .back(15)
                .build();
    }

    public static final TrajectorySequence BlueAudienceRight(CenterstageMecanumDrive drivetrain,
                                                             TensorFlowM1 camera){
        return drivetrain.trajectorySequenceBuilder(BLUE_A2.pose)

                .lineToLinearHeading(new Pose2d(-35.3, 33.5, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(-23.7, 35, Math.toRadians(180)))

                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .lineToLinearHeading(new Pose2d(42, 35, Math.toRadians(180)))
                .strafeLeft(7)
                .strafeLeft(camera.getDesiredTagX())
                .addTemporalMarker(() ->{

                })
                .waitSeconds(1)

                .lineToLinearHeading(new Pose2d(42, 12.7,Math.toRadians(180)))
                .back(15)

                .build();
    }
    public static final TrajectorySequence BlueAudienceCenter(CenterstageMecanumDrive drivetrain,
                                                              TensorFlowM1 camera){
        return drivetrain.trajectorySequenceBuilder(BLUE_A2.pose)
                .lineToLinearHeading(new Pose2d(-35.3, 29, Math.toRadians(180)))
                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .strafeRight(6)
                .back(10)
                .lineToLinearHeading(new Pose2d(-23.7, 35, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(42, 35, Math.toRadians(180)))
                .strafeLeft(camera.getDesiredTagX())
                .addTemporalMarker(() ->{

                })
                .waitSeconds(1)

                .lineToLinearHeading(new Pose2d(42, 12.7,Math.toRadians(180)))
                .back(15)
                .build();
    }

    public static final TrajectorySequence BlueAudienceLeft(CenterstageMecanumDrive drivetrain,
                                                            TensorFlowM1 camera){
        return drivetrain.trajectorySequenceBuilder(BLUE_A2.pose)
                .lineToLinearHeading(new Pose2d(-35.3, 29, Math.toRadians(0)))

                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .strafeRight(20)
                .splineToConstantHeading(new Vector2d(0.7, 7.8),Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(19, 18),Math.toRadians(0))
                .lineToLinearHeading(new Pose2d(42, 42.5,Math.toRadians(180)))
                .strafeLeft(camera.getDesiredTagX())
                .addTemporalMarker(() ->{

                })
                .waitSeconds(1)

                .lineToLinearHeading(new Pose2d(42, 12.7,Math.toRadians(180)))
                .back(15)
                .build();
    }

    public static final TrajectorySequence RedAudienceLeft(CenterstageMecanumDrive drivetrain,
                                                           TensorFlowM1 camera){
        return drivetrain.trajectorySequenceBuilder(RED_F2.pose)
                .lineToLinearHeading(new Pose2d(-35.3, -29, Math.toRadians(0)))

                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .strafeLeft(20)
                .splineToConstantHeading(new Vector2d(0.7, -7.8),Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(19, -18),Math.toRadians(0))
                .lineToLinearHeading(new Pose2d(42, -42.5, Math.toRadians(180)))
                .strafeLeft(camera.getDesiredTagX())
                .addTemporalMarker(() ->{

                })
                .waitSeconds(1)
                .lineToLinearHeading(new Pose2d(42, -11,Math.toRadians(180)))
                .back(15)
                .build();
    }
    public static final TrajectorySequence RedAudienceCenter(CenterstageMecanumDrive drivetrain,
                                                             TensorFlowM1 camera){
        return drivetrain.trajectorySequenceBuilder(RED_F2.pose)
                .lineToLinearHeading(new Pose2d(-35.3, -33.5, Math.toRadians(90)))
                .lineToLinearHeading(new Pose2d(-23.7, -35, Math.toRadians(180)))

                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .lineToLinearHeading(new Pose2d(42, -35, Math.toRadians(180)))
                .strafeLeft(camera.getDesiredTagX())
                .addTemporalMarker(() ->{

                })
                .waitSeconds(1)

                .lineToLinearHeading(new Pose2d(42, -11,Math.toRadians(180)))
                .back(15)
                .build();
    }
    public static final TrajectorySequence RedAudienceRight(CenterstageMecanumDrive drivetrain,
                                                            TensorFlowM1 camera){
        return drivetrain.trajectorySequenceBuilder(RED_F2.pose)
                .lineToLinearHeading(new Pose2d(-35.3, -29, Math.toRadians(0)))

                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .strafeLeft(20)
                .splineToConstantHeading(new Vector2d(0.7, -7.8),Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(19, -18),Math.toRadians(0))
                .lineToLinearHeading(new Pose2d(42, -42.5, Math.toRadians(180)))
                .strafeLeft(camera.getDesiredTagX())
                .addTemporalMarker(() ->{

                })
                .waitSeconds(1)
                .lineToLinearHeading(new Pose2d(42, -11,Math.toRadians(180)))
                .back(15)
                .build();
    }


}
