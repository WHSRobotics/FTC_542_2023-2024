package org.whitneyrobotics.ftc.teamcode.Constants;

import org.whitneyrobotics.ftc.teamcode.Roadrunner.drive.CenterstageMecanumDrive;
import org.whitneyrobotics.ftc.teamcode.Roadrunner.trajectorysequence.TrajectorySequence;
import static org.whitneyrobotics.ftc.teamcode.Constants.FieldConstants.StartingTiles.*;
import static org.whitneyrobotics.ftc.teamcode.Libraries.Utilities.UnitConversion.DistanceUnit.TILE_WIDTH;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

public class AutoPaths {
    public static final TrajectorySequence buildBlueBackstage(CenterstageMecanumDrive drivetrain) {
        return drivetrain.trajectorySequenceBuilder(BLUE_A4.pose)
                .lineToConstantHeading(new Vector2d(TILE_WIDTH.toInches(0.5), TILE_WIDTH.toInches(1.5)))
                .splineToSplineHeading(
                        new Pose2d(TILE_WIDTH.toInches(2.5), TILE_WIDTH.toInches(0.5), Math.PI),
                        0
                ).build();
    }

    public static final TrajectorySequence buildRedBackstage(CenterstageMecanumDrive drivetrain){
        return drivetrain.trajectorySequenceBuilder(RED_F4.pose)
                .lineToConstantHeading(new Vector2d(TILE_WIDTH.toInches(0.5), TILE_WIDTH.toInches(-1.5)))
                .splineToSplineHeading(
                        new Pose2d(TILE_WIDTH.toInches(2.5), TILE_WIDTH.toInches(-0.5), Math.PI),
                        0
                ).build();
    }

    public static final TrajectorySequence buildBlueAudience(CenterstageMecanumDrive drivetrain){
        return drivetrain.trajectorySequenceBuilder(BLUE_A2.pose)
                .lineToConstantHeading(new Vector2d(TILE_WIDTH.toInches(-1.5), TILE_WIDTH.toInches(2.5)))
                .lineToConstantHeading(new Vector2d(TILE_WIDTH.toInches(0.5), TILE_WIDTH.toInches(2.5)))
                .lineToLinearHeading(new Pose2d(TILE_WIDTH.toInches(2), TILE_WIDTH.toInches(2), Math.PI)
                ).build();
    }

    public static final TrajectorySequence buildRedAudience(CenterstageMecanumDrive drivetrain){
        return drivetrain.trajectorySequenceBuilder(RED_F2.pose)
                .lineToConstantHeading(new Vector2d(TILE_WIDTH.toInches(-1.5), TILE_WIDTH.toInches(-2.5)))
                .lineToConstantHeading(new Vector2d(TILE_WIDTH.toInches(0.5), TILE_WIDTH.toInches(-2.5)))
                .lineToLinearHeading(new Pose2d(TILE_WIDTH.toInches(2), TILE_WIDTH.toInches(-2), Math.PI)
                ).build();
    }

}
