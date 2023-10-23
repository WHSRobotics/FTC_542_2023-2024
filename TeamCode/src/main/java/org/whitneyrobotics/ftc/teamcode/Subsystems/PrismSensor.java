package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoControllerEx;

import org.whitneyrobotics.ftc.teamcode.Libraries.StateForge.StateForge;
import org.whitneyrobotics.ftc.teamcode.Libraries.StateForge.StateMachine;

public class PrismSensor {

    enum States {
        DETECTING_TOP,
        DETECTING_BOTTOM,
        IDLE
    }
    ServoControllerEx controller;
    private final int servoBottomIndex = 3, servoTopIndex = 5;
    private boolean requestImmediateDetection;

    RevColorSensorV3 sensor;
    public int[] colors = new int[2];
    public final StateMachine<States> stateMachine;
    public PrismSensor(HardwareMap hardwareMap){
        controller = hardwareMap.getAll(ServoControllerEx.class).get(0); //Gets the servo controller for the Control Hub
        sensor = hardwareMap.get(RevColorSensorV3.class, "PrismSensor");
        controller.setServoPosition(servoBottomIndex,1);
        controller.setServoPosition(servoTopIndex,1);
        controller.setServoPwmDisable(servoTopIndex);
        controller.setServoPwmDisable(servoBottomIndex);
        stateMachine = new StateForge.StateMachineBuilder<States>()
                .state(States.DETECTING_TOP)
                    .onEntry(() -> controller.setServoPwmEnable(servoTopIndex))
                    .onExit(() -> {
                        colors[1] = sensor.getNormalizedColors().toColor();
                        controller.setServoPwmDisable(servoTopIndex);
                    })
                    .timedTransitionLinear(0.5)
                .fin()
                .state(States.DETECTING_BOTTOM)
                    .onEntry(() -> controller.setServoPwmEnable(servoBottomIndex))
                    .onEntry(() -> {
                        colors[0] = sensor.getNormalizedColors().toColor();
                        controller.setServoPwmDisable(servoBottomIndex);
                    })
                    .timedTransitionLinear(0.5)
                .fin()
                .state(States.IDLE)
                    .timedTransitionLinear(5)
                    .transitionLinear(()->requestImmediateDetection)
                    .onExit(() -> requestImmediateDetection = false)
                .fin().build();
        stateMachine.start();
    }

    public void update(){
        stateMachine.update();
    }

    public String getState(){
        return stateMachine.getMachineState().name();
    }

    public double[] getPositions(){
        return new double[]{
                controller.getServoPosition(servoTopIndex),
                controller.getServoPosition(servoBottomIndex)
        };
    }

    public void requestImmediateDetection() {requestImmediateDetection = true;}

}