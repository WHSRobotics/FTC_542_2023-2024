package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class PrismSensor {
    DigitalChannel bottom, top;
    public PrismSensor(HardwareMap hardwareMap){
        bottom = hardwareMap.digitalChannel.get("bottom");
        top = hardwareMap.digitalChannel.get("top");
    }

}
