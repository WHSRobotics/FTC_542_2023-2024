package org.whitneyrobotics.ftc.teamcode.OpMode.Autonomous;

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
public class OpenCVBlue {

    private static OpenCvCamera camera;

    public int path;
    private static WebcamName webcam;

    public enum Position {
        LEFT,
        CENTER,
        RIGHT
    }

    public static void initalizeCamCV(HardwareMap hardwareMap) {
        webcam = hardwareMap.get(WebcamName.class, "Webcam 1");
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcam);
        camera.setPipeline(new Pipeline());
        camera.openCameraDevice();
        camera.startStreaming(1920, 1080);
    }

    public static void stopCamera(){
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

            Mat blueChannel = channels.get(0);

            Rect leftRegion = new Rect(150, 20, 240, 260);
            Rect centerRegion = new Rect(798, 50, 242, 245);
            Rect rightRegion = new Rect(1465, 140, 270, 270);
//            Rect leftRegion = new Rect((int) rightRegionX, (int) rightRegionY, (int) rightRegionWidth, (int) rightRegionHeight);
//            Rect centerRegion = new Rect((int) centerRegionX, (int) centerRegionY, (int) centerRegionWidth, (int) centerRegionHeight);
//            Rect rightRegion = new Rect((int) rightRegionX, (int) rightRegionY, (int) rightRegionWidth, (int) rightRegionHeight);
            Mat leftRedRegion = new Mat(blueChannel, leftRegion);
            Mat centerRedRegion = new Mat(blueChannel, centerRegion);
            Mat rightRedRegion = new Mat(blueChannel, rightRegion);
            Scalar leftMean = Core.mean(leftRedRegion);
            Scalar centerMean = Core.mean(centerRedRegion);
            Scalar rightMean = Core.mean(rightRedRegion);
            double leftIntensityValue = leftMean.val[0];
            double centerIntensityValue = centerMean.val[0];
            double rightIntensityValue = rightMean.val[0];

            leftIntensity = leftIntensityValue;
            centerIntensity = (centerIntensityValue);
            rightIntensity = rightIntensityValue;

            Imgproc.rectangle(input, leftRegion.tl(), leftRegion.br(), new Scalar(0, 255, 0), 2);
            Imgproc.rectangle(input, centerRegion.tl(), centerRegion.br(), new Scalar(0, 255, 0), 2);
            Imgproc.rectangle(input, rightRegion.tl(), rightRegion.br(), new Scalar(0, 255, 0), 2);

            double meanRedIntensity = Core.mean(blueChannel).val[0];

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

    }

}
