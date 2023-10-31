package org.whitneyrobotics.ftc.teamcode.OpMode.TeleOp;

import static org.whitneyrobotics.ftc.teamcode.Extensions.GamepadEx.RumbleEffects.endgame;
import static org.whitneyrobotics.ftc.teamcode.Extensions.GamepadEx.RumbleEffects.matchEnd;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.robot.Robot;

import org.whitneyrobotics.ftc.teamcode.Constants.Alliance;
import org.whitneyrobotics.ftc.teamcode.Extensions.OpModeEx.OpModeEx;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.LineItem;
import org.whitneyrobotics.ftc.teamcode.Libraries.Utilities.Functions;
import org.whitneyrobotics.ftc.teamcode.Subsystems.RobotImpl;

import java.util.function.UnaryOperator;

@TeleOp(name="Centerstage TeleOp", group="A")
public class WHSTeleOp extends OpModeEx {

    boolean fieldCentric = true;
    RobotImpl robot;
    private final UnaryOperator<Float> scalingFunctionDefault = x -> (float)Math.pow(x, 3);

    @Override
    public void initInternal() {
        robot = RobotImpl.getInstance(hardwareMap);
        robot.colorSubsystem.bindGamepads(gamepad1, gamepad2);
        robot.drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        dashboardTelemetry.setMsTransmissionInterval(25);
        telemetryPro.useDashboardTelemetry(dashboardTelemetry);
        gamepad1.SQUARE.onPress(robot::switchAlliance);
        gamepad1.START.onPress(() -> {
            Pose2d previousPosition = robot.drive.getPoseEstimate();
            robot.drive.setPoseEstimate(new Pose2d(
                    previousPosition.getX(),
                    previousPosition.getY(),
                    0 + (robot.alliance == Alliance.RED ? Math.PI/2 : -Math.PI/2)
            ));
        });
        //gamepad1.CROSS.onPress(robot.droneLauncher::toggleState);
        gamepad1.BUMPER_RIGHT.onPress(() -> fieldCentric = !fieldCentric);
        robot.teleOpInit();
        setupNotifications();
    }

    void setupNotifications(){
        addTemporalCallback(resolve -> {
            //playSound("endgame",100f);
            gamepad1.getEncapsulatedGamepad().runRumbleEffect(endgame);
            gamepad2.getEncapsulatedGamepad().runRumbleEffect(endgame);
            telemetryPro.addLine("Endgame approaching soon!", LineItem.Color.ROBOTICS, LineItem.RichTextFormat.BOLD).persistent();
            resolve.accept(!gamepad1.getEncapsulatedGamepad().isRumbling() && gamepad2.getEncapsulatedGamepad().isRumbling());
        }, 85000);

        addTemporalCallback(resolve -> {
            telemetryPro.removeLineByCaption("Endgame approaching soon!");
            resolve.accept(true);
        },90000);

        addTemporalCallback(resolve -> {
            //playSound("matchend",100f);
            gamepad1.getEncapsulatedGamepad().runRumbleEffect(matchEnd);
            gamepad2.getEncapsulatedGamepad().runRumbleEffect(matchEnd);
            telemetryPro.addLine("Match ends in 5 seconds!", LineItem.Color.FUCHSIA, LineItem.RichTextFormat.BOLD).persistent();
            resolve.accept(!gamepad1.getEncapsulatedGamepad().isRumbling() && gamepad2.getEncapsulatedGamepad().isRumbling());
        }, 113000);

        addTemporalCallback(resolve -> {
            //playSound("slay",100f);
            telemetryPro.removeLineByCaption("Match ends in 5 seconds!");
            telemetryPro.addLine("Match has ended.", LineItem.Color.LIME, LineItem.RichTextFormat.ITALICS).persistent();
            resolve.accept(true);
        },120000);
    }

    @Override
    protected void loopInternal() {
        float brakePower = gamepad1.LEFT_TRIGGER.value();
        UnaryOperator<Float> scaling = scalingFunctionDefault;
        if(gamepad1.BUMPER_LEFT.value()) scaling = x -> x/2;
        if (!robot.drive.isBusy()) robot.drive.setWeightedDrivePower(
                Functions.rotateVectorCounterclockwise(new Pose2d(
                        scaling.apply(gamepad1.LEFT_STICK_Y.value()),
                        scaling.apply(-gamepad1.LEFT_STICK_X.value()),
                        scaling.apply(-gamepad1.RIGHT_STICK_X.value())
                ).times(1-brakePower), (fieldCentric ? -robot.drive.getPoseEstimate().getHeading() + /*(robot.alliance == Alliance.BLUE ? Math.PI/2 : -Math.PI/2)*/Math.PI/2 : 0))
        );
        robot.update();
        if(fieldCentric) telemetryPro.addLine("FIELD CENTRIC ENABLED", LineItem.Color.YELLOW, LineItem.RichTextFormat.BOLD);
        telemetryPro.addData("brake", brakePower);
    }
}
