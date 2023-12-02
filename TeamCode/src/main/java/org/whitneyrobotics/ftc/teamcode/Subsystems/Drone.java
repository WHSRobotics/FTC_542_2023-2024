package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Drone {

    Servo droneServo;
    Servo angleServo;

    enum States {
        PRIMED(0.42),
        LAUNCHED(0.76);
        States(double pos){
            this.pos = pos;
        }
        public final double pos;
    }

    enum Angles {
        DEG_15(0),
        DEG_30(0.1),
        DEG_45(0.2),
        DEG_60(0.3),
        DEG_75(0.4);

        public final double pos;
        Angles(double pos){
            this.pos = pos;
        }
    }

    enum BasicAngleMacros {
        REST(0.33),
        READY(0.14);

        public final double pos;
        BasicAngleMacros(double pos){
            this.pos = pos;
        }
    }

    public States currentState = States.PRIMED;
    public Angles currentAngle = Angles.DEG_15;
    public BasicAngleMacros currentMacro = BasicAngleMacros.REST;

    public Drone(HardwareMap hm){
        droneServo = hm.get(Servo.class, "droneServo");
        angleServo = hm.get(Servo.class, "angleServo");
        //droneServo.scaleRange(0, 1000);
    }

    public void fire(){
        this.currentState = (currentState == States.PRIMED) ? States.LAUNCHED : States.PRIMED;
    }

    public void updateAngle(){
        this.currentAngle = Angles.values()[(currentAngle.ordinal() + 1) % (Angles.values().length)];
    }

    public void updateMacroAngle(){
        this.currentMacro = BasicAngleMacros.values()[(currentMacro.ordinal() + 1) % (BasicAngleMacros.values().length)];
    }

    public void update(){
        droneServo.setPosition(currentState.pos);
        angleServo.setPosition(currentMacro.pos);
    }

    public String getState(){
        return currentState.name();
    }

    public double getPos(){
        return droneServo.getPosition();
    }
}
