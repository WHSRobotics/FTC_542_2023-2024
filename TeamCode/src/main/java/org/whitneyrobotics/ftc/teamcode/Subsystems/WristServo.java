//package org.whitneyrobotics.ftc.teamcode.Subsystems;
//
//import com.qualcomm.hardware.rev.RevColorSensorV3;
//import com.qualcomm.robotcore.hardware.ColorSensor;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.hardware.Servo;
//
//import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
//
//public class  WristServo{
//    private final Servo wrist;
//    private boolean override = false;
//
//    //For closing the grabber, issues with going all the way down. Should decrease Close position
//    public enum WristStates {
//        OPEN(0), CLOSE(0.55);
//        //NEED TO FIGURE OUT ACTUAL NUMBERS
//        private double position;
//        WristStates(double position){
//            this.position = position;
//        }
//        public double getPosition() {
//            return position;
//        }
//    }
//
//    public WristStates currentState = WristStates.OPEN;
//
//    public WristServo(HardwareMap hardwareMap){
//        wrist = hardwareMap.get(Servo.class,"wrist");
//        this.tick();
//    }
//
//
//    public void toggleState(){
//        currentState = (currentState == WristStates.OPEN ? WristStates.CLOSE : WristStates.OPEN);
//        wrist.setPosition(currentState.getPosition());
//    }
//
//    public void setState(boolean opened){
//        currentState = (opened ? WristStates.CLOSE : WristStates.OPEN);
//    }
//
//    public void updateState(WristStates state){
//        currentState = state;
//    }
//
//    public void setForceOpen(boolean state){
//        override = state;
//    }
//
//    public void testSetPosition(double position) {
//        wrist.setPosition(position);
//    }
//    public void forceOpen(){ override = true; }
//
//    public void tick() {
//        if(override){
//            wrist.setPosition(WristStates.OPEN.getPosition());
//            //override should be updated every loop
//            override = false;
//        } else {
//            wrist.setPosition(currentState.getPosition());
//        }
//    }
//
//    public double getPosition(){
//        return wrist.getPosition();
//    }
//
//    public WristStates getCurrentState(){
//        return currentState;
//    }
//
//    public Servo getArmServo(){
//        return wrist;
//    }
//
//    /**
//     * Standardized reset method for resetting encoders
//     */
//    public void resetEncoders() {
//
//    }
//}
//
//
