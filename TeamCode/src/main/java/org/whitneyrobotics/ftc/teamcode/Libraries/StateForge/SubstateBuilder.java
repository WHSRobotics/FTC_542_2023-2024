package org.whitneyrobotics.ftc.teamcode.Libraries.StateForge;

@FunctionalInterface
public interface SubstateBuilder<E extends Enum<E>,R extends Enum<R>> {
    StateForge.StateMachineBuilder<R> useCommands(StateForge.StateMachineBuilder<R> builder);
}
