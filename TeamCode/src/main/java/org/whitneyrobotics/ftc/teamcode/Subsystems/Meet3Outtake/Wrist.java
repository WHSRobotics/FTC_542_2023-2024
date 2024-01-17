package org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Outtake;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Wrist {
    public Servo wristL;
    public Servo wristR;

    public WristPositions currentState = WristPositions.INTAKING;

    public enum WristPositions{
        INTAKING(0.96, 0),
        OUTTAKING(0, 0.7);

        double posL;
        double posR;
        WristPositions(double posL, double posR){
            this.posL = posL;
            this.posR = posR;
        }
    }

    public Wrist (HardwareMap wristMap){
        wristL = wristMap.get(Servo.class, "wristLeft");
        wristR = wristMap.get(Servo.class, "wristRight");
    }

    public void update(){
        currentState = WristPositions.values()[(currentState.ordinal() + 1) % 2];
    }

    public void run(){
        wristL.setPosition(currentState.posL);
        wristR.setPosition(currentState.posR);
    }

}
