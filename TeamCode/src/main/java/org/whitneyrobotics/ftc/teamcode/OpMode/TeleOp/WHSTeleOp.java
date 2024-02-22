package org.whitneyrobotics.ftc.teamcode.OpMode.TeleOp;

import static org.whitneyrobotics.ftc.teamcode.Extensions.GamepadEx.RumbleEffects.endgame;
import static org.whitneyrobotics.ftc.teamcode.Extensions.GamepadEx.RumbleEffects.matchEnd;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.whitneyrobotics.ftc.teamcode.Constants.Alliance;
import org.whitneyrobotics.ftc.teamcode.Extensions.OpModeEx.OpModeEx;
import org.whitneyrobotics.ftc.teamcode.Extensions.TelemetryPro.LineItem;
import org.whitneyrobotics.ftc.teamcode.Libraries.Utilities.Functions;
import org.whitneyrobotics.ftc.teamcode.Subsystems.ArmElevator;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Auto.PurpleServo;
import org.whitneyrobotics.ftc.teamcode.Subsystems.ColorSubsystem;
import org.whitneyrobotics.ftc.teamcode.Subsystems.ElbowWristImpl;
import org.whitneyrobotics.ftc.teamcode.Subsystems.RobotImpl;

import org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Intake;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Outtake.Elbow;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Outtake.Gate;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Outtake.Wrist;

import java.util.function.UnaryOperator;

@TeleOp(name="Centerstage TeleOp", group="A")
public class WHSTeleOp extends OpModeEx {

    boolean fieldCentric = true;
    RobotImpl robot;
    public boolean change = true;
    private final UnaryOperator<Float> scalingFunctionDefault = x -> (float)Math.pow(x, 3);

    ElbowWristImpl elbowWrist;

    Meet3Intake intake;
    private int intakeState = 0;
    private boolean unPressed = true;
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
                    robot.alliance.headingAngle
            ));
        });
        //gamepad1.CROSS.onPress(robot.droneLauncher::toggleState);
        gamepad1.BUMPER_RIGHT.onPress(() -> fieldCentric = !fieldCentric);
        robot.teleOpInit();
        setupNotifications();
        //gamepad2.SQUARE.onPress(e -> robot.elevator.setTargetPosition(ArmElevator.Target.RETRACT));
        //gamepad2.CROSS.onPress(e -> robot.elevator.setTargetPosition(ArmElevator.Target.ONE));
        //gamepad2.BUMPER_LEFT.onPress(robot.drone::fire);
        //gamepad2.CIRCLE.onPress(robot.intake::onePosition);
//        gamepad2.SQUARE.onPress(robot.intake::stackPosition);
//        gamepad1.TRIANGLE.onPress(() -> robot.elbow.update());
//        gamepad2.CIRCLE.onPress(robot.wrist::update);
//        gamepad2.SQUARE.onPress(robot.gate::update);
//        gamepad2.DPAD_UP.onPress(e -> robot.elevator.setTargetPosition(ArmElevator.Target.THREE));
//        gamepad2.DPAD_RIGHT.onPress((e -> robot.elevator.setTargetPosition(ArmElevator.Target.TWO)));
//        gamepad2.DPAD_DOWN.onPress(e -> robot.elevator.setTargetPosition(ArmElevator.Target.RETRACT));
//        gamepad2.DPAD_LEFT.onPress(e -> robot.elevator.setTargetPosition(ArmElevator.Target.ONE));
//        gamepad2.BUMPER_RIGHT.onPress(robot.intake::update);

        gamepad2.DPAD_UP.onPress(() -> {
            robot.hookAndWinch.toggleHook();
            robot.elbowWrist.endgameFlipBucket();
        });
        gamepad2.DPAD_DOWN.onPress(() -> robot.gate.update());

        gamepad1.CIRCLE.onPress(robot.drone::nextDefinedAngle);
        gamepad1.CROSS.onPress(robot.drone::quickPrep);

        gamepad1.DPAD_DOWN.onPress(robot.intake::onePosition);
        gamepad1.DPAD_RIGHT.onPress(robot.intake::stackPosition);
        gamepad1.DPAD_UP.onPress(robot.intake::raisedPosition);
        gamepad1.DPAD_LEFT.onPress(robot.drone::fire);


        gamepad1.TOUCHPAD.onPress(robot.drone::reset);
        gamepad1.SHARE.onPress(robot.drone::init);


        gamepad2.CROSS.onPress(e -> robot.elevator.setTargetPosition(ArmElevator.Target.RETRACT));
        gamepad2.SQUARE.onPress(e -> robot.elevator.setTargetPosition(ArmElevator.Target.ONE));
        gamepad2.TRIANGLE.onPress(e -> robot.elevator.setTargetPosition(ArmElevator.Target.TWO));
        gamepad2.CIRCLE.onPress(e -> robot.elevator.setTargetPosition(ArmElevator.Target.THREE));
        gamepad2.BUMPER_RIGHT.onPress(robot.elbowWrist::toggle);
        gamepad2.START.onPress(robot.elevator::resetEncoders);
        gamepad2.SELECT.onPress(robot.hookAndWinch::reset);
        gamepad2.BUMPER_LEFT.onPress(robot.elevator::slowModeOn);
        gamepad2.BUMPER_LEFT.onRelease(robot.elevator::slowModeOff);
        gamepad2.DPAD_RIGHT.onPress(() -> {
            robot.hookAndWinch.toggleHook();
            robot.elbowWrist.endgameFlipBucket();
        });
        gamepad2.DPAD_LEFT.onPress(()->{
            robot.purpleAuto.setState(PurpleServo.PurplePositions.CLOSED);
            robot.purpleAuto.update();
        });
        robot.intake.onePosition();

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
        robot.update();
        robot.intake.setRPM(Math.sqrt(gamepad1.RIGHT_TRIGGER.value()) * Meet3Intake.MAX_RPM);
        robot.intake.setReversed(gamepad1.TRIANGLE.value());
        robot.elevator.inputPower(gamepad2.LEFT_STICK_Y.value());
        if (robot.hookAndWinch.hookReleased()) robot.hookAndWinch.setPower(gamepad2.RIGHT_STICK_Y.value());

        if(gamepad1.RIGHT_STICK_DOWN.value()){
            if(gamepad1.LEFT_STICK_DOWN.value()){
                robot.colorSubsystem.requestColor(ColorSubsystem.Colors.PURPLE_PIXEL);
            } else {
                robot.colorSubsystem.requestColor(ColorSubsystem.Colors.GREEN_PIXEL);
            }
        } else if (gamepad1.LEFT_STICK_DOWN.value()) robot.colorSubsystem.requestColor(ColorSubsystem.Colors.YELLOW_PIXEL);

        float brakePower = gamepad1.LEFT_TRIGGER.value();
        UnaryOperator<Float> scaling = scalingFunctionDefault;
        if(gamepad1.BUMPER_LEFT.value()) scaling = x -> x/2;
        if (!robot.drive.isBusy()) robot.drive.setWeightedDrivePower(
                Functions.rotateVectorCounterclockwise(new Pose2d(
                        scaling.apply(gamepad1.LEFT_STICK_Y.value()),
                        scaling.apply(-gamepad1.LEFT_STICK_X.value()),
                        scaling.apply(-gamepad1.RIGHT_STICK_X.value())
                ).times(1-brakePower), (fieldCentric ? -robot.drive.getPoseEstimate().getHeading()+robot.alliance.headingAngle : 0))
        );

        if(fieldCentric) telemetryPro.addLine("FIELD CENTRIC ENABLED", LineItem.Color.YELLOW, LineItem.RichTextFormat.BOLD);
        telemetryPro.addData("brake", brakePower);
        telemetryPro.addData("angle", Math.toDegrees(robot.drive.getPoseEstimate().getHeading()));
        telemetryPro.addData("Slides height", robot.elevator.getPosition());
        telemetryPro.addData("Slides target", robot.elevator.getState());
        telemetryPro.addData("Gate open",robot.gate.currentState, (robot.gate.currentState == Gate.GatePositions.OPEN || robot.gate.currentState == null ? LineItem.Color.GREEN : LineItem.Color.RED));
        telemetryPro.addData("Lifting height", robot.hookAndWinch.getDistanceTravelled());
        telemetryPro.addData("Drone Angle", robot.drone.getAngle());
    }
}
