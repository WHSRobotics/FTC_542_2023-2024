package org.whitneyrobotics.ftc.teamcode.teleop;


import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad2;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whitneyrobotics.ftc.teamcode.Subsystems.newLinearSlides;
import org.whitneyrobotics.ftc.teamcode.visionImpl.gaugeDistance;


//CenterStage Main Teleop

@TeleOp(name = "CenterStage TeleOp", group = "A")
@RequiresApi(api =  Build.VERSION_CODES.N)
public class WHSTeleOp {

        void setupGamepads() {
            gamepad2.SQUARE.onPress(e -> robot.linearSlides.setTarget(newLinearSlides.Targets.FIRSTMARK));
            gamepad2.TRIANGLE.onPress(e -> robot.linearSlides.setTarget(newLinearSlides.Targets.SECONDMARK));
            gamepad2.CIRCLE.onPress(e -> robot.linearSlides.setTarget(newLinearSlides.Targets.THIRDMARK));
            gamepad2.CROSS.onPress(e -> robot.linearSlides.setTarget(newLinearSlides.Targets.LOWERED));
        }


}
