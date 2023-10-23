package org.whitneyrobotics.ftc.teamcode.Subsystems;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;


//This script will be used to cohesively manage linear slides, servos 1 and 2, claw, AprilTagScanner and drive train, for intake/outtake
@RequiresApi(api = Build.VERSION_CODES.N)
public class fullMotion {
    LinearSlides linearSlides;
    ServoImplEx servo1;
    ServoImplEx servo2;
    ServoImplEx claw;


    public fullMotion(HardwareMap hardwareMap) {

    }

}
