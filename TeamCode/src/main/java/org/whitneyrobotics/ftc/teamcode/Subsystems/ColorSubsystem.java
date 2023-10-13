package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whitneyrobotics.ftc.teamcode.Extensions.GamepadEx.GamepadEx;
import org.whitneyrobotics.ftc.teamcode.Libraries.StateForge.StateForge;
import org.whitneyrobotics.ftc.teamcode.Libraries.StateForge.StateMachine;

import java.util.Arrays;

public class ColorSubsystem {
    private double refreshRateHz = 1.0d; //Hz
    private Gamepad[] gamepads = new Gamepad[0];
    private Colors currentStatus = Colors.OFF;
    private int[] RGBValues = new int[3];
    private StateMachine<Phases> stateMachine;

    private enum Phases {ON, OFF}
    enum Colors {
        ERROR(false),
        ERROR_FLASHING(true),
        NOTIFICATION(true),
        WHITE(false),
        GREEN_PIXEL(false),
        PURPLE_PIXEL(false),
        YELLOW_PIXEL(false),
        BUSY(false),
        OFF(false);
        private boolean blink;
        Colors(boolean blink){this.blink = blink;}
    }
    private RevBlinkinLedDriver ledDriver;
    public ColorSubsystem(HardwareMap hardwareMap){
        this.ledDriver = hardwareMap.getAll(RevBlinkinLedDriver.class).iterator().next(); // Get the first REV Blinkin defined
        stateMachine = StateForge.StateMachine()
                .state(Phases.ON)
                    .onEntry(() -> setGamepadColors())
                    .timedTransitionLinear(0.5)
                    .fin()
                .state(Phases.OFF)
                    .onEntry(() -> {if (currentStatus.blink) setGamepadColors();})
                    .transition(() -> !currentStatus.blink, Phases.ON)
                    .timedTransitionLinear(0.5)
                .fin().build();
        stateMachine.start();
    }

    public void bindGamepads(Gamepad... gamepads){
        this.gamepads = gamepads;
    }

    public void bindGamepads(GamepadEx... gamepads){
        bindGamepads((Gamepad[]) Arrays.stream(gamepads).map(g -> g.getEncapsulatedGamepad()).toArray());
    }

    public void setRefreshRate(double rate){
        this.refreshRateHz = rate;
    }

    public void requestColor(Colors c){
        this.currentStatus = c;
        switch(currentStatus){
            case ERROR:
                ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
                RGBValues = new int[] {235, 64, 52};
                break;
            case ERROR_FLASHING:
                ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.BREATH_RED);
                RGBValues = new int[] {235, 64, 52};
                break;
            case NOTIFICATION:
                ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_BLUE);
                RGBValues = new int[] {15, 134, 252};
                break;
            case WHITE:
                ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.WHITE);
                RGBValues = new int[] {255,255,255};
                break;
            case GREEN_PIXEL:
                ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
                RGBValues = new int[] {85, 174, 53};
                break;
            case PURPLE_PIXEL:
                ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.VIOLET);
                RGBValues = new int[] {161, 140, 213};
                break;
            case YELLOW_PIXEL:
                ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.YELLOW);
                RGBValues = new int[] {255, 195, 32};
                break;
            case BUSY:
                ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.CP1_LIGHT_CHASE);
                RGBValues = new int[] {255, 147, 32};
                break;
            default: //Unimplemented colors will default to black
                ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLACK);
                RGBValues = new int[3];
        }
    }

    public void update(){
        stateMachine.update();
    }

    private void setGamepadColors(){
        for (Gamepad g : gamepads){
            if(g == null) continue;
            g.setLedColor(RGBValues[0], RGBValues[1], RGBValues[2], (int)(refreshRateHz/2*1000));
        }
    }
}
