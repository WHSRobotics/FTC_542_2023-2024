package org.whitneyrobotics.ftc.teamcode.Constants;

import org.whitneyrobotics.ftc.teamcode.Roadrunner.drive.CenterstageMecanumDrive;
import org.whitneyrobotics.ftc.teamcode.Roadrunner.trajectorysequence.TrajectorySequence;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Auto.PurpleServo;
import org.whitneyrobotics.ftc.teamcode.Subsystems.ElbowWristImpl;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Intake;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Outtake.Elbow;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Outtake.Gate;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Outtake.Wrist;

import static org.whitneyrobotics.ftc.teamcode.Constants.FieldConstants.StartingTiles.*;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;

public class AutoPaths {


    private  static PurpleServo purpleServo;
    private  static ElbowWristImpl elbowWrist;
    private  static Gate gate;

    private static Meet3Intake intake;

    private static TrajectoryVelocityConstraint VEL;

    public static void setAutoSubsystems(PurpleServo purple, ElbowWristImpl el, Gate gte, Meet3Intake in) {
        purpleServo = purple;
        elbowWrist = el;
        gate = gte;
        intake = in;
    }

    private static final double distFromBackdrop = 49.7;



    public static final TrajectorySequence BlueBackstageLeft(CenterstageMecanumDrive drivetrain
                                                             ) {
        return drivetrain.trajectorySequenceBuilder(BLUE_A4.pose)
                .lineToLinearHeading(new Pose2d(34.2, 33.5, Math.toRadians(180)))
                .addTemporalMarker(() -> {
                    //CLAW STUFF
                    purpleServo.setState(PurpleServo.PurplePositions.CLOSED);
                })
                .waitSeconds(1)
                .lineToLinearHeading(new Pose2d(distFromBackdrop+1.5, 43.5,Math.toRadians(180)))
                .waitSeconds(3)
                .addTemporalMarker(3.4,() -> {
                    intake.stackPosition();
                    intake.update();
                })
                .waitSeconds(1)
                .addTemporalMarker(4.5,() -> {
                    elbowWrist.toggle();
                    elbowWrist.update();
                })
                .addTemporalMarker(6.8,() -> {
                    gate.update();
                    gate.run();

                })
                .waitSeconds(1)
                .addTemporalMarker(8,() -> {
                    elbowWrist.toggle();
                    elbowWrist.update();
                })
                .waitSeconds(2)
                .lineToLinearHeading(new Pose2d(distFromBackdrop+1.5, 10.3, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(58, 10.3, Math.toRadians(180)))
                .build();
    }
    public static final TrajectorySequence BlueBackstageCenter(CenterstageMecanumDrive drivetrain
                                                               ) {
        return drivetrain.trajectorySequenceBuilder(BLUE_A4.pose)
                .lineToLinearHeading(new Pose2d(23.3, 26.8, Math.toRadians(180)))
                .addTemporalMarker(() -> {
                    //CLAW STUFF
                    purpleServo.setState(PurpleServo.PurplePositions.CLOSED);
                })
                .lineToLinearHeading(new Pose2d(distFromBackdrop+1.7, 39, Math.toRadians(180)))
                .waitSeconds(3)
                .addTemporalMarker(3.4,() -> {
                    intake.stackPosition();
                    intake.update();
                })
                .waitSeconds(1)
                .addTemporalMarker(4.5,() -> {
                    elbowWrist.toggle();
                    elbowWrist.update();
                })
                .addTemporalMarker(6.8,() -> {
                    gate.update();
                    gate.run();

                })
                .waitSeconds(1)
                .addTemporalMarker(8,() -> {
                    elbowWrist.toggle();
                    elbowWrist.update();
                })
                .waitSeconds(2)
                .lineToLinearHeading(new Pose2d(distFromBackdrop+1.7, 10.3, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(58, 10.3, Math.toRadians(180)))
                .build();
    }
    public static final TrajectorySequence BlueBackstageRight(CenterstageMecanumDrive drivetrain
                                                              ) {
        return drivetrain.trajectorySequenceBuilder(BLUE_A4.pose)
                .lineToLinearHeading(new Pose2d(19, 38, Math.toRadians(180)))
                .forward(7.5)
                .addTemporalMarker(() -> {

                    //CLAW STUFF
                    purpleServo.setState(PurpleServo.PurplePositions.CLOSED);

                })
                .waitSeconds(1)
                .back(5)
                .lineToLinearHeading(new Pose2d(distFromBackdrop+1.7, 31, Math.toRadians(180)))
                .waitSeconds(3)
                .addTemporalMarker(5.4,() -> {
                    intake.stackPosition();
                    intake.update();
                })
                .waitSeconds(1)
                .addTemporalMarker(6.5,() -> {
                    elbowWrist.toggle();
                    elbowWrist.update();
                })
                .addTemporalMarker(8.8,() -> {
                    gate.update();
                    gate.run();

                })
                .waitSeconds(1)
                .addTemporalMarker(10,() -> {
                    elbowWrist.toggle();
                    elbowWrist.update();
                })
                .waitSeconds(2)
                .lineToLinearHeading(new Pose2d(distFromBackdrop, 10.3, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(58, 10.3, Math.toRadians(180)))

                .build();
    }

    public static final TrajectorySequence RedBackstageLeft(CenterstageMecanumDrive drivetrain
                                                            ){
        return drivetrain.trajectorySequenceBuilder(RED_F4.pose)
                .lineToLinearHeading(new Pose2d(19, -29, Math.toRadians(180)))
                .forward(9.5)
                .addTemporalMarker(() -> {
                    //CLAW STUFF
                    purpleServo.setState(PurpleServo.PurplePositions.CLOSED);
                })
                .waitSeconds(2)

                .lineToLinearHeading(new Pose2d(distFromBackdrop, -29.7, Math.toRadians(180)))

                .waitSeconds(3)
                .addTemporalMarker(6,() -> {
                    intake.stackPosition();
                    intake.update();
                })
                .waitSeconds(1)
                .addTemporalMarker(6.5,() -> {
                    elbowWrist.toggle();
                    elbowWrist.update();
                })
                .addTemporalMarker(8.8,() -> {
                    gate.update();
                    gate.run();
                })
                .waitSeconds(1)
                .addTemporalMarker(10,() -> {
                    elbowWrist.toggle();
                    elbowWrist.update();
                })
                .lineToLinearHeading(new Pose2d(distFromBackdrop, -10.3, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(58, -8.3, Math.toRadians(180)))
                .build();
    }
    public static final TrajectorySequence RedBackstageCenter(CenterstageMecanumDrive drivetrain
                                                              ){
        return drivetrain.trajectorySequenceBuilder(RED_F4.pose)
                .lineToLinearHeading(new Pose2d(23.3, -19.4, Math.toRadians(180)))
                .addTemporalMarker(() -> {
                    //CLAW STUFF
                    purpleServo.setState(PurpleServo.PurplePositions.CLOSED);


                })
                .lineToLinearHeading(new Pose2d(distFromBackdrop, -35, Math.toRadians(180)))
                .waitSeconds(2)
                .addTemporalMarker(3.4,() -> {
                    intake.stackPosition();
                    intake.update();
                })
                .waitSeconds(1)
                .addTemporalMarker(5,() -> {
                    elbowWrist.toggle();
                    elbowWrist.update();
                })
                .addTemporalMarker(6.8,() -> {
                    gate.update();
                    gate.run();
                })

                .addTemporalMarker(8.5,() -> {
                    elbowWrist.toggle();
                    elbowWrist.update();
                })
                .waitSeconds(3)
                .lineToLinearHeading(new Pose2d(distFromBackdrop, -10.3, Math.toRadians(180)))

                .waitSeconds(2)
                .lineToLinearHeading(new Pose2d(58, -10.3, Math.toRadians(180)))
                .build();
    }
    public static final TrajectorySequence RedBackstageRight(CenterstageMecanumDrive drivetrain){
        return drivetrain.trajectorySequenceBuilder(RED_F4.pose)
                .lineToLinearHeading(new Pose2d(32.9, -28, Math.toRadians(180)))
                .addTemporalMarker(() -> {
                    //CLAW STUFF
                    purpleServo.setState(PurpleServo.PurplePositions.CLOSED);

                })
                .lineToLinearHeading(new Pose2d(distFromBackdrop, -42.5,Math.toRadians(180)))
                .waitSeconds(3)
                .addTemporalMarker(3.4,() -> {
                    intake.stackPosition();
                    intake.update();
                })
                .waitSeconds(1)
                .addTemporalMarker(5.5,() -> {
                    elbowWrist.toggle();
                    elbowWrist.update();
                })
                .addTemporalMarker(6.8,() -> {
                    gate.update();
                    gate.run();
                })
                .waitSeconds(1)
                .addTemporalMarker(8,() -> {
                    elbowWrist.toggle();
                    elbowWrist.update();
                })
                .waitSeconds(2)
                .lineToLinearHeading(new Pose2d(distFromBackdrop, -10.3, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(58, -10.3, Math.toRadians(180)))
                .build();
    }

    public static final TrajectorySequence BlueAudienceRight(CenterstageMecanumDrive drivetrain){
        return drivetrain.trajectorySequenceBuilder(BLUE_A2.pose)

                .lineToLinearHeading(new Pose2d(-35.3, 33.5, Math.toRadians(270)))
                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .back(1.5)
                .strafeLeft(61.5)
                .waitSeconds(0.2)

                .lineToLinearHeading(new Pose2d(distFromBackdrop, 28, Math.toRadians(180)))
                .addTemporalMarker(() ->{

                })
                .waitSeconds(2)
                .lineToLinearHeading(new Pose2d(distFromBackdrop, 10.3, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(58, 10.3, Math.toRadians(180)))
                .build();
    }
    public static final TrajectorySequence BlueAudienceCenter(CenterstageMecanumDrive drivetrain){
        return drivetrain.trajectorySequenceBuilder(BLUE_A2.pose)
                .lineToLinearHeading(new Pose2d(-35.3, 29, Math.toRadians(180)))
                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .strafeRight(6)
                .back(78)
                .lineToLinearHeading(new Pose2d(distFromBackdrop, 35, Math.toRadians(180)))
                //.lineToLinearHeading(new Pose2d(44.4, 35, Math.toRadians(180)))
                .addTemporalMarker(() ->{

                })
                .waitSeconds(2)
                .lineToLinearHeading(new Pose2d(distFromBackdrop, 10.3, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(58, 10.3, Math.toRadians(180)))
                .build();
    }

    public static final TrajectorySequence BlueAudienceLeft(CenterstageMecanumDrive drivetrain){
        return drivetrain.trajectorySequenceBuilder(BLUE_A2.pose)
                .lineToLinearHeading(new Pose2d(-35.3, 29, Math.toRadians(0)))

                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .strafeRight(18.5)
//                                .splineToConstantHeading(new Vector2d(0.7, 7.8),Math.toRadians(0))
//                                .splineToConstantHeading(new Vector2d(19, 18),Math.toRadians(0))
                .forward(50)
                .lineToLinearHeading(new Pose2d(distFromBackdrop, 42.5,Math.toRadians(180)))
                .addTemporalMarker(() ->{

                })
                .waitSeconds(2)
                .lineToLinearHeading(new Pose2d(distFromBackdrop, 10.3, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(58, 10.3, Math.toRadians(180)))
                .build();
    }

    public static final TrajectorySequence RedAudienceLeft(CenterstageMecanumDrive drivetrain){
        return drivetrain.trajectorySequenceBuilder(RED_F2.pose)
                .lineToLinearHeading(new Pose2d(-52.2, -22, Math.toRadians(270)))
                .addTemporalMarker(() -> {
                    //CLAW STUFF
                    purpleServo.setState(PurpleServo.PurplePositions.CLOSED);

                })
                .waitSeconds(2)
                .waitSeconds(0.35)
                .lineToLinearHeading(new Pose2d(-36, -9, Math.toRadians(270)))
                .waitSeconds(7.5)
                .lineToLinearHeading(new Pose2d(15, -9, Math.toRadians(270)))
                .waitSeconds(0.5)
                .lineToLinearHeading(new Pose2d(34.1, -24, Math.toRadians(180)))
                .waitSeconds(2)
                .lineToLinearHeading(new Pose2d(distFromBackdrop+1.7, -27.8, Math.toRadians(180)))
                .addTemporalMarker(18,() -> {
                    intake.stackPosition();
                    intake.update();
                })
                .waitSeconds(1)
                .addTemporalMarker(20.5,() -> {
                    elbowWrist.toggle();
                    elbowWrist.update();
                })
                .addTemporalMarker(21.8,() -> {
                    gate.update();
                    gate.run();
                })
                .waitSeconds(1)
                .addTemporalMarker(22,() -> {
                    elbowWrist.toggle();
                    elbowWrist.update();
                })

                .waitSeconds(4)

                .build();
    }
    public static final TrajectorySequence RedAudienceCenter(CenterstageMecanumDrive drivetrain){
        return drivetrain.trajectorySequenceBuilder(RED_F2.pose)


                .lineToLinearHeading(new Pose2d(-50, -25.6, Math.toRadians(0)))
                .addTemporalMarker(() -> {
                    //CLAW STUFF
                    purpleServo.setState(PurpleServo.PurplePositions.CLOSED);

                })
                .waitSeconds(0.5)
                .lineToLinearHeading(new Pose2d(-36, -9, Math.toRadians(270)))
                .waitSeconds(7.5)

                .lineToLinearHeading(new Pose2d(15, -9, Math.toRadians(270)))
                .waitSeconds(0.5)
                .lineToLinearHeading(new Pose2d(34.1, -24, Math.toRadians(180)))
                .waitSeconds(2)
                .lineToLinearHeading(new Pose2d(distFromBackdrop+1.7, -30.8, Math.toRadians(180)))
                .addTemporalMarker(18,() -> {
                    intake.stackPosition();
                    intake.update();
                })
                .waitSeconds(1)
                .addTemporalMarker(20.5,() -> {
                    elbowWrist.toggle();
                    elbowWrist.update();
                })
                .addTemporalMarker(21.8,() -> {
                    gate.update();
                    gate.run();
                })
                .waitSeconds(1)
                .addTemporalMarker(22,() -> {
                    elbowWrist.toggle();
                    elbowWrist.update();
                })

                .waitSeconds(4)
                .build();
    }
    public static final TrajectorySequence RedAudienceRight(CenterstageMecanumDrive drivetrain){
        return drivetrain.trajectorySequenceBuilder(RED_F2.pose)
                .lineToLinearHeading(new Pose2d(-42, -38.8, Math.toRadians(0)))
                .lineToLinearHeading(new Pose2d(-34.3, -38.8, Math.toRadians(0)))
                .waitSeconds(0.3)
                .addTemporalMarker(() -> {
                    intake.raisedPosition();
                    //CLAW STUFF
                    purpleServo.setState(PurpleServo.PurplePositions.CLOSED);
                })
                .waitSeconds(0.35)
                .lineToLinearHeading(new Pose2d(-46.3, -38.8, Math.toRadians(0)))
                .lineToLinearHeading(new Pose2d(-36, -9, Math.toRadians(90)))
                .waitSeconds(7.5)
                .lineToLinearHeading(new Pose2d(15, -9, Math.toRadians(90)))
                .waitSeconds(0.5)
                .lineToLinearHeading(new Pose2d(34.1, -24, Math.toRadians(180)))
                .waitSeconds(2)
                .lineToLinearHeading(new Pose2d(distFromBackdrop+1.5, -35.8, Math.toRadians(180)))
                .addTemporalMarker(20,() -> {
                    intake.stackPosition();
                    intake.update();
                })
                .waitSeconds(1)
                .addTemporalMarker(22.5,() -> {
                    elbowWrist.toggle();
                    elbowWrist.update();
                })
                .addTemporalMarker(23.8,() -> {
                    gate.update();
                    gate.run();
                })
                .waitSeconds(1)
                .addTemporalMarker(24,() -> {
                    elbowWrist.toggle();
                    elbowWrist.update();
                })
                .waitSeconds(3)
                .build();
    }


     /*
        public static final TrajectorySequence buildBlueBackstage(CenterstageMecanumDrive drivetrain) {
            return drivetrain.trajectorySequenceBuilder(BLUE_A4.pose)
                    .lineToConstantHeading(new Vector2d(TILE_WIDTH.toInches(-1.5), TILE_WIDTH.toInches(-0.5)))
                    .splineToSplineHeading(
                            new Pose2d(TILE_WIDTH.toInches(-0.5), TILE_WIDTH.toInches(2.5), Math.toRadians(-90)),
                            Math.toRadians(90)
                    ).build();
        }

        public static final TrajectorySequence buildRedBackstage(CenterstageMecanumDrive drivetrain){
            return drivetrain.trajectorySequenceBuilder(RED_F4.pose)
                    .lineToConstantHeading(new Vector2d(TILE_WIDTH.toInches(1.5), TILE_WIDTH.toInches(-0.5)))
                    .splineToSplineHeading(
                            new Pose2d(TILE_WIDTH.toInches(0.5), TILE_WIDTH.toInches(2.5), Math.toRadians(-90)),
                            Math.toRadians(90)
                    ).build();
        }

        public static final TrajectorySequence buildBlueAudience(CenterstageMecanumDrive drivetrain){
            return drivetrain.trajectorySequenceBuilder(BLUE_A2.pose)
                    .lineToConstantHeading(new Vector2d(TILE_WIDTH.toInches(-2.5), TILE_WIDTH.toInches(-1.5)))
                    .lineToConstantHeading(new Vector2d(TILE_WIDTH.toInches(-2.5), TILE_WIDTH.toInches(0.5)))
                    .lineToLinearHeading(new Pose2d(TILE_WIDTH.toInches(-2.5), TILE_WIDTH.toInches(2.5), Math.toRadians(-90))
                    ).build();
        }

        public static final TrajectorySequence buildRedAudience(CenterstageMecanumDrive drivetrain){
            return drivetrain.trajectorySequenceBuilder(RED_F2.pose)
                    .lineToConstantHeading(new Vector2d(TILE_WIDTH.toInches(2.5), TILE_WIDTH.toInches(-1.5)))
                    .lineToConstantHeading(new Vector2d(TILE_WIDTH.toInches(2.5), TILE_WIDTH.toInches(0.5)))
                    .lineToLinearHeading(new Pose2d(TILE_WIDTH.toInches(2.5), TILE_WIDTH.toInches(2.5), Math.toRadians(-90))
                    ).build();
        }
        */


}





