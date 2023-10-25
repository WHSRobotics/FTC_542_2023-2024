package org.whitneyrobotics.ftc.teamcode.Libraries.Motion;

import java.util.concurrent.Callable;

public class EfficientMotionProfileTrapezoidal {

    private static double maxAccelGeneric = 5.5;
    private double maxAccelSpecific;

    private static double maxVelGeneric = 5.5;
    private double maxVelSpecific = 5.5;

    private double motionProfileAccel;
    private double motionProfileStable;
    private double motionProfileDecel;

    public EfficientMotionProfileTrapezoidal (double maxAccel, double maxVel){
        this.maxAccelSpecific = maxAccel;
        this.maxVelSpecific = maxVel;
    }

    public EfficientMotionProfileTrapezoidal (){
        maxAccelSpecific = maxAccelGeneric;
        maxVelSpecific = maxVelGeneric;
    }

    public void modifyGenericMaxAccel (double maxAccelGeneric){
        EfficientMotionProfileTrapezoidal.maxAccelGeneric = maxAccelGeneric;
    }

    public void modifySpecificMaxAccel (double maxAccelSpecific){
        this.maxAccelSpecific = maxAccelSpecific;
    }

    public void modifyGenericMaxVel (double maxVelGeneric){
        EfficientMotionProfileTrapezoidal.maxVelGeneric = maxVelGeneric;
    }

    public void modifySpecificMaxVel (double maxVelSpecific){
        this.maxVelSpecific = maxVelSpecific;
    }

    public double getMaxAccelGeneric(){
        return maxAccelGeneric;
    }

    public double getMaxAccelSpecific(){
        return maxAccelSpecific;
    }

    public double getMaxVelGeneric(){
        return maxVelGeneric;
    }

    public double getMaxVelSpecific(){
        return maxVelSpecific;
    }

    public void mapMotionProfiles (double position){
        if (position <= 2 * (maxAccelSpecific * Math.pow(maxVelSpecific / maxAccelSpecific, 2))){
            motionProfileAccel = Math.sqrt(position / maxAccelSpecific);
            motionProfileStable = 0;
            motionProfileDecel = Math.sqrt(position / maxAccelSpecific);
        } else {
            double a = 0;
        }
    }

}
