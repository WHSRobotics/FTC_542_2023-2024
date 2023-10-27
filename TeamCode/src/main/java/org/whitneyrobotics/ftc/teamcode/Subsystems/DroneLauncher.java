package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class DroneLauncher{
    private final Servo launcher;
    private double initializationCutoff = 5;
    private boolean override = false;

    private Servo[] servos = new Servo[2];

    //For closing the grabber, issues with going all the way down. Should decrease Close position
    public enum WristStates {
        OPEN(0), CLOSE(0.5);
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

    public DroneLauncher(HardwareMap hardwareMap){
        launcher = hardwareMap.get(Servo.class,"wrist");
        this.tick();
    }


    public void toggleState(){
        currentState = (currentState == WristStates.OPEN ? WristStates.CLOSE : WristStates.OPEN);
        launcher.setPosition(currentState.getPosition());
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
        launcher.setPosition(position);
    }
    public void forceOpen(){ override = true; }

    public void tick() {
        if(override){
            launcher.setPosition(WristStates.OPEN.getPosition());
            //override should be updated every loop
            override = false;
        } else {
            launcher.setPosition(currentState.getPosition());
        }
    }

    public double getPosition(){
        return launcher.getPosition();
    }

    public WristStates getCurrentState(){
        return currentState;
    }

    public Servo getArmServo(){
        return launcher;
    }

    /**
     * Standardized reset method for resetting encoders
     */
    public void resetEncoders() {

    }
}


