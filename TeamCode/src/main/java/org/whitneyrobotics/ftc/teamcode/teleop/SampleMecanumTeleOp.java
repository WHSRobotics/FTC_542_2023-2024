package org.whitneyrobotics.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.whitneyrobotics.ftc.teamcode.BetterTelemetry.LineItem;
import org.whitneyrobotics.ftc.teamcode.BetterTelemetry.SliderDisplayLine;
import org.whitneyrobotics.ftc.teamcode.framework.Vector;
import org.whitneyrobotics.ftc.teamcode.framework.opmodes.OpModeEx;
import org.whitneyrobotics.ftc.teamcode.subsys.IMU;
import org.whitneyrobotics.ftc.teamcode.subsys.IMU2;

import java.util.function.UnaryOperator;

@TeleOp(name = "Sample Mecanum TeleOp", group = "teleop")
public class SampleMecanumTeleOp extends OpModeEx {
    DcMotorEx fl, fr, bl, br;
    IMU2 imu;
    boolean isFieldCentric = false;
    static UnaryOperator<Double> cubicScalingFunction = x -> Math.pow(x, 3);
    static UnaryOperator<Double> slowLinearScalingFunction = x -> x / 4;


    @Override
    public void initInternal() {
        fl = hardwareMap.get(DcMotorEx.class, "fL");
        fr = hardwareMap.get(DcMotorEx.class, "fR");
        bl = hardwareMap.get(DcMotorEx.class, "bL");
        br = hardwareMap.get(DcMotorEx.class, "bR");
        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setDirection(DcMotorSimple.Direction.REVERSE);
        imu = new IMU2(hardwareMap);
        gamepad1.SQUARE.onPress((e) -> isFieldCentric = !isFieldCentric);
        imu.zeroHeading();
        gamepad1.CIRCLE.onPress(e -> imu.zeroHeading());
        betterTelemetry.addItem(
                new SliderDisplayLine("Heading", imu::getHeading, LineItem.Color.AQUA)
                        .setMin(0)
                        .setScale(360)
        );
        betterTelemetry.addItem(
                new SliderDisplayLine("Strafe", () -> (double) gamepad1.LEFT_STICK_X.value(), LineItem.Color.BLUE)
                        .setMin(-1)
                        .setScale(1)
        );
        betterTelemetry.addItem(
                new SliderDisplayLine("Drive", () -> (double) gamepad1.LEFT_STICK_Y.value(), LineItem.Color.BLUE)
                        .setMin(-1)
                        .setScale(1)
        );
        betterTelemetry.addItem(
                new SliderDisplayLine("Turn", () -> (double) gamepad1.RIGHT_STICK_X.value(), LineItem.Color.BLUE)
                        .setMin(-1)
                        .setScale(1)
        );
    }

    @Override
    protected void loopInternal() {
        Vector v = new Vector(gamepad1.LEFT_STICK_X.value(), gamepad1.LEFT_STICK_Y.value());
        double turn = gamepad1.RIGHT_STICK_X.value();
        double angle = imu.getHeading();
        v = v.rotate(angle);
        v = v.withNewElement(turn);
        //UnaryOperator<Double> scalingFunction = cubicScalingFunction;
        //if(gamepad1.BUMPER_LEFT.value()) {
         UnaryOperator<Double>  scalingFunction = slowLinearScalingFunction;
        //}
        //v = v.applyLambda(scalingFunction);
        double denom = Math.min(v.getMaxAbs(),1);
        if (gamepad1.BUMPER_LEFT.value()){
            v = v.divideByScalar(denom * 2);
        } else v = v.divideByScalar(denom * 4);
        //vector order = [x y turn]
        v = v.multiplyByScalar(1-gamepad1.LEFT_TRIGGER.value());
        fl.setPower(v.aggregateWithCoefficients(1,1,1));
        bl.setPower(v.aggregateWithCoefficients(-1,1,1));
        fr.setPower(v.aggregateWithCoefficients(-1,1,-1));
        br.setPower(v.aggregateWithCoefficients(1,1,-1));
        betterTelemetry.addData("Brake", gamepad1.LEFT_TRIGGER);
    }
}
