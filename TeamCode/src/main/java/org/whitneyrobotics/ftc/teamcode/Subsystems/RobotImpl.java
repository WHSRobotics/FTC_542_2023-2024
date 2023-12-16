package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.whitneyrobotics.ftc.teamcode.Constants.Alliance;
import org.whitneyrobotics.ftc.teamcode.Libraries.StateForge.StateMachine;
import org.whitneyrobotics.ftc.teamcode.Roadrunner.drive.CenterstageMecanumDrive;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Odometry.ArmElevator;

import static org.whitneyrobotics.ftc.teamcode.Libraries.Utilities.UnitConversion.DistanceUnit.TILE_WIDTH;
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
//    public final PrismSensor prismSensor;
//    public final ColorSubsystem colorSubsystem;
    public final VoltageSensor voltageSensor;

    public final ArmElevator elevator;
//    public final ElbowMotor elbow;
    public final Drone drone;
    public final JeffClaw claw;


    private RobotImpl(HardwareMap hardwareMap) {
        drive = new CenterstageMecanumDrive(hardwareMap);
//        prismSensor = new PrismSensor(hardwareMap);
        voltageSensor = hardwareMap.getAll(VoltageSensor.class).iterator().next();
//        colorSubsystem = new ColorSubsystem(hardwareMap);
        elevator = new ArmElevator(hardwareMap);
        drone = new Drone(hardwareMap);
        claw = new JeffClaw(hardwareMap);
        //clawStatesStateMachine.start();
//        elbow = new ElbowMotor(hardwareMap);
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
    }

    public void update(){
        elevator.update();
        //clawStatesStateMachine.update();//Will automatically move the arm depending on elevator height
        drive.update();
        drone.update();
//        Colors status = Colors.OFF;
//        if(drive.isBusy()){
//            status = Colors.AUTO_RUNNING;
//        }
//        if (showMatchNotifs){
//            status = Colors.NOTIFICATION;
//        }
//        if (Math.abs(drive.getLocalizer().getPoseEstimate().getX()-TILE_WIDTH.toInches(-0.5)) <= TILE_WIDTH.toInches((double)2/3)){
//            status = Colors.BUSY;
//            /*if(elevator.isBusy() || elevator.getPosition()>4){ //Auto retraction - UNTESTED
//                elevator.cancel();
//                elevator.setTargetPosition(ArmElevator.Target.RETRACT);
//            }*/
//        }
//        //Check if linear slides are busy and set color to BUSY if true
//        colorSubsystem.requestColor(status);
    }

}
