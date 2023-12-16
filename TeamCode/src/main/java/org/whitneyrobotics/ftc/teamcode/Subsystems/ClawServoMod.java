//package org.whitneyrobotics.ftc.teamcode.Subsystems;
//
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.hardware.Servo;
//
//public class ClawServoMod {
//    private Servo clawServo;
//    private Servo wristServo;
//
//    private Servo currentServo = clawServo;
//    private boolean switchServoToggle = true;
//    private boolean incDecToggle = true;
//
//    private boolean clawToggle = true;
//    private boolean wristToggle = true;
//
//    private CLAW_STATES clawPos = CLAW_STATES.SECOND_DROP;
//    private CLAW_STATES[] clawStates = {CLAW_STATES.BOTH_LOCKED, CLAW_STATES.FIRST_DROP, CLAW_STATES.SECOND_DROP};
//
//    private WRIST_STATES wristPos = WRIST_STATES.DEG_30;
//    private WRIST_STATES[] wristStates = {WRIST_STATES.DEG_30, WRIST_STATES.SHAKE_ONE, WRIST_STATES.SHAKE_TWO};
//
//    public ClawServoMod (HardwareMap clawMap){
//        clawServo = clawMap.get(Servo.class, "claw");
//        wristServo = clawMap.get(Servo.class, "wrist");
//    }
//
//    private enum CLAW_STATES {
//        BOTH_LOCKED(0), FIRST_DROP(0.5), SECOND_DROP(1.0);
//        double position;
//        CLAW_STATES(double position){this.position = position;}
//    }
//
//    private enum WRIST_STATES {
//        DEG_30(0.5), SHAKE_ONE(0.55), SHAKE_TWO(0.45);
//
//        double position;
//        WRIST_STATES(double position){this.position = position;}
//    }
//
//    public void testPos(boolean inc, boolean dec, boolean switchServo){
//        double operationalPos = currentServo.getPosition();
//
//        if (switchServo && switchServoToggle){
//            if (currentServo == clawServo){
//                currentServo = wristServo;
//            } else if (currentServo == wristServo){
//                currentServo = clawServo;
//            }
//
//            switchServoToggle = false;
//        }
//
//        if (!switchServo){
//            switchServoToggle = true;
//        }
//
//        if (inc && incDecToggle){
//            currentServo.setPosition(Math.min(operationalPos + 0.01, 1));
//            incDecToggle = false;
//        } else if (dec && incDecToggle){
//            currentServo.setPosition(Math.max(0, operationalPos - 0.01));
//            incDecToggle = false;
//        }
//
//        if (!inc && !dec){
//            incDecToggle = true;
//        }
//    }
//
//    public void operateClaw(boolean stateDown, boolean stateUp){
//        if (stateUp && clawToggle){
//            clawPos = clawStates[(clawPos.ordinal() + 1) % 3];
//            clawToggle = false;
//        }
//
//        if (stateDown && clawToggle){
//            clawPos = clawStates[(clawPos.ordinal() - 1) % 3];
//            clawToggle = false;
//        }
//
//        clawServo.setPosition(clawPos.position);
//
//        if (!stateUp && !stateDown){
//            clawToggle = true;
//        }
//    }
//
//    public void operateWrist(boolean stateDown, boolean stateUp){
//        if (stateUp && wristToggle){
//            wristPos = wristStates[(wristPos.ordinal() + 1) % 3];
//            wristToggle = false;
//        }
//
//        if (stateDown && wristToggle){
//            wristPos = wristStates[(wristPos.ordinal() - 1) % 3];
//            wristToggle = false;
//        }
//
//        wristServo.setPosition(wristPos.position);
//
//        if (!stateUp && !stateDown){
//            wristToggle = true;
//        }
//    }
//}