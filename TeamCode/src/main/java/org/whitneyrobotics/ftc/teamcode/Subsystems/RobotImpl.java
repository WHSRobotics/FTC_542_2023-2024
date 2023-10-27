package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.whitneyrobotics.ftc.teamcode.Constants.Alliance;
import org.whitneyrobotics.ftc.teamcode.Roadrunner.drive.CenterstageMecanumDrive;
import static org.whitneyrobotics.ftc.teamcode.Subsystems.ColorSubsystem.Colors;
import org.whitneyrobotics.ftc.teamcode.Subsystems.WristServo;
import org.whitneyrobotics.ftc.teamcode.Subsystems.fullMotion;
import org.whitneyrobotics.ftc.teamcode.Subsystems.newLinearSlides;
import org.whitneyrobotics.ftc.teamcode.Subsystems.ClawServo;

/**
 * Singleton instance for Robot Implementation including subsystems, sensors, and other hardware.
 */
public class RobotImpl {

    public Alliance alliance = Alliance.RED;
    private static RobotImpl instance = null;
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

    public final CenterstageMecanumDrive drive;
    public final PrismSensor prismSensor;
    public final ColorSubsystem colorSubsystem;
    public final newLinearSlides slides;
    public final ElbowMotor elbowMotor;
    public final ClawServo clawServo;

    public final VoltageSensor voltageSensor;
    public final WristServo wristServo;


    private RobotImpl(HardwareMap hardwareMap) {
        drive = new CenterstageMecanumDrive(hardwareMap);
        prismSensor = new PrismSensor(hardwareMap);
        voltageSensor = hardwareMap.getAll(VoltageSensor.class).iterator().next();
        colorSubsystem = new ColorSubsystem(hardwareMap);
        slides = new newLinearSlides(hardwareMap);
        wristServo = new WristServo(hardwareMap);
        elbowMotor = new ElbowMotor(hardwareMap);
        clawServo = new ClawServo(hardwareMap);
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
