//package org.whitneyrobotics.ftc.teamcode.Subsystems;
//
//import com.qualcomm.hardware.rev.RevColorSensorV3;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.robotcore.hardware.ServoControllerEx;
//import com.qualcomm.robotcore.hardware.ServoImplEx;
//
//import org.whitneyrobotics.ftc.teamcode.Libraries.StateForge.StateForge;
//import org.whitneyrobotics.ftc.teamcode.Libraries.StateForge.StateMachine;
//
//public class PrismSensor {
//
//    enum States {
//        DETECTING_TOP,
//        DETECTING_BOTTOM,
//        IDLE
//    }
//    ServoImplEx top, bottom;
//    private final int servoBottomIndex = 3, servoTopIndex = 5;
//    private boolean requestImmediateDetection;
//
//    RevColorSensorV3 sensor;
//    public int[] colors = new int[2];
//    public final StateMachine<States> stateMachine;
//    public PrismSensor(HardwareMap hardwareMap){
//        top = (ServoImplEx) hardwareMap.get(Servo.class, "topPrism");
//        bottom = (ServoImplEx) hardwareMap.get(Servo.class, "bottomPrism");;
//        sensor = hardwareMap.get(RevColorSensorV3.class, "PrismSensor");
//        top.setPosition(1);
//        bottom.setPosition(1);
//        top.setPwmDisable();
//        bottom.setPwmDisable();
//        stateMachine = new StateForge.StateMachineBuilder<States>()
//                .state(States.DETECTING_TOP)
//                    .onEntry(top::setPwmEnable)
//                    .onExit(() -> {
//                        colors[1] = sensor.getNormalizedColors().toColor();
//                        top.setPwmDisable();
//
//                    })
//                    .timedTransitionLinear(0.5)
//                .fin()
//                .state(States.DETECTING_BOTTOM)
//                    .onEntry(bottom::setPwmEnable)
//                    .onExit(() -> {
//                        colors[0] = sensor.getNormalizedColors().toColor();
//                        bottom.setPwmDisable();
//                    })
//                    .timedTransitionLinear(0.5)
//                .fin()
//                .state(States.IDLE)
//                    .timedTransitionLinear(5)
//                    .transitionLinear(()->requestImmediateDetection)
//                    .onExit(() -> requestImmediateDetection = false)
//                .fin().build();
//        stateMachine.start();
//    }
//
//    public void update(){
//        stateMachine.update();
//    }
//
//    public String getState(){
//        return stateMachine.getMachineState().name();
//    }
//
//    public double[] getPositions(){
//        return new double[]{
//                0,0
//        };
//    }
//
//    public void requestImmediateDetection() {requestImmediateDetection = true;}
//
//}