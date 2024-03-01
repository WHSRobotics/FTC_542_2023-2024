package org.whitneyrobotics.ftc.teamcode.Libraries.JSON;

import com.acmerobotics.roadrunner.geometry.Pose2d;

public class WHSRobotData {
    public static double heading = 0.0d;
    public static double lastX = 0.0d;
    public static double lastY = 0.0d;
    public static double slidesHeight = 0.0d;
    //simulate power cycling
    public static void reset() {
        heading = 0.0d;
    }
    public static Pose2d getPose() {
        return new Pose2d(lastX, lastY, heading);
    }
}
