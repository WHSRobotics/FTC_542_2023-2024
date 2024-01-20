package org.whitneyrobotics.ftc.teamcode.Tests.SubsystemTests;

import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.Extensions.OpModeEx.OpModeEx;

public class WilliamM3DroneTest extends OpModeEx {
    public static double currentPositionAngle;
    public static double currentPositionLaunch;

    Servo angle = hardwareMap.get(Servo.class, "angle");
    Servo launch = hardwareMap.get(Servo.class, "launch");

    @Override
    public void initInternal() {
        telemetryPro.useDashboardTelemetry(dashboardTelemetry);
        dashboardTelemetry.setMsTransmissionInterval(15);
    }

    @Override
    protected void loopInternal() {
        angle.setPosition(currentPositionAngle);
        launch.setPosition(currentPositionLaunch);

        telemetryPro.addData("Angle Position", currentPositionAngle);
        telemetryPro.addData("Launch Position", currentPositionLaunch);
    }
}
