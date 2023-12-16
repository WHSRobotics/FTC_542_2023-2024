package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.Libraries.Controllers.PIDVACoefficients;
import org.whitneyrobotics.ftc.teamcode.Libraries.Controllers.PIDVAControllerNew;
import org.whitneyrobotics.ftc.teamcode.Libraries.Utilities.Functions;

public class JeffClaw {
    private DcMotor intake;
    private Servo intakeHeight;
    private Servo gate;
    private Servo wristOne;
    private Servo wristTwo;
    private Servo elbowOne;
    private Servo elbowTwo;
    private DcMotorEx linearSlides;

    private double intakeState = 0;
    private double intakeHeightState = 0;
    private double gateState = 0;
    private double wristState = 0;
    private double elbowState = 0;
    private int linearSlideState = 0;
    private double linearSlidePosition;

    private boolean resetIntake = true;
    private boolean resetIntakeHeight = true;
    private boolean resetGate = true;
    private boolean resetWrist = true;
    private boolean resetElbow = true;
    private boolean resetSlides = true;

    private double slideMode = 0; // 0 is Macro, 1 is Micro

    private PIDVAControllerNew linearSlidesController;
    private PIDVACoefficients lSlidesCoeffs = new PIDVACoefficients();

    public JeffClaw(HardwareMap clawMap){
        intake = clawMap.get(DcMotor.class, "intake");
        intakeHeight = clawMap.get(Servo.class, "intakeFlip");
        gate = clawMap.get(Servo.class, "gate");
        wristOne = clawMap.get(Servo.class, "wristLeft");
        wristTwo = clawMap.get(Servo.class, "wristRight");
        elbowOne = clawMap.get(Servo.class, "elbowLeft");
        elbowTwo = clawMap.get(Servo.class, "elbowRight");
        linearSlides = clawMap.get(DcMotorEx.class, "linearSlides");

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

    public enum IntakeStates {
        INTAKING(0.9),
        OUTTAKING(-1), //if stuck
        OFF(0);

        double state;
        IntakeStates(double state){
            this.state = state;
        }
    }
    public enum IntakeHeightPositions {
        ONE(0.2),
        TWO(0.3),
        THREE(0.5),
        FOUR(0.7),
        FIVE(0.9);

        double IHPos;
        IntakeHeightPositions(double IHpos){
            this.IHPos = IHPos;
        }
    }

    public enum GatePositions {
        OPEN(0),
        CLOSED(0.43);

        double GatePos;
        GatePositions(double GatePos){
            this.GatePos = GatePos;
        }
    }

    public enum WristPositions {
        INTAKING(1, 1),
        OUTTAKING(0.275, 0.275);

        double wristOnePos;
        double wristTwoPos;

        WristPositions(double posOne, double posTwo){
            wristOnePos = posOne;
            wristTwoPos = posTwo;
        }
    }

    public enum ElbowPositions {
        INTAKING(1, 1),
        OUTTAKING(0.9, 0.9); // parallel to backdrop

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
    public void operateIntakeAuto(IntakeStates state) {
        intake.setPower(state.state);
        intakeState = state.ordinal();
    }
    public void operateIntakeTele() {
        intakeState = (intakeState + 1) % 3;

        if (intakeState == 0){
            intake.setPower(IntakeStates.INTAKING.state);
        } else if (intakeState == 1) {
            intake.setPower(IntakeStates.OFF.state);
        }
    }
    public void operateIntakeOutTele() {
        intake.setPower(IntakeStates.OUTTAKING.state);
    }


    public void operateIntakeHeightAuto(IntakeHeightPositions target){
        intakeHeight.setPosition(target.IHPos);
        intakeHeightState = target.ordinal();
    }

    public void operateIntakeHeightTele(){
        intakeHeightState = (intakeHeightState + 1) % 5;

        if (intakeHeightState == 0){
            intakeHeight.setPosition(IntakeHeightPositions.ONE.IHPos);
        } else if (intakeHeightState == 1){
            intakeHeight.setPosition(IntakeHeightPositions.TWO.IHPos);
        } else if (intakeHeightState == 2){
            intakeHeight.setPosition(IntakeHeightPositions.THREE.IHPos);
        } else if (intakeHeightState == 3) {
            intakeHeight.setPosition(IntakeHeightPositions.FOUR.IHPos);
        } else if (intakeHeightState == 4) {
            intakeHeight.setPosition(IntakeHeightPositions.FIVE.IHPos);
        }
    }
    public void operateGateAuto(GatePositions target) {
        gate.setPosition(target.GatePos);
        gateState = target.ordinal();
    }
    public void operateGateTele() {
        gateState = (gateState + 1) % 2;

        if (gateState == 0){
            gate.setPosition(GatePositions.OPEN.GatePos);
        } else if (gateState == 1){
            gate.setPosition(GatePositions.CLOSED.GatePos);
        }
    }
    public void operateWristAuto(WristPositions target){
        wristOne.setPosition(target.wristOnePos);
        wristTwo.setPosition(target.wristTwoPos);
        wristState = target.ordinal();
    }

    public void operateWristTele(){
        wristState = (wristState + 1) % 2;
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

    public void operateElbowTele(){
        elbowState = (elbowState + 1) % 2;

        if (elbowState == 0){
            elbowOne.setPosition(ElbowPositions.INTAKING.elbowOnePos);
            elbowTwo.setPosition(ElbowPositions.INTAKING.elbowTwoPos);
        } else if (elbowState == 1){
            elbowOne.setPosition(ElbowPositions.OUTTAKING.elbowOnePos);
            elbowTwo.setPosition(ElbowPositions.OUTTAKING.elbowTwoPos);
        }
    }

   /* public void operateLinearSlidesAuto(SlidePositions height){
        linearSlidesController.calculate(linearSlides.getCurrentPosition(), height.height);
        double output = linearSlidesController.getOutput();

        double adjustedOutput = Functions.map(Math.abs(output), 0, 2000, 0.4, 1);
        adjustedOutput = output <= 0 ? adjustedOutput * -1 : adjustedOutput;

        linearSlides.setPower(adjustedOutput);

        linearSlideState = height.ordinal();
    }
*/
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
                operateLinearSlidesAuto(SlidePositions.values()[linearSlideState].ordinal());
            } catch (Exception e) {
                operateLinearSlidesAuto(SlidePositions.values()[linearSlideState].height + linearSlidePosition);
            }
        } else if (slideMode == 1){
            operateLinearSlidesAuto(linearSlidePosition);
        }
        }

}