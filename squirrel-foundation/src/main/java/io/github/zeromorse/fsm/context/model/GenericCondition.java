package io.github.zeromorse.fsm.context.model;

import io.github.zeromorse.fsm.context.FsmAgentConfiguration;
import io.github.zeromorse.fsm.context.config.cache.FsmBehavior;
import io.github.zeromorse.fsm.context.config.cache.FteTriple;
import org.squirrelframework.foundation.fsm.AnonymousCondition;

public abstract class GenericCondition extends AnonymousCondition<GenericStateMachine, String, String, GenericContext> {

    /**
     * 判断时，均有值
     */
    @Override
    public boolean isSatisfied(String from, String to, String event, GenericContext context, GenericStateMachine stateMachine) {
        FsmAgentConfiguration configuration = context.getConfiguration();
        FsmBehavior fsmBehavior = stateMachine.getVersion().getBehavior();
        String condition = fsmBehavior.getTransitCondition(new FteTriple(from, to, event));
        if (condition == null) {
            return true;
        }
        return ((boolean) configuration.getMethodInvoker().invoke(condition, context.getArgStr()));
    }
}
