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
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

//All the telemetry is just for testing, it can be removed later and ignored for now. Only the int variable "path" matters as it returns the correct autopath based on the randomized custom team prop's placement.
//@TeleOp(name = "TensorFlow M1 Test", group = "Tests")
public class TensorFlowM1 extends LinearOpMode {
    private static final boolean USE_WEBCAM = true;
    private static final String TFOD_MODEL_ASSET = "CenterStage.tflite";
    private static final String[] LABELS = {
            "Pixel",
    };
    private TfodProcessor tfod;
    private VisionPortal visionPortal;

    @Override
    public void runOpMode() {
        initTfod();
        waitForStart();
        if (opModeIsActive()) {

        }
        visionPortal.close();
    }
    public void initTfod() {

        tfod = new TfodProcessor.Builder()

                .build();
        VisionPortal.Builder builder = new VisionPortal.Builder();
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }
        builder.addProcessor(tfod);

        visionPortal = builder.build();

    }
    public int path() {
        for (Recognition recognition : tfod.getRecognitions()) {
            if (recognition.getLabel().equals("Pixel")) {
                double x = (recognition.getLeft() + recognition.getRight()) / 2;
                if (x < 200) {
                    //left
                    return 1;
                } else if (x > 200 && x < 400) {
                    //center
                    return 2;
                } else if (x > 400) {
                    //right
                    return 3;
                }
            }
        }
        return -1;
    }

    //Testing here
    /*
    private void telemetryTfod() {

        List<Recognition> currentRecognitions = tfod.getRecognitions();
        telemetry.addData("# Objects Detected", currentRecognitions.size());
        for (Recognition recognition : currentRecognitions) {
            double x = (recognition.getLeft() + recognition.getRight()) / 2 ;
            double y = (recognition.getTop()  + recognition.getBottom()) / 2 ;

            telemetry.addData("X Pos", x);
            telemetry.addData("Y Pos", y);
        }

    }
    */
}