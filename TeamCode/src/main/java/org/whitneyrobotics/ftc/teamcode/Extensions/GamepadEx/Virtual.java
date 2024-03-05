package org.whitneyrobotics.ftc.teamcode.Extensions.GamepadEx;

//Creates a virtual copy of the button
public class Virtual extends Button {

    protected Button original;
    public Virtual(Button button) {
        super(() -> false);
        this.original = button;
        this.newStateSupplier = button.newStateSupplier;
    }
}
