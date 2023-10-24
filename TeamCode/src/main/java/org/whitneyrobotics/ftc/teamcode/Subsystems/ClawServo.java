package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.ServoControllerEx;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.ServoConfigurationType;

public class ClawServo extends ServoImplEx {
    ClawServo(ServoControllerEx controller, int portNumber, ServoConfigurationType servoType){
        super(controller,portNumber, servoType);
    }
    public static void singlePixelDrop(ClawServo ClawServoInstance) {
        ClawServoInstance.setPosition(0.5); //Once we know the specifics, we can change 0.5 to the actual value
    }
    public static void doublePixelDrop(ClawServo ClawServoInstance) {
        ClawServoInstance.setPosition(0.8); //Once we know the specifics, we can change 0.8 to the actual value
    }
}
