package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.Libraries.Controllers.PIDVACoefficients;
import org.whitneyrobotics.ftc.teamcode.Libraries.Controllers.PIDVAControllerNew;
import org.whitneyrobotics.ftc.teamcode.Libraries.Utilities.Functions;

public class ClawMeetOne {
    private Servo mainClaw;
    private Servo wristOne;
    private Servo wristTwo;
    private Servo elbowOne;
    private Servo elbowTwo;
    private DcMotorEx linearSlides;

    private double clawState = 0;
    private double wristState = 0;
    private double elbowState = 0;
    private int linearSlideState = 0;
    private double linearSlidePosition;

    private boolean reset = true;
    private boolean resetWrist = true;
    private boolean resetElbow = true;
    private boolean resetSlides = true;

    private double slideMode = 0; // 0 is Macro, 1 is Micro

    private PIDVAControllerNew linearSlidesController;
    private PIDVACoefficients lSlidesCoeffs = new PIDVACoefficients();

    public enum ClawPositions {
        NONE(0),
        ONE(0.5),
        TWO(0.87);

        double pos;
        ClawPositions(double pos){
            this.pos = pos;
        }
    }

    public enum WristPositions {
        INTAKING(0, 0),
        OUTTAKING(0.7, 0.7);

        double wristOnePos;
        double wristTwoPos;

        WristPositions(double posOne, double posTwo){
            wristOnePos = posOne;
            wristTwoPos = posTwo;
        }
    }

    public enum ElbowPositions {
        INTAKING(0, 0),
        OUTTAKING(0.5, 0.5); // parallel to backdrop

        double elbowOnePos;
        double elbowTwoPos;

        ElbowPositions(double posOne, double posTwo){
            elbowOnePos = posOne;
            elbowTwoPos = posTwo;
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

    public ClawMeetOne(HardwareMap clawMap){
        mainClaw = clawMap.get(Servo.class, "Claw");
        wristOne = clawMap.get(Servo.class, "Wrist Left");
        wristTwo = clawMap.get(Servo.class, "Wrist Right");
        elbowOne = clawMap.get(Servo.class, "Elbow Left");
        elbowTwo = clawMap.get(Servo.class, "Elbow Right");
        linearSlides = clawMap.get(DcMotorEx.class, "Linear Slides");

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

    public void operateAuto(ClawPositions target){
        mainClaw.setPosition(target.pos);
        clawState = target.ordinal();
    }

    public void operateTele(boolean stateDec, boolean stateInc){
        if (stateInc && reset){
            clawState = (clawState + 1) % 3;
            reset = false;
        } else if (stateDec && reset){
            clawState = (clawState - 1) % 3;
            reset = false;
        }

        if (!stateInc && !stateDec){
            reset = true;
        }

        if (clawState == 0){
            mainClaw.setPosition(ClawPositions.NONE.pos);
        } else if (clawState == 1){
            mainClaw.setPosition(ClawPositions.ONE.pos);
        } else if (clawState == 2){
            mainClaw.setPosition(ClawPositions.TWO.pos);
        }
    }

    public void operateWristAuto(WristPositions target){
        wristOne.setPosition(target.wristOnePos);
        wristTwo.setPosition(target.wristTwoPos);
        wristState = target.ordinal();
    }

    public void operateWristTele(boolean change){
        if (change && resetWrist){
            wristState = (wristState + 1) % 2;
        }

        if (wristState == 0){
            wristOne.setPosition(WristPositions.INTAKING.wristOnePos);
            wristTwo.setPosition(WristPositions.INTAKING.wristTwoPos);
        } else if (wristState == 1){
            wristOne.setPosition(WristPositions.OUTTAKING.wristOnePos);
            wristTwo.setPosition(WristPositions.OUTTAKING.wristTwoPos);
        }
    }

    public void operateElbowAuto(ElbowPositions target){
        elbowOne.setPosition(target.elbowOnePos);
        elbowTwo.setPosition(target.elbowTwoPos);
        elbowState = target.ordinal();
    }

    public void operateElbowTele(boolean change){
        if (change && resetElbow){
            elbowState = (elbowState + 1) % 2;
        }

        if (elbowState == 0){
            elbowOne.setPosition(ElbowPositions.INTAKING.elbowOnePos);
            elbowTwo.setPosition(ElbowPositions.INTAKING.elbowTwoPos);
        } else if (elbowState == 1){
            elbowOne.setPosition(ElbowPositions.OUTTAKING.elbowOnePos);
            elbowTwo.setPosition(ElbowPositions.OUTTAKING.elbowTwoPos);
        }
    }

    public void operateLinearSlidesAuto(SlidePositions height){
        linearSlidesController.calculate(linearSlides.getCurrentPosition(), height.height);
        double output = linearSlidesController.getOutput();

        double adjustedOutput = Functions.map(Math.abs(output), 0, 2000, 0.4, 1);
        adjustedOutput = output <= 0 ? adjustedOutput * -1 : adjustedOutput;

        linearSlides.setPower(adjustedOutput);

        linearSlideState = height.ordinal();
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
                operateLinearSlidesAuto(SlidePositions.values()[linearSlideState]);
            } catch (Exception e) {
                operateLinearSlidesAuto(SlidePositions.values()[linearSlideState].height + linearSlidePosition);
            }
        } else if (slideMode == 1){
            operateLinearSlidesAuto(linearSlidePosition);
        }
    }
}
