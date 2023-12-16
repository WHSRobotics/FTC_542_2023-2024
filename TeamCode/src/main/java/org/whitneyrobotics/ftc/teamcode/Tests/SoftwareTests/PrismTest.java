//package org.whitneyrobotics.ftc.teamcode.Tests.SoftwareTests;
//
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//
//import org.whitneyrobotics.ftc.teamcode.Extensions.OpModeEx.OpModeEx;
//import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.LineItem;
////import org.whitneyrobotics.ftc.teamcode.Subsystems.PrismSensor;
//import org.whitneyrobotics.ftc.teamcode.Tests.Test;
//
//import java.util.Arrays;
//
//@Test(name="Prism test", description = "Tests the state machine of the solid state prism color detector")
//@TeleOp(name="Prism Test", group="Software Tests")
//public class PrismTest extends OpModeEx {
//    PrismSensor sensor;
//    @Override
//    public void initInternal() {
//        sensor = new PrismSensor(hardwareMap);
//        gamepad1.CROSS.onPress(sensor::requestImmediateDetection);
//    }
//
//    @Override
//    protected void loopInternal() {
//        sensor.update();
//        telemetryPro.addData("RGB TOP", sensor.colors[1]);
//        telemetryPro.addData("RGB BOTTOM", sensor.colors[0]);
//        telemetryPro.addData("State",sensor.getState());
//        telemetryPro.addData("Servo Positions", Arrays.toString(sensor.getPositions()), LineItem.Color.ROBOTICS);
//    }
//}
