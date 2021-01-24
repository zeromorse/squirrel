package io.github.zeromorse.fsm.context.model;

import com.sankuai.meituan.waimai.config.fsm.agent.context.FsmAgentConfiguration;
import com.sankuai.meituan.waimai.config.fsm.agent.context.config.cache.FsmBehavior;
import com.sankuai.meituan.waimai.config.fsm.agent.context.config.cache.FteTriple;
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
