package org.whitneyrobotics.ftc.teamcode.Libraries.Motion;

import org.whitneyrobotics.ftc.teamcode.Libraries.Utilities.NanoStopwatch;

public class MotionProfileTrapezoidal {
    private double maxAccel, maxVelocity;

    private double t1,t2,tf;

    private double goalPoint;

    public MotionProfileTrapezoidal(double maxAccel, double maxVelocity){
        this.maxAccel = maxAccel;
        this.maxVelocity = maxVelocity;
    }

    public void setGoal(double goal){
        this.goalPoint = goal;
        //v = at
        //t = a/v
        if(maxVelocity/maxAccel*maxVelocity>=goal){

        }
    }

    public double getMaxAccel() {
        return maxAccel;
    }

    public void setMaxAccel(double maxAccel) {
        this.maxAccel = maxAccel;
    }

    public double getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(double maxVelocity) {
        this.maxVelocity = maxVelocity;
    }
}
