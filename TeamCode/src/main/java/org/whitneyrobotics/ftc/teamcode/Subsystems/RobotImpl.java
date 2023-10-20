package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.whitneyrobotics.ftc.teamcode.Constants.Alliance;
import org.whitneyrobotics.ftc.teamcode.Roadrunner.drive.CenterstageMecanumDrive;
import static org.whitneyrobotics.ftc.teamcode.Subsystems.ColorSubsystem.Colors;

/**
 * Singleton instance for Robot Implementation including subsystems, sensors, and other hardware.
 */
public class RobotImpl {

    public Alliance alliance = Alliance.RED;
    private static RobotImpl instance = null;
    private VoltageSensor voltageSensor;
    boolean showMatchNotifs = false; //Controls notifications lights for endgame and match end notifications

    public static RobotImpl getInstance() {
        return instance;
    }

    public static RobotImpl getInstance(HardwareMap hardwareMap){
        if(instance == null){
            init(hardwareMap);
        }
        return instance;
    }

    //Call init in every autonomous, and in the event where instance does not exist
    public static void init(HardwareMap hardwareMap) {
        instance = new RobotImpl(hardwareMap);
    }

    public CenterstageMecanumDrive drive;
    public PrismSensor prismSensor;
    public ColorSubsystem colorSubsystem;

    private RobotImpl(HardwareMap hardwareMap) {
        drive = new CenterstageMecanumDrive(hardwareMap);
        prismSensor = new PrismSensor(hardwareMap);
        voltageSensor = hardwareMap.getAll(VoltageSensor.class).iterator().next();
        colorSubsystem = new ColorSubsystem(hardwareMap);
    }

    public void switchAlliance(){
        alliance = alliance == Alliance.RED ? Alliance.BLUE : Alliance.RED;
    }

    public void update(){
        prismSensor.update();
        colorSubsystem.update();
        drive.update();
        Colors status = Colors.OFF;
        if(drive.isBusy()){
            status = Colors.AUTO_RUNNING;
        }
        if (showMatchNotifs){
            status = Colors.NOTIFICATION;
        }
        //Check if linear slides are busy and set color to BUSY if true


        colorSubsystem.requestColor(status);
    }
}
