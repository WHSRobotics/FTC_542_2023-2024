package org.whitneyrobotics.ftc.teamcode.Tests.HardwareTests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whitneyrobotics.ftc.teamcode.Extensions.OpModeEx.OpModeEx;
import org.whitneyrobotics.ftc.teamcode.Tests.Test;

@Test(name="Linear Slides Meet 3")
@TeleOp(name="Linear Slides Meet 3 Test", group="Hardware Tests")
public class LinearSlidesMeet3Test extends OpModeEx {
    public LinearSlidesMeet3 slides;

    @Override
    public void initInternal() {
        slides = new LinearSlidesMeet3(hardwareMap);
        //slides.EmptyConstants();
        telemetryPro.useDashboardTelemetry(dashboardTelemetry);
        addTelemetryFields(slides, "velocity","currentTarget","");
        addTelemetryFields(slides.slidingController,"velocity");
        gamepad1.CIRCLE.onPress(e -> slides.setTarget(LinearSlidesMeet3.Target.MEDIUM));
        gamepad1.CROSS.onPress(e -> slides.setTarget(LinearSlidesMeet3.Target.LOWERED));
    }

    @Override
    protected void loopInternal() {
        slides.operate(gamepad1.LEFT_STICK_Y.value(),gamepad1.BUMPER_RIGHT.value());
        telemetryPro.addData("current position",slides.getPosition());

        telemetryPro.addData("current velocity",slides.velocity);
        telemetryPro.addData("target velocity",slides.slidingController.velocity);
        telemetryPro.addData("currentTarget",slides.currentTarget);
        telemetryPro.addData("PIDVA Controller phase",slides.slidingController.phase);
        telemetryPro.addData("PID Controller output", slides.output);
        telemetryPro.addData("sliding Controller output", slides.slidingController.getOutput());
        telemetryPro.addData("Error",slides.error);
        telemetryPro.addData("Mode",slides.currentState);
    }
}
