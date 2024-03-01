package org.whitneyrobotics.ftc.teamcode.Extensions.GamepadEx;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SyntheticCaptureGroup extends Button {
    private ArrayList<Virtual> buttons = new ArrayList<>();
    private boolean stopPropagation;
    private Map<Button, Boolean> interactionMap, pressMap, releaseMap;

    SyntheticCaptureGroup(GamepadEx gamepad, Button... buttons) {
        this(true, gamepad, buttons);
    }
    public SyntheticCaptureGroup(boolean stopPropagation,GamepadEx gamepad, Button... buttons) {
        super(() -> false);
        this.stopPropagation = stopPropagation;
        for(Button button : buttons) {
            Virtual v = new Virtual(button);
            this.buttons.add(v);
            gamepad.virtuals.add(v);
        }
        resetAllMaps();
    }

    private void resetAllMaps(){
        interactionMap = new HashMap<>();
        pressMap = new HashMap<>();
        releaseMap = new HashMap<>();
        for(Button button : buttons){
            interactionMap.put(button, false);
            pressMap.put(button, false);
            releaseMap.put(button, false);
        }
    }

    @Override
    public void onInteraction(@NonNull Consumer<GamepadInteractionEvent> callback) {
        for (Virtual button : buttons) {
            button.onInteraction(() -> {
                interactionMap.put(button, true);
                button.interactionEventConsumer.accept(null);
                if(stopPropagation) {
                    button.blockInteraction = true;
                    button.original.blockRelease = true;
                }
            });
        }
        this.interactionEventConsumer = callback;
    }

    @Override
    public void onInteraction(VoidAction callback) {
        onInteraction(e -> callback.action());
    }

    @Override
    public void onPress(@NonNull Consumer<GamepadInteractionEvent> callback) {
        for (Virtual button : buttons) {
            button.onInteraction(() -> {
                pressMap.put(button, true);
                button.pressEventConsumer.accept(null);
                if(stopPropagation) {
                    button.blockPress = true;
                    button.original.blockRelease = true;
                }
            });
        }
    }

    @Override
    public void onPress(VoidAction callback) {
        super.onPress(callback);
    }

    @Override
    public void onRelease(@NonNull Consumer<GamepadInteractionEvent> callback) {
        for (Virtual button : buttons) {
            button.onInteraction(() -> {
                releaseMap.put(button, true);
                button.releaseEventConsumer.accept(null);
                if(stopPropagation) {
                    button.blockRelease = true;
                    button.original.blockRelease = true;
                }
            });
        }
    }

    @Override
    public void onRelease(VoidAction callback) {
        super.onRelease(callback);
    }

    @Override
    public void onButtonHold(@NonNull Consumer<GamepadInteractionEvent> callback) {
        throw new UnsupportedOperationException("SyntheticCaptureGroup does not support onButtonHold");
    }

    @Override
    public void onShortPress(@NonNull Consumer<GamepadInteractionEvent> callback) {
        throw new UnsupportedOperationException("SyntheticCaptureGroup does not support onShortPress");
    }

    @Override
    public void update() {
        if (interactionMap.values().stream().allMatch(val -> val)) {
            interactionEventConsumer.accept(new GamepadInteractionEvent(false, 0f, 0L, 0));
        }
        if(pressMap.values().stream().allMatch(val -> val)){
            pressEventConsumer.accept(new GamepadInteractionEvent(false, 0f, 0L, 0));
        }
        if(releaseMap.values().stream().allMatch(val -> val)){
            releaseEventConsumer.accept(new GamepadInteractionEvent(false, 0f, 0L, 0));
        }
        resetAllMaps();
    }
}
