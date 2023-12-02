package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class PixelGrabber {

    public String state = "";
    public static double RELEASE_BOTH = 0.72, INTAKE_ONE = 0.83, INTAKE_BOTH = 0.96;
    private Servo pixelGrabber;

    public PixelGrabber(HardwareMap hardwareMap){
        pixelGrabber = hardwareMap.get(Servo.class, "pixelGrabber");
        releaseBoth();
    }

    public void grabBoth(){
        pixelGrabber.setPosition(INTAKE_BOTH);
        state = "INTAKE BOTH";
    }

    public void releaseBoth(){
        pixelGrabber.setPosition(RELEASE_BOTH);
        state = "RELEASE BOTH";
    }

    public void grabOne(){
        pixelGrabber.setPosition(INTAKE_ONE);
        state = "GRAB ONE";
    }

    public double position(){
        return pixelGrabber.getPosition();
    }

}
