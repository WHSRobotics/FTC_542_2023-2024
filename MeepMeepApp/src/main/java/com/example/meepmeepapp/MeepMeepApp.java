package com.example.meepmeepapp;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
public class MeepMeepApp {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity blueBotBackdrop1 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(11, 58.3, Math.toRadians(90)))
                                .lineToLinearHeading(new Pose2d(32.9, 28.5, Math.toRadians(180)))
                                .addDisplacementMarker(() -> {
                                    //CLAW STUFF
                                })

                                .lineToLinearHeading(new Pose2d(42, 42.5,Math.toRadians(180)))
                                .waitSeconds(1)
                                .addTemporalMarker(() ->{

                                })
                                .build()

                );

        RoadRunnerBotEntity blueBotBackdrop2 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(11, 58.3, Math.toRadians(90)))
                                .lineToLinearHeading(new Pose2d(23.3, 24, Math.toRadians(180)))
                                .addDisplacementMarker(() -> {
                                    //CLAW STUFF
                                })
                                .lineToLinearHeading(new Pose2d(42, 34.8, Math.toRadians(180)))
                                .addTemporalMarker(() ->{

                                })
                                .build()
                );
        RoadRunnerBotEntity blueBotBackdrop3 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(11, 58.3, Math.toRadians(90)))
                                .lineToLinearHeading(new Pose2d(12.5, 30.8, Math.toRadians(180)))
                                .addDisplacementMarker(() -> {
                                    //CLAW STUFF
                                })
                                .lineToLinearHeading(new Pose2d(42, 27.5, Math.toRadians(180)))
                                .addTemporalMarker(() ->{

                                })
                                .build()
                );

        RoadRunnerBotEntity redBotBackdrop1 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(12.5, -58.3, Math.toRadians(270)))
                                .lineToLinearHeading(new Pose2d(23, -42.1, Math.toRadians(90)))
                                .addDisplacementMarker(() -> {
                                    //CLAW STUFF
                                })
                                .lineToLinearHeading(new Pose2d(47.5, -41.1,Math.toRadians(180)))
                                .addTemporalMarker(() ->{

                                })
                                .build()
                );
        RoadRunnerBotEntity redBotBackdrop2 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(12.5, -58.3, Math.toRadians(270)))
                                .lineToLinearHeading(new Pose2d(12.5, -35.7, Math.toRadians(180)))
                                .addDisplacementMarker(() -> {
                                    //CLAW STUFF
                                })
                                .lineToLinearHeading(new Pose2d(47.5, -28.5, Math.toRadians(180)))
                                .addTemporalMarker(() ->{

                                })
                                .build()
                );
        RoadRunnerBotEntity redBotBackdrop3 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(11, -58.3, Math.toRadians(270)))
                                .lineToLinearHeading(new Pose2d(12.5, -35.7, Math.toRadians(90)))
                                .addDisplacementMarker(() -> {
                                    //CLAW STUFF
                                })
                                .lineToLinearHeading(new Pose2d(47.5, -34.8, Math.toRadians(180)))
                                .addTemporalMarker(() ->{

                                })
                                .build()
                );
        RoadRunnerBotEntity blueBotWall1 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-35.5, 58, Math.toRadians(90)))
                                .lineToLinearHeading(new Pose2d(-35.3, 29, Math.toRadians(180)))
                                .addDisplacementMarker(() -> {
                                    //CLAW STUFF
                                })
                                .strafeRight(6)
                                .back(10)
                                .lineToLinearHeading(new Pose2d(-23.7, 35, Math.toRadians(180)))
                                .lineToLinearHeading(new Pose2d(42, 35, Math.toRadians(180)))
                                .addTemporalMarker(() ->{

                                })
                                .build()
                );

        RoadRunnerBotEntity blueBotWall2 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-35.5, 58, Math.toRadians(90)))

                                .lineToLinearHeading(new Pose2d(-35.3, 33.5, Math.toRadians(270)))
                                .lineToLinearHeading(new Pose2d(-23.7, 35, Math.toRadians(180)))

                                .addDisplacementMarker(() -> {
                                    //CLAW STUFF
                                })
                                .lineToLinearHeading(new Pose2d(42, 35, Math.toRadians(180)))
                                .addTemporalMarker(() ->{

                                })
                                .build()
                );
        RoadRunnerBotEntity blueBotWall3 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-35.5, 58, Math.toRadians(90)))
                                .lineToLinearHeading(new Pose2d(-35.3, 29, Math.toRadians(0)))

                                .addDisplacementMarker(() -> {
                                    //CLAW STUFF
                                })
                                .strafeRight(20)
                                .splineToConstantHeading(new Vector2d(0.7, 7.8),Math.toRadians(0))
                                .splineToConstantHeading(new Vector2d(19, 18),Math.toRadians(0))
                                .lineToLinearHeading(new Pose2d(42, 42.5,Math.toRadians(180)))
                                .addTemporalMarker(() ->{

                                })
                                .build()
                );
        RoadRunnerBotEntity redBotWall1 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-35.5, -58, Math.toRadians(270)))
                                .lineToLinearHeading(new Pose2d(-46.8, -43, Math.toRadians(90)))
                                .addDisplacementMarker(() -> {
                                    //CLAW STUFF
                                })
                                .lineToLinearHeading(new Pose2d(-23.7, -35, Math.toRadians(180)))
                                .lineToLinearHeading(new Pose2d(47.5, -34.8, Math.toRadians(180)))
                                .lineToLinearHeading(new Pose2d(47.5, -28.5, Math.toRadians(180)))

                                .addTemporalMarker(() ->{

                                })
                                .build()
                );
        RoadRunnerBotEntity redBotWall2 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-35.5, -58, Math.toRadians(270)))
                                .lineToLinearHeading(new Pose2d(-35.3, -33.5, Math.toRadians(90)))
                                //.lineToLinearHeading(new Pose2d(-23.7, -35, Math.toRadians(180)))
                                
                                .addDisplacementMarker(() -> {
                                    //CLAW STUFF
                                })
                                .lineToLinearHeading(new Pose2d(47.5, -34.8, Math.toRadians(180)))
                                .addTemporalMarker(() ->{

                                })
                                .build()
                );

        RoadRunnerBotEntity redBotWall3 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-35.5, -58, Math.toRadians(270)))
                                .lineToLinearHeading(new Pose2d(-35.3, -33.5, Math.toRadians(0)))

                                .addDisplacementMarker(() -> {
                                    //CLAW STUFF
                                })
                                .splineToConstantHeading(new Vector2d(-36.4, -16.4), Math.toRadians(0))
                                .splineToConstantHeading(new Vector2d(0, -4),Math.toRadians(0))
                                .lineToLinearHeading(new Pose2d(47.5, -41.1, Math.toRadians(180)))

                                .addTemporalMarker(() ->{

                                })
                                .build()
                );
        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)




                //blue alliance backdrop
                //.addEntity(blueBotBackdrop1) //left
                //.addEntity(blueBotBackdrop2) //center
                //.addEntity(blueBotBackdrop3) //right



                //blue alliance backdrop
                //.addEntity(blueBotBackdrop1) //left
                //.addEntity(blueBotBackdrop2) //center
                //.addEntity(blueBotBackdrop3) //right




                //red alliance, backdrop
                //.addEntity(redBotBackdrop1) //right
                //.addEntity(redBotBackdrop2) //left
                //.addEntity(redBotBackdrop3) //center



                //blue alliance, wall
                .addEntity(blueBotWall1) //right

                .addEntity(blueBotWall2) //center

                .addEntity(blueBotWall3) //left


                /*
                .addEntity(redBotWall1)//left
                .addEntity(redBotWall2)//center
                .addEntity(redBotWall3)//right
                //red alliance, backdrop
                //.addEntity(redBotBackdrop1) //right
                //.addEntity(redBotBackdrop2) //left
                //.addEntity(redBotBackdrop3) //center



                //blue alliance, wall
                .addEntity(blueBotWall1) //right

                .addEntity(blueBotWall2) //center

                .addEntity(blueBotWall3) //left


                /*
                .addEntity(redBotWall1)//left
                .addEntity(redBotWall2)//center
                .addEntity(redBotWall3)//right



                */
                .start();
    }
}