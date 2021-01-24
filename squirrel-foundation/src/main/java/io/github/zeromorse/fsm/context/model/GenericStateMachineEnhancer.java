package io.github.zeromorse.fsm.context.model;

/**
 * 状态机增强器，例如可以用来加入监听器
 */
public interface GenericStateMachineEnhancer {
    void enhance(GenericStateMachine stateMachine);
}
