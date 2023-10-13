package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PWMOutput;
import com.qualcomm.robotcore.hardware.PWMOutputImplEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.Libraries.StateForge.StateForge;
import org.whitneyrobotics.ftc.teamcode.Libraries.StateForge.StateMachine;
import org.whitneyrobotics.ftc.teamcode.Libraries.StateForge.TransitionCondition;

public class PrismSensor {

    enum States {
        DETECTING_TOP,
        DETECTING_BOTTOM,
        IDLE
    }
    Servo bottomLED, topLED;
    boolean requestImmediateDetection;

    RevColorSensorV3 sensor;
    PWMOutputImplEx bottomLEDImpl, topLEDImpl;
    int[] colors = new int[2];
    public final StateMachine<States> stateMachine;
    public PrismSensor(HardwareMap hardwareMap){
        bottomLED = hardwareMap.get(Servo.class, "bottomLED");
        topLED = hardwareMap.get(Servo.class, "topLED");
        sensor = hardwareMap.get(RevColorSensorV3.class, "PrismSensor");
        stateMachine = StateForge.StateMachine()
                .state(States.DETECTING_TOP)
                    .onEntry(() -> topLED.setPosition(0.5))
                    .onExit(() -> {
                        colors[1] = sensor.getNormalizedColors().toColor();
                        topLED.setPosition(0);
                    })
                    .timedTransitionLinear(0.5)
                .fin()
                .state(States.DETECTING_BOTTOM)
                    .onEntry(() -> bottomLED.setPosition(0.5))
                    .onEntry(() -> {
                        colors[0] = sensor.getNormalizedColors().toColor();
                        bottomLED.setPosition(0);
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

}
