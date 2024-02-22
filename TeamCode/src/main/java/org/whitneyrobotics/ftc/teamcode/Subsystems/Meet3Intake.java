package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Config
public class Meet3Intake {
    private Servo intakeServo;
    private DcMotorEx intakeMotor;

    public static final double MAX_RPM = 312;

    private double motorPower = 0.0;

    public static double RAISED = 1.0, STACK = 0.85, ONE = 0.8;

    public static double servoPos = RAISED;
    private boolean reversed = false;

    public void setRPM(double RPM){
        motorPower = RPM/MAX_RPM;
    }

    public void setReversed(boolean rev){
        reversed = rev;
    }

    public void raisedPosition(){
        servoPos = RAISED;
    }
    public void stackPosition(){
        servoPos = STACK;
    }
    public void onePosition(){
        servoPos = ONE;
    }

    public Meet3Intake(HardwareMap hardwareMap){
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intakeServo = hardwareMap.get(Servo.class, "intakeServo");
    }

    public void update(){
        intakeMotor.setPower(motorPower * (reversed ? -1 : 1));
        intakeServo.setPosition(servoPos);
    }

    public double getTargetRPM(){
        return motorPower*MAX_RPM*(reversed ? -1 : 1);
    }

    public double getActualRPM(){
        return (intakeMotor.getVelocity(AngleUnit.RADIANS) * 30/Math.PI);
    }

    public double servoPos(){
        return servoPos;
    }
}
