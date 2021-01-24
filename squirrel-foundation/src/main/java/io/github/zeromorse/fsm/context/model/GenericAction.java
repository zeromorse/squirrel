package io.github.zeromorse.fsm.context.model;

import io.github.zeromorse.fsm.context.FsmAgentConfiguration;
import io.github.zeromorse.fsm.context.config.cache.FsmBehavior;
import org.squirrelframework.foundation.fsm.AnonymousAction;

import java.util.List;

public abstract class GenericAction extends AnonymousAction<GenericStateMachine, String, String, GenericContext> {

    protected void doExecute(GenericContext context, GenericStateMachine stateMachine, Provider provider) {
        FsmAgentConfiguration configuration = context.getConfiguration();
        FsmBehavior fsmBehavior = stateMachine.getVersion().getBehavior();
        List<String> signatures = provider.get(fsmBehavior);

        if (signatures == null || signatures.isEmpty()) {
            return;
        }

        for (String signature : signatures) {
            configuration.getMethodInvoker().invoke(signature, context.getArgStr());
        }
    }

    // 提供方法列表
    public interface Provider {
        List<String> get(FsmBehavior behavior);
    }
}
