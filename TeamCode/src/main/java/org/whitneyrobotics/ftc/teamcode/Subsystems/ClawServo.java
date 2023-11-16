package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class ClawServo{
    private final Servo claw;
    private double initializationCutoff = 5;
    private boolean override = false;

    //For closing the grabber, issues with going all the way down. Should decrease Close position
    public enum WristStates {
        OPEN(0), CLOSE(0.83);
        //NEED TO FIGURE OUT ACTUAL NUMBERS
        private double position;
        WristStates(double position){
            this.position = position;
        }
        public double getPosition() {
            return position;
        }
    }

    public WristStates currentState = WristStates.OPEN;

    public ClawServo(HardwareMap hardwareMap){
        claw = hardwareMap.get(Servo.class,"wrist");
        this.tick();
    }


    public void toggleState(){
        currentState = (currentState == WristStates.OPEN ? WristStates.CLOSE : WristStates.OPEN);
        claw.setPosition(currentState.getPosition());
    }

    public void setState(boolean opened){
        currentState = (opened ? WristStates.CLOSE : WristStates.OPEN);
    }

    public void updateState(WristStates state){
        currentState = state;
    }

    public void setForceOpen(boolean state){
        override = state;
    }

    public void testSetPosition(double position) {
        claw.setPosition(position);
    }
    public void forceOpen(){ override = true; }

    public void tick() {
        if(override){
            claw.setPosition(WristStates.OPEN.getPosition());
            //override should be updated every loop
            override = false;
        } else {
            claw.setPosition(currentState.getPosition());
        }
    }

    public double getPosition(){
        return claw.getPosition();
    }

    public WristStates getCurrentState(){
        return currentState;
    }

    public Servo getArmServo(){
        return claw;
    }

    /**
     * Standardized reset method for resetting encoders
     */
    public void resetEncoders() {

    }
}