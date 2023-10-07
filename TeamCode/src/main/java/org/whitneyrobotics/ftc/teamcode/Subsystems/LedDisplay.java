package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

public class LedDisplay {
    private RevBlinkinLedDriver ledDriver;
    public LedDisplay(RevBlinkinLedDriver ledDriver){
        this.ledDriver = ledDriver;
    }

    public void setPattern(RevBlinkinLedDriver.BlinkinPattern pattern){
        ledDriver.setPattern(pattern);
    }

    public void operate(){
    }
}
