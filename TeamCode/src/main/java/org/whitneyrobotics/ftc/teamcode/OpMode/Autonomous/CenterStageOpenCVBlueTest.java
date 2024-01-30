package org.whitneyrobotics.ftc.teamcode.OpMode.Autonomous;

import com.acmerobotics.dashboard.config.Config;
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
@Config
@TeleOp(name = "CenterStage OpenCV Blue", group = "Concept")
public class CenterStageOpenCVBlueTest extends LinearOpMode {

    private OpenCvCamera camera;
    public int path;
    public static double potentiometer = 0.7;
    public static double rightRegionX = 0.0;
    public static double rightRegionY = 0.0;
    public static double rightRegionWidth = 0.0;
    public static double rightRegionHeight = 0.0;
    public static double centerRegionX = 0.0;
    public static double centerRegionY = 0.0;
    public static double centerRegionWidth = 0.0;
    public static double centerRegionHeight = 0.0;
    public static double leftRegionX = 0.0;
    public static double leftRegionY = 0.0;
    public static double leftRegionWidth = 0.0;
    public static double leftRegionHeight = 0.0;


    @Override
    public void runOpMode() {
        HardwareMap hardwareMap = this.hardwareMap;
        WebcamName webcam = hardwareMap.get(WebcamName.class, "Webcam 1");

        camera = OpenCvCameraFactory.getInstance().createWebcam(webcam);
        camera.setPipeline(new Pipeline());
        camera.openCameraDevice();
        camera.startStreaming(800, 448);

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Position: ", Pipeline.getPosition());
            telemetry.addData("Path: ", path);
            telemetry.addData("Left Intensity: ", Pipeline.getLeftIntensity());
            telemetry.addData("Right Intensity: ", Pipeline.getRightIntensity());
            telemetry.addData("Center Intensity: ", Pipeline.getCenterIntensity());
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
        private static double centerIntensity = 0.0;
        private static double dynamicThreshold = 0.0;

        @Override
        public Mat processFrame(Mat input) {
            List<Mat> channels = new ArrayList<>();
            Core.flip(input.t(), input, 2);
            Core.split(input, channels);

            Mat redChannel = channels.get(0);

            Rect leftRegion = new Rect(50,1,120,115);
            Rect centerRegion = new Rect(370,1,75,70);
            Rect rightRegion = new Rect(610,1,170,120);
//            Rect leftRegion = new Rect((int) rightRegionX, (int) rightRegionY, (int) rightRegionWidth, (int) rightRegionHeight);
//            Rect centerRegion = new Rect((int) centerRegionX, (int) centerRegionY, (int) centerRegionWidth, (int) centerRegionHeight);
//            Rect rightRegion = new Rect((int) rightRegionX, (int) rightRegionY, (int) rightRegionWidth, (int) rightRegionHeight);
            Mat leftRedRegion = new Mat(redChannel, leftRegion);
            Mat centerRedRegion = new Mat(redChannel, centerRegion);
            Mat rightRedRegion = new Mat(redChannel, rightRegion);
            Scalar leftMean = Core.mean(leftRedRegion);
            Scalar centerMean = Core.mean(centerRedRegion);
            Scalar rightMean = Core.mean(rightRedRegion);
            double leftIntensityValue = leftMean.val[0];
            double centerIntensityValue = centerMean.val[0];
            double rightIntensityValue = rightMean.val[0];

            leftIntensity = leftIntensityValue;
            centerIntensity = centerIntensityValue;
            rightIntensity = rightIntensityValue;

            Imgproc.rectangle(input, leftRegion.tl(), leftRegion.br(), new Scalar(0, 255, 0), 2);
            Imgproc.rectangle(input, centerRegion.tl(), centerRegion.br(), new Scalar(0, 255, 0), 2);
            Imgproc.rectangle(input, rightRegion.tl(), rightRegion.br(), new Scalar(0, 255, 0), 2);

            double meanRedIntensity = Core.mean(redChannel).val[0];

            dynamicThreshold = meanRedIntensity * potentiometer; // ADJUST THIS

            //rightIntensity is reduced by 22 to compensate for the fact that it is overpowered because it can see more of the Spike Mark than left can

            if (rightIntensity < leftIntensity && rightIntensity < centerIntensity) {
                position = Position.RIGHT;
            }else if (leftIntensity < rightIntensity && leftIntensity < centerIntensity) {
                position = Position.LEFT;
            } else {
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
        public static double getCenterIntensity() {
            return centerIntensity;
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