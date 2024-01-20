package org.whitneyrobotics.ftc.teamcode.Subsystems.Odometry;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.Libraries.Utilities.Functions;
import org.whitneyrobotics.ftc.teamcode.Subsystems.SubsystemIterative;

import java.util.Arrays;
import java.util.SortedMap;
import java.util.TreeMap;

@Config
public class DroneB implements SubsystemIterative {
    private Servo angle, fire;
    private double angleNumeric = 0.0;
    private double position = 0.0d;

    private int i = 1;

    public static String newPair = "";

    public static double firingPosition = 0.5d;

    //Regression constants for angles
    //See https://www.desmos.com/calculator/ow5xt1unvc
    public static double A = 0.840039;
    public static double B = -0.0203668;
    public static double C = 0.000301712;
    public static double D = -0.00000173691;

    public void fire(){
        firingPosition = 0.255;
    }

    public void retract(){
        setAngle(0.0d);
        firingPosition = 0.68;
        i = 1;
    }

    public static double regress(double angle){
        angle = Functions.clamp(angle, 0, 90);
        return A + B*angle + C*Math.pow(angle, 2) + D*Math.pow(angle, 3);
    }

    public void setAngle(double ang){
        double pos = regress(ang);
        angleNumeric = ang;
        position = pos;
    }

    public void nextDefinedAngle(){
        double angle = (double)anglePositions.keySet().toArray()[i];
        Double pos = anglePositions.get(angle);
        if(pos == null) return;
        i = (i+1) % anglePositions.size();
        position = pos;
        angleNumeric = angle;
    }

    public
    SortedMap<Double, Double> anglePositions = new TreeMap<>();
    public DroneB(HardwareMap hardwareMap){
        angle = hardwareMap.get(Servo.class, "angle");
        fire = hardwareMap.get(Servo.class, "drone");
        reset();
    }

    @Override
    public void init() {
        setAngle(0.0d);
        retract();
        update();
    }

    @Override
    public void update() {
        this.angle.setPosition(position);
        this.fire.setPosition(firingPosition);
    }

    public double getAngle(){
        return angleNumeric;
    }

    public double getPosition(){
        return position;
    }

    public double getFiringPosition(){
        return firingPosition;
    }

    /**
     * For testing only.
     */
    public void submitPosition(){
        submitPosition(()->{});
    }

    public void submitPosition(Runnable afterDone){
        String[] pairing = newPair.split(",");
        if(pairing.length != 2) return;
        pairing = Arrays.stream(pairing).map(String::trim).toArray(String[]::new);
        try{
            double angle = Double.parseDouble(pairing[0]);
            double position = Double.parseDouble(pairing[1]);
            anglePositions.put(angle, position);
            afterDone.run();
        } catch (NumberFormatException e){
        }
        newPair = "";
    }

    @Override
    public void reset() {
        anglePositions.clear();
        anglePositions.put(0.0, 0.84);
        anglePositions.put(30.0, 0.54);
        anglePositions.put(45.0, 0.46);
        anglePositions.put(50.0,0.439);
        anglePositions.put(60.0, 0.40);
    }
}
