package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.acmerobotics.roadrunner.util.NanoClock;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whitneyrobotics.ftc.teamcode.Extensions.GamepadEx.GamepadEx;
import org.whitneyrobotics.ftc.teamcode.Libraries.StateForge.StateForge;
import org.whitneyrobotics.ftc.teamcode.Libraries.StateForge.StateMachine;
import org.whitneyrobotics.ftc.teamcode.Libraries.Utilities.NanoStopwatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColorSubsystem {
    //Ineffective as of StateForge 0.0.1
    private double refreshRateHz = 2.0d; //Hz
    private Gamepad[] gamepads = new Gamepad[0];
    private Colors currentStatus = Colors.OFF;
    private int[] RGBValues = new int[3];
    private StateMachine<Phases> stateMachine;

    private NanoStopwatch clock = new NanoStopwatch();

    private enum Phases {ON, OFF}
    public enum Colors {
        RED(false),
        BLUE(false),
        ERROR_FLASHING(true),
        NOTIFICATION(true),
        WHITE(false),
        GREEN_PIXEL(false),
        PURPLE_PIXEL(false),
        YELLOW_PIXEL(false),
        BUSY(true),
        OFF(false),
        AUTO_RUNNING(true);
        private boolean blink;
        Colors(boolean blink){this.blink = blink;}
    }
    private RevBlinkinLedDriver ledDriver;
    public ColorSubsystem(HardwareMap hardwareMap){
        this.ledDriver = hardwareMap.getAll(RevBlinkinLedDriver.class).iterator().next(); // Get the first REV Blinkin defined
        stateMachine = StateForge.StateMachine()
                .state(Phases.ON)
                    .onEntry(() -> {
                        setGamepadColors();
                    })
                    .timedTransitionLinear(1/(refreshRateHz*2))
                    .fin()
                .state(Phases.OFF)
                    .onEntry(() -> {
                        if(currentStatus.blink) disableGamepadColors();
                    })
                .   timedTransitionLinear(1/(refreshRateHz*2))
                .fin().build();
        requestColor(Colors.OFF);
        stateMachine.start();
        setGamepadColors();
    }

    public void bindGamepads(Gamepad... gamepads){
        this.gamepads = gamepads;
    }

    public void bindGamepads(GamepadEx... gamepads){
        List<Gamepad> gps = new ArrayList<>();
        for (GamepadEx g : gamepads) gps.add(g.getEncapsulatedGamepad());
        bindGamepads(gps.toArray(new Gamepad[gps.size()]));
    }

    public void setRefreshRate(double rate){
        this.refreshRateHz = rate;
    }

    public void requestColor(Colors c){
        this.currentStatus = c;
        switch(currentStatus){
            case RED:
                ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
                RGBValues = new int[] {255, 0, 0};
                break;
            case BLUE:
                ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);
                RGBValues = new int[] {0, 0, 255};
                break;
            case ERROR_FLASHING:
                ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.BREATH_RED);
                RGBValues = new int[] {255, 0, 0};
                break;
            case NOTIFICATION:
                ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_BLUE);
                RGBValues = new int[] {0, 255, 255};
                break;
            case WHITE:
                ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.WHITE);
                RGBValues = new int[] {255,255,255};
                break;
            case GREEN_PIXEL:
                ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
                RGBValues = new int[] {0, 255, 0};
                break;
            case PURPLE_PIXEL:
                ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.VIOLET);
                RGBValues = new int[] {255, 0, 255};
                break;
            case YELLOW_PIXEL:
                ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.YELLOW);
                RGBValues = new int[] {255, 255, 0};
                break;
            case BUSY:
                ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.CP1_LIGHT_CHASE);
                RGBValues = new int[] {255, 255, 0};
                break;
            case AUTO_RUNNING:
                ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.CP2_SHOT);
                RGBValues = new int[] {0, 255, 255};
                break;
            default: //Unimplemented colors will default to black
                ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLACK);
                RGBValues = new int[3];
        }
        setGamepadColors();
    }

    public void update(){
        stateMachine.update();
    }

    private void setGamepadColors(){
        for (Gamepad g : gamepads){
            if(g == null) continue;
            g.setLedColor(RGBValues[0], RGBValues[1], RGBValues[2], -1);
        }
    }

    public int[] getRGBValues(){
        return RGBValues;
    }

    public void disableGamepadColors(){
        for (Gamepad g : gamepads){
            if (g==null) continue;
            g.setLedColor(0,0,0,-1);
        }
    }

    public String currentPhase(){
        return stateMachine.getMachineState().name();
    }
}
