package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Lifting {
    public Double liftingMotorPosition;
    public Double liftingSpeed;
    public boolean isOnTarget;

    public final DcMotorEx ActuatorMotor;

    public Double liftingTargetPosition = 0;

    public double errormargin = 0.5;

    public double liftingMotorPosition(){
        return ((ActuatorMotor.getCurrentPosition());
    }

    public boolean isOnTarget(){return Math.abs(liftingTargetPosition-liftingMotorPosition())<=errormargin;}

    }
}
