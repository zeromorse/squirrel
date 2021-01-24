package io.github.zeromorse.fsm.context.model.impl;

import io.github.zeromorse.fsm.context.config.cache.FsmBehavior;
import io.github.zeromorse.fsm.context.config.cache.FteTriple;
import io.github.zeromorse.fsm.context.model.GenericAction;
import io.github.zeromorse.fsm.context.model.GenericContext;
import io.github.zeromorse.fsm.context.model.GenericStateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class GenericTransitAction extends GenericAction {
    private static Logger logger = LoggerFactory.getLogger(GenericTransitAction.class);
    public final static GenericTransitAction INSTANCE = new GenericTransitAction();

    private GenericTransitAction() {
    }

    /**
     * 迁移时，均有值
     */
    @Override
    public void execute(final String from, final String to, final String event, GenericContext context, GenericStateMachine stateMachine) {
        logger.debug("transit {} to {} by {} start...", from, to, event);
        doExecute(context, stateMachine, new Provider() {
            @Override
            public List<String> get(FsmBehavior behavior) {
                return behavior.getTransitAction(new FteTriple(from, to, event));
            }
        });
    }
}
