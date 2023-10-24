package org.whitneyrobotics.ftc.teamcode.Subsystems;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.whitneyrobotics.ftc.teamcode.VisionImpl.AprilTagScanner2023;




//This script will be used to cohesively manage linear slides, servos 1 and 2, claw, AprilTagScanner and drive train, for intake/outtake
@RequiresApi(api = Build.VERSION_CODES.N)
public class fullMotion {
    LinearSlides linearSlides;
    ServoImplEx servo1;
    ServoImplEx servo2;
    ServoImplEx claw;
    AprilTagScanner2023 scanner;


    public fullMotion(HardwareMap hardwareMap) {

        linearSlides = new LinearSlides(hardwareMap);
        servo1 = hardwareMap.get(ServoImplEx.class, "servo1");
        servo2 = hardwareMap.get(ServoImplEx.class, "servo2");
        claw = hardwareMap.get(ServoImplEx.class, "claw");
        scanner = new AprilTagScanner2023("Webcam 1", hardwareMap);

    }

    public void setServo1(double position) {
        servo1.setPosition(position);
    }

    public void setServo2(double position) {
        servo2.setPosition(position);
    }

    public void setClaw(double position) {
        claw.setPosition(position);
    }

    public void setLinearSlides(double position) {
        newLinearSlides.setSlides(position);
    }

}