package org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Outtake;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Wrist {
    public Servo wristL;
    public Servo wristR;

    public static double INTAKING_posL = 1.0, INTAKING_posR = 0.78, ONE = 0.64;


    public WristPositions currentState = WristPositions.INTAKING;

    public enum WristPositions{
        INTAKING(1, 0),
        OUTTAKING(0.279, 0.75);

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
