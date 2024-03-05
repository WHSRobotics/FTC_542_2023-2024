package org.whitneyrobotics.ftc.teamcode.Extensions.GamepadEx;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.whitneyrobotics.ftc.teamcode.Tests.SoftwareTests.TouchpadTest;

import java.util.ArrayList;
import java.util.function.Consumer;

@RequiresApi(api = Build.VERSION_CODES.N)
public class GamepadEx {
    public enum InteractionType {
        GENERIC, PRESS, RELEASE
    }

    private final ArrayList<SyntheticCaptureGroup>  syntheticCaptureGroups = new ArrayList<>();
    final ArrayList<Virtual> virtuals = new ArrayList<>();

    private Gamepad gamepad;

    public final Button A = new Button(() -> gamepad.a);
    public final Button B = new Button(() -> gamepad.b);
    public final Button X = new Button(() -> gamepad.x);
    public final Button Y = new Button(() -> gamepad.y);

    public final Button CROSS = A;
    public final Button CIRCLE = B;
    public final Button SQUARE = X;
    public final Button TRIANGLE = Y;

    public final Button LEFT_STICK_DOWN = new Button(() -> gamepad.left_stick_button);
    public final Button RIGHT_STICK_DOWN = new Button(() -> gamepad.right_stick_button);

    public final Button DPAD_UP = new Button(() -> gamepad.dpad_up);
    public final Button DPAD_RIGHT = new Button(() -> gamepad.dpad_right);
    public final Button DPAD_DOWN = new Button(() -> gamepad.dpad_down);
    public final Button DPAD_LEFT = new Button(() -> gamepad.dpad_left);

    public final Button START = new Button(() -> gamepad.start);
    public final Button SELECT = new Button(() -> gamepad.options);

    public final Button HOME = new Button(() -> gamepad.ps);
    public final Button PSButton = HOME;

    public final Button BACK = new Button(() -> gamepad.back);
    public final Button SHARE = BACK;

    public final Button BUMPER_LEFT = new Button(() -> gamepad.left_bumper);
    public final Button BUMPER_RIGHT = new Button(() -> gamepad.right_bumper);

    public final Button TOUCHPAD = new Button(() -> gamepad.touchpad);



    @GamepadScalarHardware.LimitSensitivity
    public final GamepadScalarHardware LEFT_STICK_X = new GamepadScalarHardware(() -> gamepad.left_stick_x);
    @GamepadScalarHardware.LimitSensitivity
    public final GamepadScalarHardware LEFT_STICK_Y = new GamepadScalarHardware(() -> gamepad.left_stick_y, true);
    @GamepadScalarHardware.LimitSensitivity
    public final GamepadScalarHardware RIGHT_STICK_X = new GamepadScalarHardware(() -> gamepad.right_stick_x);
    @GamepadScalarHardware.LimitSensitivity
    public final GamepadScalarHardware RIGHT_STICK_Y = new GamepadScalarHardware(() -> gamepad.right_stick_y,true);

    public final GamepadScalarHardware LEFT_TRIGGER = new GamepadScalarHardware(()-> gamepad.left_trigger);
    public final GamepadScalarHardware RIGHT_TRIGGER = new GamepadScalarHardware(() -> gamepad.right_trigger);

    public final GamepadHardware[] inputs = {A,B,X,Y,LEFT_STICK_DOWN,DPAD_UP,DPAD_RIGHT,DPAD_DOWN,DPAD_LEFT, START,SELECT,HOME,BUMPER_LEFT,BUMPER_RIGHT, LEFT_STICK_X, LEFT_STICK_Y, RIGHT_STICK_X, RIGHT_STICK_Y, LEFT_TRIGGER, RIGHT_TRIGGER, TOUCHPAD};

    public final boolean hasError;

    public GamepadEx(Gamepad gamepad){
        this.gamepad = gamepad;
        if(this.gamepad != null) this.gamepad.rumble(250);
        hasError = this.gamepad == null;
    }

    public void addGenericEventListener(GamepadHardware hardware, Consumer<GamepadInteractionEvent> handler){
        hardware.onInteraction(handler);
    }

    public void removeAllEventListeners(){
        for(GamepadHardware hardware : inputs){
            hardware.disconnectAllHandlers();
        }
    }

    public SyntheticCaptureGroup addSyntheticCaptureGroup(Button... buttons){
        SyntheticCaptureGroup g = new SyntheticCaptureGroup(this, buttons);
        syntheticCaptureGroups.add(g);
        return g;
    }

    public SyntheticCaptureGroup addSyntheticCaptureGroup(boolean stopPropagation, Button... buttons){
        SyntheticCaptureGroup g = new SyntheticCaptureGroup(stopPropagation, this, buttons);
        syntheticCaptureGroups.add(g);
        return g;
    }

    public Gamepad getEncapsulatedGamepad(){
        return gamepad;
    }

    public void addButtonEventListener(Button b, InteractionType interactionType, Consumer<GamepadInteractionEvent> handler){
        switch (interactionType){
            case PRESS:
                b.onPress(handler);
                break;
            case RELEASE:
                b.onRelease(handler);
                break;
            default:
                b.onInteraction(handler);
                break;
        }
    }

    public void update(){
        if(gamepad == null) return;
        virtuals.forEach(Virtual::update);
        syntheticCaptureGroups.forEach(SyntheticCaptureGroup::update);
        A.update();
        B.update();
        X.update();
        Y.update();
        LEFT_STICK_DOWN.update();
        RIGHT_STICK_DOWN.update();
        DPAD_UP.update();
        DPAD_RIGHT.update();
        DPAD_DOWN.update();
        DPAD_LEFT.update();
        START.update();
        SELECT.update();
        HOME.update();
        BUMPER_LEFT.update();
        BUMPER_RIGHT.update();
        LEFT_STICK_X.update();
        LEFT_STICK_Y.update();
        RIGHT_STICK_X.update();
        RIGHT_STICK_Y.update();
        LEFT_TRIGGER.update();
        RIGHT_TRIGGER.update();
        BACK.update();
        TOUCHPAD.update();
    }

}
