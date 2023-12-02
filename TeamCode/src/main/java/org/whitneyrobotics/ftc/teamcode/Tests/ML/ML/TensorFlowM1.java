/* Copyright (c) 2019 FIRST. All rights reserved.
   * Modified extensively by Whitney Robotics 2023 for use in the 2023-2024 FIRST FTC CenterStage Season.
 */

package org.whitneyrobotics.ftc.teamcode.Tests.ML.ML;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

//All the telemetry is just for testing, it can be removed later and ignored for now. Only the int variable "path" matters as it returns the correct autopath based on the randomized custom team prop's placement.
@TeleOp(name = "TensorFlow M1 Test", group = "Tests")
public class TensorFlowM1 extends LinearOpMode {
    private static final boolean USE_WEBCAM = true;
    private static final String TFOD_MODEL_ASSET = "CenterStage.tflite";
    private static final String[] LABELS = {
            "Pixel",
    };
    private TfodProcessor tfod;
    private AprilTagProcessor tags;

    private AprilTagDetection desiredTag;
    private VisionPortal visionPortal;

    private  List<AprilTagDetection> currentDetections;

    private double pathNum;
    @Override
    public void runOpMode() {
        initTfod();
        waitForStart();
        if (opModeIsActive()) {
            telemetryTfod();
        }
        visionPortal.close();
    }
    public void initTfod() {

        tfod = new TfodProcessor.Builder().build();

        tags = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .build();


        VisionPortal.Builder builder = new VisionPortal.Builder();
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }
        builder.addProcessor(tfod);
        builder.addProcessor(tags);


        visionPortal = builder.build();

    }
    public int path() {
        for (Recognition recognition : tfod.getRecognitions()) {
            if (recognition.getLabel().equals("Pixel")) {
                pathNum = (recognition.getLeft() + recognition.getRight()) / 2;
                if (pathNum < 200) {
                    //left
                    return 1;
                } else if (pathNum > 200 && pathNum < 400) {
                    //center
                    return 2;
                } else if (pathNum > 400) {
                    //right
                    return 3;
                }
            }
        }
        return -1;
    }

    public void updateAprilTagDetections(){
        currentDetections = tags.getDetections();

    }
    public double getDesiredTagX(){
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                if (detection.id == pathNum){
                    desiredTag = detection;
                    return desiredTag.ftcPose.x + 5.45;
                }
            }
        }
        return 0;
    }
    //Testing here

    private void telemetryTfod() {

        List<Recognition> currentRecognitions = tfod.getRecognitions();
        currentDetections = tags.getDetections();
        telemetry.addData("# Objects Detected", currentRecognitions.size());
        for (Recognition recognition : currentRecognitions) {
            double x = (recognition.getLeft() + recognition.getRight()) / 2 ;
            double y = (recognition.getTop()  + recognition.getBottom()) / 2 ;

            telemetry.addData("X Pos", x);
            telemetry.addData("Y Pos", y);
        }
//        for (AprilTagDetection detection : currentDetections) {
//            if (detection.metadata != null) {
//                if (detection.id == pathNum){
//                    desiredTag = detection;
//                    telemetry.addData("Offset:" , desiredTag.ftcPose.x + 5.45);
//                }
//            }
//        }
    }
}