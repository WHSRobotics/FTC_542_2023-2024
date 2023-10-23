package org.whitneyrobotics.ftc.teamcode.visionImpl;

import android.util.Size;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

public class AprilTagScanner2023 {
    private final AprilTagProcessor tagProcessor;
    private final VisionPortal visionPortal;
    private double lastDistance;

    public AprilTagScanner2023 (String webcam, HardwareMap hardwareMap){
        lastDistance = -1;
        tagProcessor = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .build();
        visionPortal = new VisionPortal.Builder()
                .addProcessor(tagProcessor)
                .setCamera(hardwareMap.get(WebcamName.class, webcam))
                .setCameraResolution(new Size(800, 448))
                .build();
    }

    public Double getLastDistance(){return lastDistance;}

    public Double gaugeDistance(){
        AprilTagDetection tag;
        if (tagProcessor.getDetections().size() > 0) {
            tag = tagProcessor.getDetections().get(0);
            lastDistance = tag.ftcPose.y;
            return tag.ftcPose.y;
        }
        lastDistance = -1;
        return -1.0;
    }

}
