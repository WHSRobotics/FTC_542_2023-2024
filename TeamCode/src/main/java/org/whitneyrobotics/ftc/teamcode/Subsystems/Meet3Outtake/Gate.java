package org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Outtake;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Gate {
    public Servo gate;

    public GatePositions currentState = GatePositions.CLOSED;

    public enum GatePositions{
        OPEN(0.10),
        CLOSED(0.65);

        double pos;

        GatePositions(double pos){
            this.pos = pos;
        }
    }

    public Gate (HardwareMap gateMap){

        gate = gateMap.get(Servo.class, "gate");
    }

    public void update(){
        currentState = GatePositions.values()[(currentState.ordinal() + 1) % 2];
    }

    public void run(){
        gate.setPosition(currentState.pos);
    }
}
