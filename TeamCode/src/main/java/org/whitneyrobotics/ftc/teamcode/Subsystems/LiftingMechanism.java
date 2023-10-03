package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class LiftingMechanism {
    public Double liftingMotorPosition;
    public Double liftingSpeed;
    public boolean isOnTarget;

    public  DcMotorEx ActuatorMotor;

    public double liftingTargetPosition = 0;

    public double errorMargin = 0.5;

    public double liftingMotorPosition(){
        return ((ActuatorMotor.getCurrentPosition());
    }

    public boolean isOnTarget(){return Math.abs(liftingTargetPosition-liftingMotorPosition())<=errorMargin;}

}
