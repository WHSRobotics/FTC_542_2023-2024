package org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Outtake;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Elbow {
    public Servo elbowL;
    public Servo elbowR;

    public ElbowPositions currentState = ElbowPositions.PARALLEL;

    public enum ElbowPositions{
        PARALLEL(0.82, 0.04),
        ANGLED(0.28, 0.57);

        double posL;
        double posR;
        ElbowPositions(double posL, double posR){
            this.posL = posL;
            this.posR = posR;
        }
    }

    public Elbow (HardwareMap elbowMap){
        elbowL = elbowMap.get(Servo.class, "elbowLeft");
        elbowR = elbowMap.get(Servo.class, "elbowRight");
    }

    public void update(){
        currentState = ElbowPositions.values()[(currentState.ordinal() + 1) % 2];
    }

    public void run(){
        elbowL.setPosition(currentState.posL);
        elbowR.setPosition(currentState.posR);
    }
}
