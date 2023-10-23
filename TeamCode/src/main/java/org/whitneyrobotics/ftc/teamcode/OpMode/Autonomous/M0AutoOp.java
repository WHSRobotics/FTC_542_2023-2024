package org.whitneyrobotics.ftc.teamcode.OpMode.Autonomous;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.whitneyrobotics.ftc.teamcode.Constants.Alliance;
import org.whitneyrobotics.ftc.teamcode.Extensions.OpModeEx.OpModeEx;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.AutoSetupTesting.TestManager;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.AutoSetupTesting.Tests;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.KeyValueLine;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.LineItem;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.TelemetryPro;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.TextLine;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.TextLineLambda;
import org.whitneyrobotics.ftc.teamcode.Subsystems.ColorSubsystem;
import org.whitneyrobotics.ftc.teamcode.Subsystems.RobotImpl;

import java.util.List;

@Autonomous(name="M0 Auto", group="Autonomous", preselectTeleOp = "WHS TeleOp")
public class M0AutoOp extends OpModeEx {
    RobotImpl robot;
    @Override
    public void initInternal() {
        robot = RobotImpl.getInstance(hardwareMap);
        telemetryPro.useTestManager()
                .addTest("Gamepad 1 Initialization", () -> Tests.assertGamepadSetup(gamepad1, "Gamepad 1"))
                .addTest("Gamepad 2 Initialization", () -> Tests.assertGamepadSetup(gamepad1, "Gamepad 2"))
                .addTest("Battery voltage test", () -> Tests.assertBatteryCharged(hardwareMap.get(LynxModule.class, "Control Hub")));
        telemetryPro.addItem(new KeyValueLine(
                "Alliance",
                true,
                robot.alliance::name,
                (robot.alliance == Alliance.RED ? LineItem.Color.RED : LineItem.Color.BLUE))
        );
        telemetryPro.useDashboardTelemetry(dashboardTelemetry);

    }

    @Override
    public void initInternalLoop(){
        List<TestManager.Test> results =  telemetryPro.getTestManager().run();
        ColorSubsystem.Colors desiredColor = ColorSubsystem.Colors.GREEN_PIXEL;
        if(results.stream().anyMatch(test -> test.getWarning() || test.getFailed())){
            desiredColor = ColorSubsystem.Colors.ERROR_FLASHING;
        }
        if(gamepad1.TRIANGLE.value()){ //Press and hold triangle to check alliance
            desiredColor = robot.alliance == Alliance.RED ? ColorSubsystem.Colors.RED : ColorSubsystem.Colors.BLUE;
        }
        robot.colorSubsystem.requestColor(desiredColor);
    }

    @Override
    protected void loopInternal() {

    }
}
