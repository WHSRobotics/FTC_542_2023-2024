//package org.whitneyrobotics.ftc.teamcode.Subsystems;
//
//import com.qualcomm.robotcore.hardware.DcMotorEx;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//
//public class FormalLinearSlides {
//    public DcMotorEx lSlides;
//    public double currentTarget = ServoPositions.DOWN.position;
//
//    private boolean generalChange = false;
//    private boolean fineChange = false;
//
//    private final double MIN_POWER = 0.6;
//
//    private void switchPos(){
//        if (currentTarget == ServoPositions.DOWN.position){
//            currentTarget = ServoPositions.UP.position;
//        } else if (currentTarget == ServoPositions.UP.position){
//            currentTarget = ServoPositions.DOWN.position;
//        }
//    }
//
//    private boolean isClose(double pos1, double pos2, double margin){
//        if (pos1 < pos2 && pos1 + margin > pos2){
//            return true;
//        } else if (pos2 < pos1 && pos2 + margin > pos1){
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public FormalLinearSlides (HardwareMap slidesMap){
//        lSlides = slidesMap.get(DcMotorEx.class, "slidesMotor");
//    }
//
//    public enum ServoPositions {
//        DOWN(-10),
//        UP(1215);
//        private double position;
//        ServoPositions(double position){
//            this.position = position;
//        }
//    }
//
//    public void operateTeleOp(boolean general, boolean fineInc, boolean fineDec){
//        if (general && !generalChange) {
//            switchPos();
//            generalChange = true;
//        }
//
//        if (!general){
//            generalChange = false;
//        }
//
//        if (fineInc && !fineChange){
//            currentTarget += 25;
//            fineChange = true;
//        }
//
//        if (fineDec && !fineChange){
//            currentTarget -= 25;
//            fineChange = true;
//        }
//
//        if (!fineInc && !fineDec){
//            fineChange = false;
//        }
//
//        double power = Math.max(Math.min(1, Math.abs(lSlides.getCurrentPosition() - currentTarget) / 1200), MIN_POWER);
//        power *= lSlides.getCurrentPosition() < currentTarget ? 1 : -1;
//        power = isClose(lSlides.getCurrentPosition(), currentTarget, (currentTarget == ServoPositions.DOWN.position ? 25 : 20)) ? (currentTarget == ServoPositions.DOWN.position ? 0 : 0.1) : power;
//
//        lSlides.setPower(power);
//    }
//
//}
