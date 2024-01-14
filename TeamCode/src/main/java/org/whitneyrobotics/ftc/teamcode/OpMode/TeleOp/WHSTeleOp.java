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
    Gate gate;

    Meet3Intake intake;
    private int intakeState = 0;
    private boolean unPressed = true;

    private double triggerValue = 0;
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
//        gamepad2.DPAD_DOWN.onPress(robot.claw::operateGateTele);
//        gamepad2.DPAD_UP.onPress(robot.claw::operateWristTele);
//        gamepad2.DPAD_LEFT.onPress(robot.claw::operateElbowTele);
//        gamepad2.DPAD_RIGHT.onPress(robot.claw::operateIntakeTele);
//        gamepad2.TRIANGLE.onPress(robot.claw::operateIntakeOutTele);
//        gamepad2.CIRCLE.onPress(robot.claw::operateIntakeHeightTele);
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

        gamepad2.TRIANGLE.onPress(robot.elbowWrist::toggle);
        gamepad2.CROSS.onPress(() -> robot.gate.update());
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
        //robot.elbowWrist.update();
        //robot.elbowWrist.update();
        gamepad2.CIRCLE.onPress(robot.intake::stackPosition);
        gamepad2.SQUARE.onPress(robot.intake::onePosition);
        if (gamepad2.RIGHT_TRIGGER.value() > 0) {
            robot.intake.setReversed(false);
            triggerValue = gamepad2.RIGHT_TRIGGER.value();
        }else if (gamepad2.LEFT_TRIGGER.value() > 0) {
            robot.intake.setReversed(true);
            triggerValue = gamepad2.LEFT_TRIGGER.value();
        }
        if (gamepad2.RIGHT_TRIGGER.value() <= 0 && gamepad2.LEFT_TRIGGER.value() <= 0){
            triggerValue = 0;
        }
        robot.intake.setRPM(Math.sqrt(triggerValue) * Meet3Intake.MAX_RPM);
        robot.elevator.inputPower(gamepad2.LEFT_STICK_Y.value());


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
    }
}
