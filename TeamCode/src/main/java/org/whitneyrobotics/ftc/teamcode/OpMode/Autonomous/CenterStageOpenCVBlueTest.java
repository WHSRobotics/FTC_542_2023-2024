package org.whitneyrobotics.ftc.teamcode.OpMode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

@TeleOp(name = "CenterStage OpenCV Blue", group = "Concept")
public class CenterStageOpenCVBlueTest extends LinearOpMode {

    private OpenCvCamera camera;
    public int path;

    @Override
    public void runOpMode() {
        HardwareMap hardwareMap = this.hardwareMap;
        WebcamName webcam = hardwareMap.get(WebcamName.class, "Webcam 1");

        camera = OpenCvCameraFactory.getInstance().createWebcam(webcam);
        camera.setPipeline(new Pipeline());
        camera.openCameraDevice();
        camera.startStreaming(1280, 720);

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Position: ", Pipeline.getPosition());
            telemetry.addData("Path: ", path);
            telemetry.addData("Left Intensity: ", Pipeline.getLeftIntensity());
            telemetry.addData("Right Intensity: ", (Pipeline.getRightIntensity()) - 22);
            telemetry.addData("Dynamic Threshold: ", Pipeline.getDynamicThreshold());
            telemetry.update();
            path = Pipeline.convertEnumToInteger();
        }

        camera.stopStreaming();
    }

    static class Pipeline extends OpenCvPipeline {
        private static Position position = Position.RIGHT;
        private static double leftIntensity = 0.0;
        private static double rightIntensity = 0.0;
        private static double dynamicThreshold = 0.0;

        @Override
        public Mat processFrame(Mat input) {
            List<Mat> channels = new ArrayList<>();
            Core.flip(input.t(), input, 2);
            Core.split(input, channels);

            Mat blueChannel = channels.get(0);

            Rect leftRegion = new Rect(input.width() / 4, 45, 270, 260);
            Rect rightRegion = new Rect(950, 0, 270, 270);
            Mat leftBlueRegion = new Mat(blueChannel, leftRegion);
            Mat rightBlueRegion = new Mat(blueChannel, rightRegion);
            Scalar leftMean = Core.mean(leftBlueRegion);
            Scalar rightMean = Core.mean(rightBlueRegion);
            double leftIntensityValue = leftMean.val[0];
            double rightIntensityValue = rightMean.val[0];

            leftIntensity = leftIntensityValue;
            rightIntensity = rightIntensityValue;

            Imgproc.rectangle(input, leftRegion.tl(), leftRegion.br(), new Scalar(0, 255, 0), 2);
            Imgproc.rectangle(input, rightRegion.tl(), rightRegion.br(), new Scalar(0, 255, 0), 2);

            double meanBlueIntensity = Core.mean(blueChannel).val[0];

            dynamicThreshold = meanBlueIntensity * 1; // ADJUST THIS

            // rightIntensity is reduced by 22 to compensate for the fact that it is overpowered because it can see more of the spike mark than left can
            if (leftIntensity < dynamicThreshold && (rightIntensity - 22) < dynamicThreshold) {
                position = Position.RIGHT;
            } else if (leftIntensity < rightIntensity) {
                position = Position.LEFT;
            } else if (rightIntensity < leftIntensity) {
                position = Position.CENTER;
            }

            return input;
        }

        public static Position getPosition() {
            return position;
        }

        public static double getLeftIntensity() {
            return leftIntensity;
        }

        public static double getRightIntensity() {
            return rightIntensity;
        }

        public static double getDynamicThreshold() {
            return dynamicThreshold;
        }

        public static int convertEnumToInteger() {
            switch (position) {
                case LEFT:
                    return 1;
                case CENTER:
                    return 2;
                default:
                    return 3;
            }
        }

        public enum Position {
            LEFT,
            CENTER,
            RIGHT
        }
    }
}
