package org.whitneyrobotics.ftc.teamcode.Tests.HardwareTests;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.whitneyrobotics.ftc.teamcode.MotionProfile.MotionProfileTrapezoidal;
import org.whitneyrobotics.ftc.teamcode.Extensions.OpModeEx.OpModeEx;
import org.whitneyrobotics.ftc.teamcode.Libraries.Controllers.PIDControlledMotor;
import org.whitneyrobotics.ftc.teamcode.Tests.Test;

@RequiresApi(api = Build.VERSION_CODES.N)
@TeleOp(name="Motor PID Velocity Test", group = "PID Test")
@Test(name="PIDMotor Vel Test")
@Config
public class MotorPIDVelocityTest extends OpModeEx {
    public static double kP = 0.2;
    public static double kI = 0;
    public static double kD = 0.01;
    public static double DEADBAND = 0.5;

    /**
     * User defined init method
     * <p>
     * This method will be called once when the INIT button is pressed.
     */
    private final double maxVelocity = 30;
    //private final int ticksToInch = 300;
    private int targetPosition = 500;
    PIDControlledMotor ilMottore;
    MotionProfileTrapezoidal motionProfile = new MotionProfileTrapezoidal(0, 5, maxVelocity, DEADBAND);
    public boolean secondPass = false;

    DcMotorEx motor;

    void setupGamepads(){
        gamepad1.Y.onPress(e -> ilMottore.reloadCoefficients(kP, kI, kD));
    }


    @Override
    public void initInternal() {
        setupGamepads();
        motor = hardwareMap.get(DcMotorEx.class,"driveFL");
        ilMottore = new PIDControlledMotor(motor, 32);
        ilMottore.reloadCoefficients(kP, kI, kD);
        initializeDashboardTelemetry(50);
        telemetryPro.useDashboardTelemetry(dashboardTelemetry);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //ilMottore.setDirection(DcMotorSimple.Direction.REVERSE);
        dashboardTelemetry.addData("Current Velocity",0);
    }

    public static double TICKS_PER_REV = 537.6;
    public double getPosition() {
        return motor.getCurrentPosition() * (2 * Math.PI / TICKS_PER_REV);
    }

/*
    @Override
    public void startInternal() {
        motor.reset();
    }

 */

    /**
     * User defined loop method
     * <p>
     * This method will be called repeatedly in a loop while this op mode is running
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void loopInternal() {
        double targetVel = 0;
        if (motionProfile.isFinished() && secondPass) {
            telemetryPro.addLine("Completed. Hopefully.");
        }
        else {
            telemetryPro.addLine("code is runing");
            targetVel = motionProfile.calculate(getPosition(), targetPosition);
            ilMottore.setTargetVelocity(targetVel); //inches / sec to radians/sec
            if (motionProfile.isFinished()) {
                targetPosition = 0;
                secondPass = true;
                motionProfile = new MotionProfileTrapezoidal(motor.getCurrentPosition(), 2, maxVelocity, DEADBAND);
            }
        }
        ilMottore.update();
        try {
            telemetryPro.addData("Current Pos", getPosition());
            telemetryPro.addData("Used Acceleration", motionProfile.currentAcceleration);
            telemetryPro.addData("Last recorded time", motionProfile.lastRecordedTime);
            telemetryPro.addData("Target Pos", targetPosition);
            telemetryPro.addData("Current Velocity", motor.getVelocity(AngleUnit.RADIANS));
            telemetryPro.addData("Target Velocity", targetVel);
            telemetryPro.addData("Motor Power",motor.getPower());
            telemetryPro.addData("Error",targetPosition-getPosition());
            telemetryPro.addData("Output", ilMottore.controller.getOutput());
            telemetryPro.addData("Integral", ilMottore.controller.getIntegral());
            telemetryPro.addData("Derivative", ilMottore.controller.getDerivative());
            telemetryPro.addData("Phase", motionProfile.phase);
            telemetryPro.addData("last power", ilMottore.lastPower);
        } catch (NullPointerException e) {
            telemetry.addLine("something doesn't exist");
        }
    }
}
