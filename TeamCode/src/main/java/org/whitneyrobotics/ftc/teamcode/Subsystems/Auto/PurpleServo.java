package org.whitneyrobotics.ftc.teamcode.Subsystems.Auto;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class PurpleServo {
    public Servo purpleServo;


    private PurplePositions position = PurplePositions.OPEN;

    public enum PurplePositions{
        OPEN(0.45),
        CLOSED(0.75);

        double pos;

        PurplePositions(double pos){
            this.pos = pos;
        }
    }


    public PurpleServo (HardwareMap hardwareMap){
        purpleServo = hardwareMap.get(Servo.class, "purpleServo");
    }

    public void update(){
        purpleServo.setPosition(position.pos);
    }

    public void setState(PurplePositions pos){
        this.position = pos;
    }

}
