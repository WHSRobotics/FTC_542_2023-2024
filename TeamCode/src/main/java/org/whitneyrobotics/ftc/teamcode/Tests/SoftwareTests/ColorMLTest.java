package org.whitneyrobotics.ftc.teamcode.Tests.SoftwareTests;

import android.graphics.Color;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.vision.tfod.TfodProcessor;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.InterpreterApi;
import org.tensorflow.lite.TensorFlowLite;
import org.whitneyrobotics.ftc.teamcode.Extensions.OpModeEx.OpModeEx;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.LineItem;
import org.whitneyrobotics.ftc.teamcode.Libraries.TensorFlow.TF_Util;
import org.whitneyrobotics.ftc.teamcode.Libraries.Utilities.Callbacks;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TeleOp(name="Color ML Test", group="Software Tests")
public class ColorMLTest extends OpModeEx {
    public InterpreterApi color1Processor, color2Processor;
    RevColorSensorV3 color1;
    RevColorSensorV3 color2;
    List<LynxModule> hubs;
    String model1 = "color_model.tflite";
    public int detectionId = 0;
    String[] labels = {"Black", "White", "Yellow", "Purple", "Green"};
    private final int[] colors={Color.BLACK, Color.WHITE, Color.YELLOW, Color.MAGENTA, Color.GREEN};
    private int PID;

    public void processSensor(RevColorSensorV3 sensor, InterpreterApi processor){
        try {
            Map<String, Object> inputs = new HashMap<>();
            NormalizedRGBA rgba = sensor.getNormalizedColors();
//            inputs.put("r", rgba.red);
//            inputs.put("g", rgba.green);
//            inputs.put("b", rgba.blue);
//            inputs.put("a", rgba.alpha);
            inputs.put("Raw", sensor.getRawLightDetected());
            inputs.put("Color Int", rgba.toColor());
            Map<String, Object> outputs = new HashMap<>();
            outputs.put("color", 0);
            processor.run(inputs, outputs);
            Object res = outputs.getOrDefault("color",0);
            int detectionId = (int) (res!=null ? res : 0);
            if(detectionId != this.detectionId) {
                telemetryPro.addData("Color detected", labels[detectionId]);
                hubs.forEach(h -> {
                    h.setConstant(colors[detectionId]);
                });
                this.detectionId = detectionId;
            }
        } catch (Exception e) {
            for (StackTraceElement st : e.getStackTrace()) telemetryPro.addLine(st.toString(), LineItem.Color.RED, LineItem.RichTextFormat.BOLD).persistent();
        }
    }
    @Override
    public void initInternal() {
        color1 = hardwareMap.get(RevColorSensorV3.class, "color1");
        color2 = hardwareMap.get(RevColorSensorV3.class, "color2");
        hubs = hardwareMap.getAll(LynxModule.class);

        TF_Util.retrieveInitializeTask(hardwareMap.appContext).addOnSuccessListener(v->{
            telemetryPro.addData("TensorFlow Lite", "Initialized", LineItem.Color.GREEN).persistent();
            telemetryPro.update();
            color1Processor = InterpreterApi.create(new File(model1),
                new Interpreter.Options().setRuntime(InterpreterApi.Options.TfLiteRuntime.FROM_SYSTEM_ONLY));
                PID = Callbacks.setInterval(()->processSensor(color1, color1Processor), 1000);
        }).addOnFailureListener(e->{
            telemetryPro.addData("TensorFlow Lite", "Failed to initialize", LineItem.Color.RED).persistent();
            for (StackTraceElement st : e.getStackTrace()) telemetryPro.addLine(st.toString(), LineItem.Color.YELLOW, LineItem.RichTextFormat.BOLD).persistent();
        });
    }

    @Override
    public void startInternal() {
    }

    @Override
    protected void loopInternal() {

    }

    @Override
    public void stop() {
        Callbacks.clearInterval(PID);
    }
}
