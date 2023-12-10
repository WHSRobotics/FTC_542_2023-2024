package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.Libraries.Controllers.PIDVACoefficients;
import org.whitneyrobotics.ftc.teamcode.Libraries.Controllers.PIDVAControllerNew;
import org.whitneyrobotics.ftc.teamcode.Libraries.Utilities.Functions;
public class BackupClaw {
    private Servo gate;
    private Servo wrist;
    private DcMotorEx linearSlides;
    private DcMotorEx intakeMotor;
    private double gateState = 0;
    private double wristState = 0;
    private int linearSlideState = 0;
    private double linearSlidePosition;
    private boolean reset = true;
    private boolean resetSlides = true;
    private double slideMode = 1;
    private PIDVAControllerNew linearSlidesController;
    private PIDVACoefficients lSlidesCoeffs = new PIDVACoefficients();
    public enum GatePositions {
        Closed(0),
        Open(0.75);

        double pos;
        GatePositions(double pos){
            this.pos = pos;
        }
    }
    public enum WristPositions {
        INTAKING(0),
        OUTTAKING(0.7);

        double wristPos;

        WristPositions(double pos){
            wristPos = pos;
        }
    }
    public enum SlidePositions {
        ONE(0),
        TWO(10),
        TOUCH_ONE(20),
        FOUR(30),
        FIVE(40),
        TOUCH_TWO(50),
        SEVEN(60),
        EIGHT(70),
        NINE(80),
        TEN(90),
        ELEVEN(100);

        double height;
        SlidePositions(double height){
            this.height = height;
        }
    }
    public BackupClaw(HardwareMap hardwareMap){
        gate = hardwareMap.get(Servo.class, "gate");
        wrist = hardwareMap.get(Servo.class, "wrist");
        linearSlides = hardwareMap.get(DcMotorEx.class, "linearSlides");
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
        linearSlides.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        linearSlides.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        linearSlides.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        linearSlidesController = new PIDVAControllerNew(lSlidesCoeffs, 0, 0, 0, 0);
        linearSlidesController = new PIDVAControllerNew(lSlidesCoeffs);
        lSlidesCoeffs.setKP(1)
                .setKI(1)
                .setKD(1)
                .setKV(1)
                .setKA(1)
                .setKStatic(1)
                .setMaxVelocity(1)
                .setMaxAcceleration(1);
    }
    public void operateGateTele(boolean stateDec, boolean stateInc){
        if (stateInc && reset){
            gateState = (gateState + 1) % 3;
            reset = false;
        } else if (stateDec && reset){
            gateState = (gateState - 1) % 3;
            reset = false;
        }
        if (!stateInc && !stateDec) {
            reset = true;
        }
    }
    public void operateWristTele(boolean stateDec, boolean stateInc){
        if (stateInc && reset){
            wristState = (wristState + 1) % 3;
            reset = false;
        } else if (stateDec && reset){
            wristState = (wristState - 1) % 3;
            reset = false;
        }
        if (!stateInc && !stateDec){
            reset = true;
        }
        if (wristState == 0){
            wrist.setPosition(WristPositions.INTAKING.wristPos);
        } else if (wristState == 1){
            wrist.setPosition(WristPositions.INTAKING.wristPos);
        }
    }
    public void operateLinearSlidesAuto(double height){
        linearSlidesController.calculate(linearSlides.getCurrentPosition(), height);
        double output = linearSlidesController.getOutput();

        double adjustedOutput = Functions.map(Math.abs(output), 0, 2000, 0.4, 1);
        adjustedOutput = output <= 0 ? adjustedOutput * -1 : adjustedOutput;

        linearSlides.setPower(adjustedOutput);
    }
    public void operateLinearSlidesMacroAndMicro(boolean changeMode, boolean dec, boolean inc, boolean microDec, boolean microInc){
        if (changeMode && resetSlides){
            slideMode = (slideMode + 1) % 2;
            resetSlides = false;
        }
        if (inc && resetSlides){
            linearSlideState = (linearSlideState + 1) % 3;
            linearSlidePosition = 0;
            resetSlides = false;
        } else if (dec && resetSlides){
            linearSlideState = (linearSlideState - 1) % 3;
            linearSlidePosition = 0;
            resetSlides = false;
        }
        if (!inc && !dec && !changeMode){
            resetSlides = true;
        }
        if (slideMode == 0){
            try {
                operateLinearSlidesAuto(ClawMeetOne.SlidePositions.values()[linearSlideState].ordinal());
            } catch (Exception e) {
                operateLinearSlidesAuto(ClawMeetOne.SlidePositions.values()[linearSlideState].height + linearSlidePosition);
            }
        } else if (slideMode == 1){
            operateLinearSlidesAuto(linearSlidePosition);
        }
    }
    public void operateIntakeMotorTele(boolean stateDec, boolean stateInc){
        if (stateInc && reset){
            intakeMotor.setPower(1);
            reset = false;
        } else if (stateDec && reset){
            intakeMotor.setPower(-1);
            reset = false;
        }
        if (!stateInc && !stateDec){
            intakeMotor.setPower(0);
            reset = true;
        }
    }
}