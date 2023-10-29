package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Drone {

    Servo droneServo;

    enum States {
        PRIMED(0.1),
        LAUNCHED(1);
        States(double pos){
            this.pos = pos;
        }
        public final double pos;
    }

    public States currentState = States.PRIMED;

    public Drone(HardwareMap hm){
        droneServo = hm.get(Servo.class, "droneServo");
    }

    public void fire(){
        this.currentState = (currentState == States.PRIMED) ? States.LAUNCHED : States.PRIMED;
    }

    public void update(){
        droneServo.setPosition(currentState.pos);
    }

    public String getState(){
        return currentState.name();
    }

    public double getPos(){
        return droneServo.getPosition();
    }
}
