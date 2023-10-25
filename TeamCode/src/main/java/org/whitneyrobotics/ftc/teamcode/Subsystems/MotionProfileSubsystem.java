package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.whitneyrobotics.ftc.teamcode.Libraries.Motion.MotionProfileTrapezoidal;
import org.whitneyrobotics.ftc.teamcode.Libraries.StateForge.StateForge;
import org.whitneyrobotics.ftc.teamcode.Libraries.StateForge.StateMachine;
import org.whitneyrobotics.ftc.teamcode.Libraries.Utilities.NanoStopwatch;

import java.util.function.Supplier;

public abstract class MotionProfileSubsystem {
    enum State {
        IDLE, ACCELERATING, CRUISING, DECELERATING
    }

    private boolean isBusy;

    public boolean isBusy(){
        return isBusy;
    }

    public void start(){
        isBusy = true;
        timeElapsed.reset();
    }

    public void stop(){
        isBusy = false;
        timeElapsed.reset();
    }

    protected NanoStopwatch timeElapsed = new NanoStopwatch();

    protected StateMachine<? extends State> stateMachine;
    protected static double NOMINAL_VOLTAGE = 12.0;
    protected MotionProfileTrapezoidal motionProfile;

    protected MotionProfileSubsystem(double v_max, double a_max){
        motionProfile = new MotionProfileTrapezoidal(v_max, a_max);
    }

    protected double calculateVoltageScalingFactor(){
        Supplier<Double> voltageProvider;
        if(RobotImpl.getInstance()!=null){
            voltageProvider = RobotImpl.getInstance().voltageSensor::getVoltage;
        } else voltageProvider = () -> 12d;
        return NOMINAL_VOLTAGE/voltageProvider.get();
    }
    private void update(){
        _update();
    };

    protected abstract void _update();
}
