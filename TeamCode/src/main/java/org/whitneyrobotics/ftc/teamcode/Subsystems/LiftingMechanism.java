package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whitneyrobotics.ftc.teamcode.Libraries.Controllers.PIDVACoefficients;
import org.whitneyrobotics.ftc.teamcode.Libraries.Controllers.PIDVAControllerNew;
import org.whitneyrobotics.ftc.teamcode.Libraries.Utilities.Functions;


public class LiftingMechanism {
    private final double KP = 0.01; // Proportional constant for PID control
    private final double MAX_POWER = 1.0; // Maximum power for the motors
    public static double maxVelocity = 3.5;
    public static double output;
    public static double TICKS_PER_INCH = 100;

    private double targetPosition = 26;
    private final DcMotorEx LSlide;
    private final DcMotorEx RSlide;
    public static PIDVACoefficients smoothCoefficients = new PIDVACoefficients()
            .setKP(0.1)
            .setKI(0.00)
            .setKD(0.012)
            .setKV(1.12)
            .setKA(0.04)
            .setKStatic(0.5)
            .setMaxAcceleration(9)
            .setMaxVelocity(3.5);
    public PIDVAControllerNew smooth = new PIDVAControllerNew(smoothCoefficients);

    public LiftingMechanism(HardwareMap hardwareMap) {
        LSlide = hardwareMap.get(DcMotorEx.class, "LiftingMotorLeft");
        RSlide = hardwareMap.get(DcMotorEx.class, "LiftingMotorRight");
        LSlide.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE); // Ensure the motors don't move when power is 0
        RSlide.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    }
    //extending lift to 26 inches
    public void extendLift() {
        //THIS IS TO INCREASE LENGTH OF SLIDES
        smooth.reset();
        smooth.setTarget(this.targetPosition);
        //ok so although this seems long it really isnt
        //Math.signum return whether its +(1.0) or -(-1.0) and we are seeing it our output is + or -
        //then we multiple by Functions.map which is mapping the output, we get within 0-1 as well as max velocity
        //in our output, it gives us everything including our velocity
        smooth.calculate(getPositionInches());
        output =  Math.signum(smooth.getOutput()) * Functions.map(Math.abs(smooth.getOutput()),0,maxVelocity,0,1);

        //left slide has to go counterclockwise
        LSlide.setPower(-output);
        //right slide has to go clockwise
        RSlide.setPower(output);
    }

    //lifting the robot up
    public void liftRobot() {
        //THIS IS TO DECREASE LENGTH OF SLIDES
        this.targetPosition = 14;
        //reset the time for VA
        smooth.reset();
        //settignt he target where we are putting the target position stated at the top
        smooth.setTarget(this.targetPosition);
        //explained at the top
        output =  Math.signum(smooth.getOutput()) * Functions.map(Math.abs(smooth.getOutput()),0,maxVelocity,0,1);

        //left slide has to go clockwise
        LSlide.setPower(output);
        //right slide has to go counterclockwise
        RSlide.setPower(-output);
    }
    public double getRawPosition(){
        return (LSlide.getCurrentPosition() + RSlide.getCurrentPosition())/2;
    }

    public double getPositionInches(){
        return getRawPosition()/TICKS_PER_INCH;
    }

}
