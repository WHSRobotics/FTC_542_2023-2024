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
public class OpenCVRed {

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
        camera.startStreaming(1280, 720);
    }

    public static void stopCamera(){
        camera.stopStreaming();
    }
    static class Pipeline extends OpenCvPipeline {
        private static CenterStageOpenCVRedTest.Pipeline.Position position = CenterStageOpenCVRedTest.Pipeline.Position.RIGHT;
        private static double leftIntensity = 0.0;
        private static double rightIntensity = 0.0;
        private static double dynamicThreshold = 0.0;

        @Override
        public Mat processFrame(Mat input) {
            List<Mat> channels = new ArrayList<>();
            Core.flip(input.t(), input, 2);
            Core.split(input, channels);

            Mat redChannel = channels.get(2);

            Rect leftRegion = new Rect(input.width() / 4, 45, 270, 260);
            Rect rightRegion = new Rect(950, 0, 270, 270);
            Mat leftRedRegion = new Mat(redChannel, leftRegion);
            Mat rightRedRegion = new Mat(redChannel, rightRegion);
            Scalar leftMean = Core.mean(leftRedRegion);
            Scalar rightMean = Core.mean(rightRedRegion);
            double leftIntensityValue = leftMean.val[0];
            double rightIntensityValue = rightMean.val[0];

            leftIntensity = leftIntensityValue;
            rightIntensity = rightIntensityValue;

            Imgproc.rectangle(input, leftRegion.tl(), leftRegion.br(), new Scalar(0, 255, 0), 2);
            Imgproc.rectangle(input, rightRegion.tl(), rightRegion.br(), new Scalar(0, 255, 0), 2);

            double meanRedIntensity = Core.mean(redChannel).val[0];

            dynamicThreshold = meanRedIntensity * 0.5; // ADJUST THIS

            //rightIntensity is reduced by 22 to compensate for the fact that it is overpowered because it can see more of the Spike Mark than left can
            if (leftIntensity < dynamicThreshold && (rightIntensity - 22) < dynamicThreshold) {
                position = CenterStageOpenCVRedTest.Pipeline.Position.RIGHT;
            } else if (leftIntensity < rightIntensity) {
                position = CenterStageOpenCVRedTest.Pipeline.Position.LEFT;
            } else if (rightIntensity < leftIntensity) {
                position = CenterStageOpenCVRedTest.Pipeline.Position.CENTER;
            }

            return input;
        }

        public static CenterStageOpenCVRedTest.Pipeline.Position getPosition() {
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
