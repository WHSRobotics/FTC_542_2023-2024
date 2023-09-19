package org.whitneyrobotics.ftc.teamcode.subsys;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.whitneyrobotics.ftc.teamcode.lib.util.Functions;
import com.qualcomm.robotcore.hardware.IMU;

public class IMU2 {

        private double imuBias = 0;
        private double calibration = 0;
        private final double Z_ACCEL_THRESHOLD = 6;

        public final IMU imu;

        public IMU2(HardwareMap hardwareMap){
            this(hardwareMap, "imu");
        }

        public IMU2(HardwareMap theMap, String name) {
            IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.UP,
                    RevHubOrientationOnRobot.UsbFacingDirection.LEFT
            ));


            imu = theMap.get(IMU.class, name);
            imu.initialize(parameters);
        }

        double heading;



        public double[] getThreeHeading()
        {
            double xheading = imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS).firstAngle;// - calibration;
            double yheading = imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS).secondAngle;// - calibration;
            double zheading = imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS).thirdAngle; //- calibration;

            double[] threeHeading = {xheading,yheading,zheading};
            return threeHeading; // -180 to 180 deg
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public double getHeading(){
            heading = imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS).firstAngle - calibration;
            //heading = Functions.normalizeAngle(heading); // -180 to 180 deg
            return heading;
        }


        @RequiresApi(api = Build.VERSION_CODES.N)
        public double getHeadingRadians(){
            return getHeading()*(Math.PI/180);
        }

        public void zeroHeading(){
            zeroHeading(0.0d);
        }

        public void zeroHeading(double offset){
            calibration = imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS).firstAngle - offset;
        }

        // Returns the magnitude of the acceleration, not the direction.
        /*public double getAccelerationMag(){
            double xAccel = imu.getRobotOrientation().xAccel;
            double yAccel = imu.getRobotOrientation().yAccel;
            double zAccel = imu.getRobotOrientation().zAccel;

            double accelMag =
                    Math.sqrt(
                            Math.pow( xAccel, 2 ) + Math.pow( yAccel, 2 ) + Math.pow( zAccel, 2 )
                    );
            return accelMag;
        }*/

        // Returns the linear acceleration in the z direction
//        public double getZAcceleration() {
//            return imu.getLinearAcceleration().zAccel;
//        }
//
//        public double getYAcceleration() {
//            return imu.getLinearAcceleration().yAccel;
//        }

        // Determines if the linear acceleration in the z direction is over the threshold
//        public boolean exceedZAccelThreshold() {
//            double zAccel = imu.getLinearAcceleration().zAccel;
//            if (zAccel > Z_ACCEL_THRESHOLD) {
//                return true;
//            }
//            return false;
//        }

        //public double getAngularVelocity() {
        //    return imu.getAngularVelocity().zRotationRate;
        //}

        public void setImuBias(double vuforiaHeading){
            imuBias = Functions.normalizeAngle(vuforiaHeading - getHeading()); // -180 to 180 deg
        }

        public double getImuBias() {
            return imuBias;
        }

//        public boolean hasError(){
//            return imu.getSystemError().bVal != 0;
//        }
   // }


}
