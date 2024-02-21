package org.whitneyrobotics.ftc.teamcode.Tests.SoftwareTests;

import android.graphics.Color;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whitneyrobotics.ftc.teamcode.Extensions.OpModeEx.OpModeEx;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.LineItem;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.TelemetryPro;
import org.whitneyrobotics.ftc.teamcode.Libraries.TensorFlow.TF_Util;
import org.whitneyrobotics.ftc.teamcode.Subsystems.ColorSubsystem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TeleOp(name="Color Classification Simple", group="Software Tests")
public class ColorClassificationSimple extends OpModeEx {
    RevColorSensorV3 color1, color2;
    Map<Integer, ColorSubsystem.Colors> classes = new HashMap<>();
    List<LynxModule> hubs;
    @Override
    public void initInternal() {
        color1 = hardwareMap.get(RevColorSensorV3.class, "color1");
        color2 = hardwareMap.get(RevColorSensorV3.class, "color2");
        hubs = hardwareMap.getAll(LynxModule.class);
        classes.put(Color.WHITE, ColorSubsystem.Colors.WHITE);
        classes.put(Color.MAGENTA, ColorSubsystem.Colors.PURPLE_PIXEL);
        classes.put(Color.GREEN, ColorSubsystem.Colors.GREEN_PIXEL);
        classes.put(Color.YELLOW, ColorSubsystem.Colors.YELLOW_PIXEL);
    }

    @Override
    protected void loopInternal() {
        int closest = Color.BLACK, closestDiff = Integer.MAX_VALUE;
        if(color1.getRawLightDetected() <= 200){
            closest = Color.BLACK;
        } else {
            for (Map.Entry<Integer, ColorSubsystem.Colors> entry : classes.entrySet()) {
                int diff = Math.abs(entry.getKey() - color1.getNormalizedColors().toColor());
                if (diff < closestDiff){
                    closestDiff = diff;
                    closest = entry.getKey();
                }
            }
        }
        int finalClosest = closest;
        hubs.forEach(h->h.setConstant(finalClosest));
        telemetryPro.addData("WHITE", Color.WHITE);
        telemetryPro.addData("MAGENTA", Color.MAGENTA, LineItem.Color.FUCHSIA);
        telemetryPro.addData("GREEN", Color.GREEN, LineItem.Color.LIME);
        telemetryPro.addData("YELLOW", Color.YELLOW, LineItem.Color.ROBOTICS);
        telemetryPro.addData("Color Int", color1.getNormalizedColors().toColor());
        telemetryPro.addData("Difference", closestDiff);
    }
}
