package org.whitneyrobotics.ftc.teamcode.subsys;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.whitneyrobotics.ftc.teamcode.lib.util.Functions;
import org.whitneyrobotics.ftc.teamcode.lib.util.Queue.RobotAction;

public class Claw {
    public Double clawServoPosition;
    public Double clawTargetPosition;
    public Double clawSpeed;
    public Double servoOnePosition;
    public Double linearSlidesPosition;
    public Double linearSlidesExtensionTarget;
    public Double linearSlidesSpeed;

    public final DcMotorEx SlidesMotor;

    public Double linearSlidesPosition(){
        return ((SlidesMotor.getCurrentPosition());
    }

    public Claw(HardwareMap hardwareMap){
        clawServoPosition = hardwareMap.get();
        clawTargetPosition = 0.0;
        clawSpeed = 0.0;
        servoOnePosition = 0.0;
        linearSlidesPosition = 0.0;
        linearSlidesExtensionTarget = 0.0;
        linearSlidesSpeed = 0.0;
    }

    public void setClawServoPosition(Double clawServoPosition){
        this.clawServoPosition = clawServoPosition;
    }
}
