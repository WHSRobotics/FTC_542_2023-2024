package org.whitneyrobotics.ftc.teamcode.lib;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.whitneyrobotics.ftc.teamcode.framework.opmodes.OpModeEx;
import org.whitneyrobotics.ftc.teamcode.lib.StateRegistrar;

@TeleOp
public class TestingStateOpMode extends OpModeEx {
    DcMotorEx motor;

    @Override
    public void initInternal() {
        motor = StateRegistrar.fetchHardware(hardwareMap, DcMotorEx.class, "motor");
    }

    @Override
    protected void loopInternal() {

    }
}
