package org.whitneyrobotics.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whitneyrobotics.ftc.teamcode.Libraries.StateForge.StateForge;
import org.whitneyrobotics.ftc.teamcode.Libraries.StateForge.StateMachine;
import org.whitneyrobotics.ftc.teamcode.Libraries.StateForge.SubstateMachine;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Outtake.Elbow;
import org.whitneyrobotics.ftc.teamcode.Subsystems.Meet3Outtake.Wrist;

public class ElbowWristImpl {
    public static Elbow elbow;
    public static Wrist wrist;
    public enum ElbowWristStates {
        INTAKE, OUTTAKE
    }

    public enum INTAKE_SUBSTATES {
        IDLE, ELBOW_EXTEND, WRIST_FOLD, COMPLETE
    }

    public enum OUTTAKE_SUBSTATES {
        IDLE, ELBOW_RETRACT, WRIST_INTAKE, COMPLETE
    }

    boolean toggle = false;
    boolean substateCompleted = false;

    public static StateMachine<ElbowWristStates> stateMachine;
    public ElbowWristImpl(HardwareMap hardwareMap){
        elbow = new Elbow(hardwareMap);
        wrist = new Wrist(hardwareMap);
        stateMachine = new StateForge.StateMachineBuilder<ElbowWristStates>()
                .substate(ElbowWristStates.INTAKE)
                .transitionBehavior(SubstateMachine.TRANSITION_BEHAVIOR.RESET_INTERNAL_STATE)
                .buildEmbeddedStateMachine(s ->
                    s.state(INTAKE_SUBSTATES.IDLE)
                            //.transitionLinear(() -> toggle)
                            .fin()
                            .state(INTAKE_SUBSTATES.ELBOW_EXTEND)
                            .onEntry(() -> elbow.currentState = Elbow.ElbowPositions.ANGLED)
                            .timedTransitionLinear(0.1)
                            .fin()
                            .state(INTAKE_SUBSTATES.WRIST_FOLD)
                            .onEntry(() -> wrist.currentState = Wrist.WristPositions.OUTTAKING)
                            .timedTransitionLinear(0.01)
                            .fin()
                            .state(INTAKE_SUBSTATES.COMPLETE)
                            .transitionLinear(() -> true)
                            .fin()
                )
                .transitionWithEmbeddedStateMachine(s -> s.getMachineState() == INTAKE_SUBSTATES.COMPLETE)
                //.onExit(() -> toggle = false)
                .fin()
                .substate(ElbowWristStates.OUTTAKE)
                .transitionBehavior(SubstateMachine.TRANSITION_BEHAVIOR.RESET_INTERNAL_STATE)
                .buildEmbeddedStateMachine(s ->
                        s.state(OUTTAKE_SUBSTATES.IDLE)
                                //.transitionLinear(() -> toggle)
                                .fin()
                                .state(OUTTAKE_SUBSTATES.WRIST_INTAKE)
                                .onEntry(() -> wrist.currentState = Wrist.WristPositions.INTAKING)
                                .timedTransitionLinear(0.4)
                                .fin()
                                .state(OUTTAKE_SUBSTATES.ELBOW_RETRACT)
                                .onEntry(() -> elbow.currentState = Elbow.ElbowPositions.PARALLEL)
                                .timedTransitionLinear(0.1)
                                .fin()
                                .state(OUTTAKE_SUBSTATES.COMPLETE)
                                .transitionLinear(() -> true)
                                .fin()
                )
                .transitionWithEmbeddedStateMachine(s -> s.getMachineState() == OUTTAKE_SUBSTATES.COMPLETE)
                //.onExit(() -> toggle = false)
                .fin()
                .build();
        stateMachine.start();
    }

    public void toggle(){
        if(stateMachine.getState() instanceof SubstateMachine){
            ((SubstateMachine<?,?>) stateMachine.getState()).getEmbeddedStateMachine().transitionNextLinear();
        }
    }

    public void update(){
        stateMachine.update();
        elbow.run();

        wrist.run();
    }

    public String getState(){
        String s =  stateMachine.getMachineState().name();
        if(stateMachine.getState() instanceof SubstateMachine){
            s += ((SubstateMachine<?,?>) stateMachine.getState()).getSubstate().name();
        }
        return s;
    }

    public boolean getToggle(){
        return toggle;
    }
}
