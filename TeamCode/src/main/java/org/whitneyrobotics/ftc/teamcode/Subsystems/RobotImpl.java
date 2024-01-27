package org.whitneyrobotics.ftc.teamcode.Subsystems;

import static org.whitneyrobotics.ftc.teamcode.Libraries.Utilities.UnitConversion.DistanceUnit.TILE_WIDTH;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.whitneyrobotics.ftc.teamcode.Constants.Alliance;
import org.whitneyrobotics.ftc.teamcode.Libraries.StateForge.StateMachine;
import org.whitneyrobotics.ftc.teamcode.OpMode.Autonomous.OpenCVRed;
import org.whitneyrobotics.ftc.teamcode.Roadrunner.drive.CenterstageMecanumDrive;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Auto.PurpleServo;
import org.whitneyrobotics.ftc.teamcode.Subsystems.ColorSubsystem.Colors;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Outtake.Elbow;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Outtake.Gate;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Outtake.Wrist;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Odometry.DroneB;
//import static org.whitneyrobotics.ftc.teamcode.Subsystems.ColorSubsystem.Colors;

/**
 * Singleton instance for Robot Implementation including subsystems, sensors, and other hardware.
 */
public class RobotImpl {

    public enum ClawStates {
        REST, RAISED
    }

    StateMachine<ClawStates> clawStatesStateMachine;

    public Alliance alliance = Alliance.RED;
    public static Pose2d poseMemory = new Pose2d(0,0,0);

    public static double heightMemory, angleMemory;

    //Must be updated in autonomous loop
    public static double slidesHeightMemory, wristAngleMemory = 0;
    private static RobotImpl instance = null;
    boolean showMatchNotifs = false; //Controls notifications lights for endgame and match end notifications

    public static RobotImpl getInstance() {
        return instance;
    }

    public static RobotImpl getInstance(HardwareMap hardwareMap){
        //if(instance == null){ Right now, robot is having a hard time reassigning motors. So we will force reinit and restore the state
            init(hardwareMap);
        //}
        return instance;
    }

    //Call init in every autonomous, and in the event where instance does not exist
    public static void init(HardwareMap hardwareMap) {
        instance = new RobotImpl(hardwareMap);
    }

    public final CenterstageMecanumDrive drive;
    public final ColorSubsystem colorSubsystem;
    public final VoltageSensor voltageSensor;

    public final Meet3Intake intake;

    public final ArmElevator elevator;
    public final DroneB drone;

    public final Gate gate;
    public final ElbowWristImpl elbowWrist;

    public final PurpleServo purpleAuto;

    public final HookAndWinch hookAndWinch;


    private RobotImpl(HardwareMap hardwareMap) {
        drive = new CenterstageMecanumDrive(hardwareMap);
        //prismSensor = new PrismSensor(hardwareMap);
        voltageSensor = hardwareMap.getAll(VoltageSensor.class).iterator().next();
        colorSubsystem = new ColorSubsystem(hardwareMap);
        elevator = new ArmElevator(hardwareMap);
        drone = new DroneB(hardwareMap);
        elbowWrist = new ElbowWristImpl(hardwareMap);
        intake = new Meet3Intake(hardwareMap);
        gate = new Gate(hardwareMap);
        purpleAuto = new PurpleServo(hardwareMap);
        hookAndWinch = new HookAndWinch(hardwareMap);
//        elbow = new Elbow(hardwareMap);
        //claw = new JeffClaw(hardwareMap);
        //clawStatesStateMachine.start();
    }

    public void switchAlliance(){
        alliance = alliance == Alliance.RED ? Alliance.BLUE : Alliance.RED;
    }

    public void autonomousInit(){
        elevator.resetEncoders();
    }

    public void teleOpInit(){ //Restore states of motors from memory
        drive.setPoseEstimate(RobotImpl.poseMemory);
        elevator.setCalibrationOffset(slidesHeightMemory);
        elevator.setCalibrationOffset(heightMemory);
        drone.init();
        hookAndWinch.init();
    }
    public void update(){
        elevator.update();
        //clawStatesStateMachine.update();//Will automatically move the arm depending on elevator height
        drive.update();
        drone.update();
//        wrist.run();
//        elbow.run();
        intake.update();
        gate.run();
        elbowWrist.update();
        purpleAuto.update();
        hookAndWinch.update();

        Colors status = Colors.OFF;
        if(drive.isBusy()){
            status = Colors.AUTO_RUNNING;
        }
        if (showMatchNotifs){
            status = Colors.NOTIFICATION;
        }
        
        if (Math.abs(drive.getLocalizer().getPoseEstimate().getX()-TILE_WIDTH.toInches(-0.5)) <= TILE_WIDTH.toInches((double)2/3)){
            status = Colors.BUSY;
            /*if(elevator.isBusy() || elevator.getPosition()>4){ //Auto retraction - UNTESTED
                elevator.cancel();
                elevator.setTargetPosition(ArmElevator.Target.RETRACT);
            }*/
        }
        //Check if linear slides are busy and set color to BUSY if true
        colorSubsystem.requestColor(status);
        colorSubsystem.update();
    }

}
