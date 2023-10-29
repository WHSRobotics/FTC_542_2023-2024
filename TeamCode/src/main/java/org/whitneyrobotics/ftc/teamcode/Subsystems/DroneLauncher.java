package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class DroneLauncher{
    private final Servo launcher;
    private boolean override = false;

    //For closing the grabber, issues with going all the way down. Should decrease Close position
    public enum ReleaseStates {
        NON_RELEASE(0), RELEASE(0.5);
        //NEED TO FIGURE OUT ACTUAL NUMBERS
        private double position;
        ReleaseStates(double position){
            this.position = position;
        }
        public double getPosition() {
            return position;
        }
    }

    public ReleaseStates currentState = ReleaseStates.NON_RELEASE;

    public DroneLauncher(HardwareMap hardwareMap){
        launcher = hardwareMap.get(Servo.class,"droneLaunch");
        launcher.setPosition(currentState.getPosition());

    }


    public void toggleState(){
        currentState = (currentState == ReleaseStates.NON_RELEASE ? ReleaseStates.RELEASE : ReleaseStates.NON_RELEASE);
        launcher.setPosition(currentState.getPosition());
    }

    public void setState(boolean released){
        currentState = (released ? ReleaseStates.RELEASE : ReleaseStates.NON_RELEASE);
    }

    public void updateState(ReleaseStates state){
        currentState = state;
    }

    public void SetPosition(double position) {
        launcher.setPosition(position);
    }

    //these are the getter methods for the ReleaseStates and Servo Objects

    public double getPosition(){
        return launcher.getPosition();
    }

    public ReleaseStates getCurrentState(){
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


