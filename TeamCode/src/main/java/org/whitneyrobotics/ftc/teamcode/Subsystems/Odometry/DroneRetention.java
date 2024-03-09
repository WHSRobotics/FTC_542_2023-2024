package org.whitneyrobotics.ftc.teamcode.Subsystems.Odometry;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class DroneRetention {
    public enum DroneArmPositions {
        CLAMP(0.7),
        RELEASE(0.25);
        public final double pos;
        DroneArmPositions(double pos){
            this.pos = pos;
        }
    }
    private Servo s;
    @NonNull
    DroneArmPositions pos = DroneArmPositions.CLAMP;

    public DroneRetention(HardwareMap hm){
        s = hm.get(Servo.class, "hold");
    }
    public void setPos(@NonNull DroneArmPositions pos){
        this.pos = pos;
    }

    public void toggle(){
        this.pos = DroneArmPositions.values()[pos.ordinal() + 1 % DroneArmPositions.values().length];
    }

    public void update(){
        s.setPosition(pos.pos);
    }
}
