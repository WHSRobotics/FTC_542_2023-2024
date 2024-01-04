package org.whitneyrobotics.ftc.teamcode.Constants;

import org.whitneyrobotics.ftc.teamcode.Roadrunner.drive.CenterstageMecanumDrive;
import org.whitneyrobotics.ftc.teamcode.Roadrunner.trajectorysequence.TrajectorySequence;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Intake;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Outtake.Elbow;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Outtake.Gate;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Outtake.Wrist;

import static org.whitneyrobotics.ftc.teamcode.Constants.FieldConstants.StartingTiles.*;

import com.acmerobotics.roadrunner.geometry.Pose2d;

public class AutoPaths {


    private  static Meet3Intake intake;
    private  static Elbow   elbow;
    private  static Gate gate;
    private static Wrist wrist;

    public static void setAutoSubsystems(Meet3Intake in, Elbow el, Gate gte, Wrist wri) {
        intake = in;
        elbow = el;
        gate = gte;
        wrist = wri;
    }

    private static final double distFromBackdrop = 52.8;



    public static final TrajectorySequence BlueBackstageLeft(CenterstageMecanumDrive drivetrain
                                                             ) {
        return drivetrain.trajectorySequenceBuilder(BLUE_A4.pose)
                .lineToLinearHeading(new Pose2d(32.9, 28.5, Math.toRadians(180)))
                .addDisplacementMarker(() -> {
                    //CLAW STUFF

                })

                .lineToLinearHeading(new Pose2d(distFromBackdrop, 42.5,Math.toRadians(180)))
                .addTemporalMarker(() ->{

                })

                .build();
    }
    public static final TrajectorySequence BlueBackstageCenter(CenterstageMecanumDrive drivetrain
                                                               ) {
        return drivetrain.trajectorySequenceBuilder(BLUE_A4.pose)
                .lineToLinearHeading(new Pose2d(23.3, 24, Math.toRadians(180)))
                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .lineToLinearHeading(new Pose2d(distFromBackdrop, 35, Math.toRadians(180)))
                .addTemporalMarker(() ->{

                })
                .build();
    }
    public static final TrajectorySequence BlueBackstageRight(CenterstageMecanumDrive drivetrain
                                                              ) {
        return drivetrain.trajectorySequenceBuilder(BLUE_A4.pose)
                .lineToLinearHeading(new Pose2d(12.5, 30.8, Math.toRadians(180)))
                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .lineToLinearHeading(new Pose2d(distFromBackdrop, 28, Math.toRadians(180)))
                .addTemporalMarker(() ->{

                })
                .build();
    }

    public static final TrajectorySequence RedBackstageLeft(CenterstageMecanumDrive drivetrain
                                                            ){
        return drivetrain.trajectorySequenceBuilder(RED_F4.pose)
                .lineToLinearHeading(new Pose2d(12.5, -28.5, Math.toRadians(180)))
                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .lineToLinearHeading(new Pose2d(distFromBackdrop, -27.5, Math.toRadians(180)))
                .addTemporalMarker(() ->{

                })
                .build();
    }
    public static final TrajectorySequence RedBackstageCenter(CenterstageMecanumDrive drivetrain
                                                              ){
        return drivetrain.trajectorySequenceBuilder(RED_F4.pose)
                .lineToLinearHeading(new Pose2d(23.3, -24, Math.toRadians(180)))
                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .lineToLinearHeading(new Pose2d(distFromBackdrop, -35, Math.toRadians(180)))
                .addTemporalMarker(() ->{

                })
                .build();
    }
    public static final TrajectorySequence RedBackstageRight(CenterstageMecanumDrive drivetrain){
        return drivetrain.trajectorySequenceBuilder(RED_F4.pose)
                .lineToLinearHeading(new Pose2d(32.9, -28.5, Math.toRadians(180)))
                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .lineToLinearHeading(new Pose2d(distFromBackdrop, -42.5,Math.toRadians(180)))
                .addTemporalMarker(() ->{

                })
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
                .build();
    }

    public static final TrajectorySequence RedAudienceLeft(CenterstageMecanumDrive drivetrain){
        return drivetrain.trajectorySequenceBuilder(RED_F2.pose)
                .lineToLinearHeading(new Pose2d(-35.3, -29, Math.toRadians(180)))
                .addTemporalMarker(1.5,() -> {
                    intake.onePosition();
                    intake.setRPM(-125);


                })

                .addTemporalMarker(3,() -> {
                    intake.stackPosition();
                    intake.setRPM(0);

                })
                .waitSeconds(2)
                .strafeLeft(6)
                .lineToLinearHeading(new Pose2d(distFromBackdrop, -34.8, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(distFromBackdrop, -29.7, Math.toRadians(180)))

                .addTemporalMarker(6.4,() ->{
                    elbow.update();
                    elbow.run();

                })
                .addTemporalMarker(6.8,() ->{
                    //switches from intaking to outtaking
                    wrist.update();
                    wrist.run();

                })
                .addTemporalMarker(7.4,() ->{
                    //switches from intaking to outtaking
                    gate.run();

                })
                .addTemporalMarker(7.8,() ->{
                    //switches from intaking to outtaking
                    wrist.update();
                    elbow.update();
                    wrist.run();

                })
                .addTemporalMarker(8.4,() ->{
                    //switches from intaking to outtaking
                    elbow.run();

                })

                .waitSeconds(5)
                .build();
    }
    public static final TrajectorySequence RedAudienceCenter(CenterstageMecanumDrive drivetrain){
        return drivetrain.trajectorySequenceBuilder(RED_F2.pose)
                .lineToLinearHeading(new Pose2d(-35.3, -40.4, Math.toRadians(270)))
                .waitSeconds(0.2)
                .lineToLinearHeading(new Pose2d(-35.3, -33.5, Math.toRadians(90)))

                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .back(1.5)
                .strafeRight(70)
                .lineToLinearHeading(new Pose2d(distFromBackdrop, -35, Math.toRadians(180)))
                .addTemporalMarker(() ->{

                })
                .build();
    }
    public static final TrajectorySequence RedAudienceRight(CenterstageMecanumDrive drivetrain){
        return drivetrain.trajectorySequenceBuilder(RED_F2.pose)
                .lineToLinearHeading(new Pose2d(-35.3, -29, Math.toRadians(0)))

                .addDisplacementMarker(() -> {
                    //CLAW STUFF
                })
                .strafeLeft(20)
                .forward(61.5)
                .waitSeconds(0.2)


                .lineToLinearHeading(new Pose2d(distFromBackdrop, -42.5, Math.toRadians(180)))

                .addTemporalMarker(() ->{

                })
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





