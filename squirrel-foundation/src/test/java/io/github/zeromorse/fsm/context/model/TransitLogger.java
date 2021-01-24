package io.github.zeromorse.fsm.context.model;

import lombok.extern.slf4j.Slf4j;
import org.squirrelframework.foundation.fsm.annotation.hook.OnTransitionComplete;

@Slf4j
public class TransitLogger implements GenericStateMachineEnhancer {

    @OnTransitionComplete
    public void onTransitionComplete(String sourceState, String targetState, String event, GenericContext context) {
        log.info("transit from {} to {} by {}, reported by transitLogger", sourceState, targetState, event);
    }

    @Override
    public void enhance(GenericStateMachine stateMachine) {
        stateMachine.addDeclarativeListener(this);
    }
}