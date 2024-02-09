package org.whitneyrobotics.ftc.teamcode.Tests.HardwareTests;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.whitneyrobotics.ftc.teamcode.Extensions.OpModeEx.OpModeEx;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.LineItem;

@TeleOp(name="Color Box Test")
public class ColorBoxTest extends OpModeEx {
    RevColorSensorV3 c1, c2;

    @Override
    public void initInternal() {
        c1 = hardwareMap.get(RevColorSensorV3.class, "color1");
        c2 = hardwareMap.get(RevColorSensorV3.class, "color2");
        telemetryPro.useDashboardTelemetry(dashboardTelemetry);
        setBulkReadBehavior(BULK_READ_BEHAVIOR.ONE_FRAME_PER_LOOP);
    }

    @Override
    protected void loopInternal() {
        NormalizedRGBA c1Data = c1.getNormalizedColors();
        telemetryPro.addData("C1 R", c1Data.red, LineItem.Color.RED);
        telemetryPro.addData("C1 G", c1Data.green, LineItem.Color.GREEN);
        telemetryPro.addData("C1 B", c1Data.blue, LineItem.Color.BLUE);
        telemetryPro.addData("C1 A", c1Data.alpha);

        NormalizedRGBA c2Data = c2.getNormalizedColors();
        telemetryPro.addData("C2 R", c2Data.red, LineItem.Color.RED);
        telemetryPro.addData("C2 G", c2Data.green, LineItem.Color.GREEN);
        telemetryPro.addData("C2 B", c2Data.blue, LineItem.Color.BLUE);
        telemetryPro.addData("C2 A", c2Data.alpha);
    }
}
