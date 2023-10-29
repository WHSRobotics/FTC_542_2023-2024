package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whitneyrobotics.ftc.teamcode.Libraries.Controllers.ControlConstants;
import org.whitneyrobotics.ftc.teamcode.Libraries.Controllers.PIDController;
import org.whitneyrobotics.ftc.teamcode.Libraries.Motion.EfficientMotionProfileTrapezoidal;
import org.whitneyrobotics.ftc.teamcode.Libraries.Utilities.NanoStopwatch;

public class FormalPIDLinearSlides {
    public EfficientMotionProfileTrapezoidal slideMotion;
    public DcMotorEx lSlides;

    private NanoStopwatch stopwatch;

    public double currentTarget = ServoPositions.DOWN.position;
    public double currentMacro = currentTarget;

    private boolean generalChange = false;

    private ControlConstants linearSlideConstants;
    private PIDController slidePID;

    private boolean isClose(double pos1, double pos2, double margin){
        if (pos1 < pos2 && pos1 + margin > pos2){
            return true;
        } else if (pos2 < pos1 && pos2 + margin > pos1){
            return true;
        } else {
            return false;
        }
    }

    private void switchPos(){
        if (currentMacro == ServoPositions.DOWN.position){
            currentMacro = ServoPositions.UP.position;
            currentTarget = ServoPositions.UP.position;
        } else if (currentMacro == ServoPositions.UP.position){
            currentMacro = ServoPositions.DOWN.position;
            currentTarget = ServoPositions.DOWN.position;
        }
    }

    public enum ServoPositions {
        DOWN(-10),
        UP(1215);
        private double position;
        ServoPositions(double position){
            this.position = (position / 537.7) * 2 * Math.PI;
        }
    }

    public FormalPIDLinearSlides (HardwareMap slidesMap){
        slideMotion = new EfficientMotionProfileTrapezoidal(131.17918649480848, 5.367545237289094);
        lSlides = slidesMap.get(DcMotorEx.class, "slidesMotor");
        linearSlideConstants = new ControlConstants(0.5, 0, 0);
        stopwatch = new NanoStopwatch();
        stopwatch.reset();
        slidePID = new PIDController(linearSlideConstants);
    }

    public double operateTeleOp(boolean general, double fineChange){
        if (general && !generalChange) {
            switchPos();
            generalChange = true;
            stopwatch.reset();
            slideMotion.mapMotionProfiles(currentTarget);
        }

        if (!general){
            generalChange = false;
        }

        if (fineChange != 0) {
            currentTarget += fineChange;
            stopwatch.reset();
            slideMotion.mapMotionProfiles(currentTarget);
        }

        double error = slideMotion.velAt(stopwatch.seconds()) - lSlides.getVelocity();

        slidePID.calculate(error);

        double power = Math.abs(Math.max(Math.min(1, slidePID.getOutput()), 0.6));

        power *= error > 0 ? 1 : -1;
        power = Math.abs(error) < 0.5 ? 0 : power;

        lSlides.setPower(power);

        return error;
    }
}
