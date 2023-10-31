package org.whitneyrobotics.ftc.teamcode.Subsystems.Odometry;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class PixelGrabber {

    public static double RELEASE_BOTH = 1, INTAKE_ONE = 0.5, INTAKE_BOTH = 0;
    private Servo pixelGrabber;

    public PixelGrabber(HardwareMap hardwareMap){
        pixelGrabber = hardwareMap.get(Servo.class, "pixelGrabber");
        releaseBoth();
    }

    public void grabBoth(){
        pixelGrabber.setPosition(INTAKE_BOTH);
    }

    public void releaseBoth(){
        pixelGrabber.setPosition(RELEASE_BOTH);
    }

    public void grabOne(){
        pixelGrabber.setPosition(INTAKE_ONE);
    }

    public double position(){
        return pixelGrabber.getPosition();
    }

}
