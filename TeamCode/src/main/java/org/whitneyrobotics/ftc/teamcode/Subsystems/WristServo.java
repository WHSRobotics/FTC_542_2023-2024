package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.ServoControllerEx;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.ServoConfigurationType;

public class WristServo extends ServoImplEx {
    WristServo(ServoControllerEx controller, int portNumber, ServoConfigurationType servoType){
        super(controller,portNumber, servoType);
    }
    public static void wristRotation(WristServo WristServoInstance) { //Wrist motion when we move the arm to put the pixel on the backdrop
        WristServoInstance.setPosition(0.167);
        /* if we let the servo arm being straight up be the position 1.00 and the servo arm being pointed down (180 degree change counterclockwise ) be 0.00, then 
        a 150 degree change would be about 1-150/180=1-0.8333...=0.167 so the position would be 0.167 to get it perpendicular to the backdrop.
        We can change the parameter in setPosition() later as needed
         */

    }
}
