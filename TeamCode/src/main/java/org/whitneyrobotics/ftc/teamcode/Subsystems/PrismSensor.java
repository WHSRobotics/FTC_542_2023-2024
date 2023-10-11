package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PWMOutput;
import com.qualcomm.robotcore.hardware.PWMOutputImplEx;
import com.qualcomm.robotcore.hardware.Servo;

public class PrismSensor {
    DigitalChannel bottom, top;
    Servo bottomLED, topLED;
    PWMOutputImplEx bottomLEDImpl, topLEDImpl;
    public PrismSensor(HardwareMap hardwareMap){
        bottom = hardwareMap.digitalChannel.get("bottom");
        top = hardwareMap.digitalChannel.get("top");
        bottomLED = hardwareMap.get(Servo.class, "bottomLED");
        topLED = hardwareMap.get(Servo.class, "topLED");
    }

    public void detect(){
        bottomLED.setPosition(0.5);
    }

}
