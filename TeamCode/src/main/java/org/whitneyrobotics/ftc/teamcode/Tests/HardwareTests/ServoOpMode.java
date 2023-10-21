package org.whitneyrobotics.ftc.teamcode.Tests.HardwareTests;

import org.whitneyrobotics.ftc.teamcode.Extensions.OpModeEx.OpModeEx;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
@Config
@TeleOp(name = "ServoWilliam", group ="William")
public class ServoOpMode extends OpModeEx {
    Servo servo;
    public static double position = 0.5;
    @Override
    public void initInternal() {
        servo = hardwareMap.get(Servo.class, "servo");
    }

    @Override
    protected void loopInternal() {
        servo.setPosition(position);
    }
}
