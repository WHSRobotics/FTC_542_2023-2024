package org.whitneyrobotics.ftc.teamcode.Constants;

import org.whitneyrobotics.ftc.teamcode.Roadrunner.drive.CenterstageMecanumDrive;
import org.whitneyrobotics.ftc.teamcode.Roadrunner.trajectorysequence.TrajectorySequence;
import static org.whitneyrobotics.ftc.teamcode.Constants.FieldConstants.StartingTiles.*;
import static org.whitneyrobotics.ftc.teamcode.Libraries.Utilities.UnitConversion.DistanceUnit.TILE_WIDTH;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

public class AutoPaths {
    public static final TrajectorySequence BlueBackstageLeft(CenterstageMecanumDrive drivetrain) {
        return drivetrain.trajectorySequenceBuilder(BLUE_A4.pose)
                .lineToLinearHeading(new Pose2d(23, 42.1, Math.toRadians(270)))
                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })

                .lineToLinearHeading(new Pose2d(47.5, 41.1,Math.toRadians(180)))
                .waitSeconds(1)
                .addTemporalMarker(() ->{

                })
                .build();
    }
    public static final TrajectorySequence BlueBackstageCenter(CenterstageMecanumDrive drivetrain) {
        return drivetrain.trajectorySequenceBuilder(BLUE_A4.pose)
                .lineToLinearHeading(new Pose2d(12.5, 35.7, Math.toRadians(270)))
                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .lineToLinearHeading(new Pose2d(47.5, 34.8, Math.toRadians(180)))
                .addTemporalMarker(() ->{

                })
                .build();
    }
    public static final TrajectorySequence BlueBackstageRight(CenterstageMecanumDrive drivetrain) {
        return drivetrain.trajectorySequenceBuilder(BLUE_A4.pose)
                .lineToLinearHeading(new Pose2d(12.5, 35.7, Math.toRadians(180)))
                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .lineToLinearHeading(new Pose2d(47.5, 28.5, Math.toRadians(180)))
                .addTemporalMarker(() ->{

                })
                .build();
    }

    public static final TrajectorySequence RedBackstageLeft(CenterstageMecanumDrive drivetrain){
        return drivetrain.trajectorySequenceBuilder(RED_F4.pose)
                .lineToLinearHeading(new Pose2d(12.5, -35.7, Math.toRadians(180)))
                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .lineToLinearHeading(new Pose2d(47.5, -28.5, Math.toRadians(180)))
                .addTemporalMarker(() ->{

                })
                .build();
    }
    public static final TrajectorySequence RedBackstageCenter(CenterstageMecanumDrive drivetrain){
        return drivetrain.trajectorySequenceBuilder(RED_F4.pose)
                .lineToLinearHeading(new Pose2d(12.5, -35.7, Math.toRadians(90)))
                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .lineToLinearHeading(new Pose2d(47.5, -34.8, Math.toRadians(180)))
                .addTemporalMarker(() ->{

                })
                .build();
    }
    public static final TrajectorySequence RedBackstageRight(CenterstageMecanumDrive drivetrain){
        return drivetrain.trajectorySequenceBuilder(RED_F4.pose)
                .lineToLinearHeading(new Pose2d(23, -42.1, Math.toRadians(90)))
                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .lineToLinearHeading(new Pose2d(47.5, -41.1,Math.toRadians(180)))
                .addTemporalMarker(() ->{

                })
                .build();
    }

    public static final TrajectorySequence BlueAudienceRight(CenterstageMecanumDrive drivetrain){
        return drivetrain.trajectorySequenceBuilder(BLUE_A2.pose)
                .lineToLinearHeading(new Pose2d(-46.8, 43, Math.toRadians(270)))
                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .lineToLinearHeading(new Pose2d(-23.7, 35, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(47.5, 34.8, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(47.5, 28.5, Math.toRadians(180)))
                .addTemporalMarker(() ->{

                })
                .build();
    }
    public static final TrajectorySequence BlueAudienceCenter(CenterstageMecanumDrive drivetrain){
        return drivetrain.trajectorySequenceBuilder(BLUE_A2.pose)
                .lineToLinearHeading(new Pose2d(-35.3, 33.5, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(-23.7, 35, Math.toRadians(180)))

                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .lineToLinearHeading(new Pose2d(47.5, 34.8, Math.toRadians(180)))
                .addTemporalMarker(() ->{

                })
                .build();
    }

    public static final TrajectorySequence BlueAudienceLeft(CenterstageMecanumDrive drivetrain){
        return drivetrain.trajectorySequenceBuilder(BLUE_A2.pose)
                .lineToLinearHeading(new Pose2d(-35.3, 33.5, Math.toRadians(0)))

                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .splineToConstantHeading(new Vector2d(-36.4, 16.4), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(0, 4),Math.toRadians(0))
                .lineToLinearHeading(new Pose2d(47.5, 41.1,Math.toRadians(180)))
                .addTemporalMarker(() ->{

                })
                .build();
    }

    public static final TrajectorySequence RedAudienceLeft(CenterstageMecanumDrive drivetrain){
        return drivetrain.trajectorySequenceBuilder(RED_F2.pose)
                .lineToLinearHeading(new Pose2d(-46.8, -43, Math.toRadians(90)))
                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .lineToLinearHeading(new Pose2d(-23.7, -35, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(47.5, -34.8, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(47.5, -28.5, Math.toRadians(180)))

                .addTemporalMarker(() ->{

                })
                .build();
    }
    public static final TrajectorySequence RedAudienceCenter(CenterstageMecanumDrive drivetrain){
        return drivetrain.trajectorySequenceBuilder(RED_F2.pose)
                .lineToLinearHeading(new Pose2d(-35.3, -33.5, Math.toRadians(90)))
                .lineToLinearHeading(new Pose2d(-23.7, -35, Math.toRadians(180)))

                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .lineToLinearHeading(new Pose2d(47.5, -34.8, Math.toRadians(180)))
                .addTemporalMarker(() ->{

                })
                .build();
    }
    public static final TrajectorySequence RedAudienceRight(CenterstageMecanumDrive drivetrain){
        return drivetrain.trajectorySequenceBuilder(RED_F2.pose)
                .lineToLinearHeading(new Pose2d(-35.3, -33.5, Math.toRadians(0)))

                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .splineToConstantHeading(new Vector2d(-36.4, -16.4), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(0, -4),Math.toRadians(0))
                .lineToLinearHeading(new Pose2d(47.5, -41.1, Math.toRadians(180)))

                .addTemporalMarker(() ->{

                })
                .build();
    }

}
