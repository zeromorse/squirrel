package io.github.zeromorse.fsm.context.model.impl;

import io.github.zeromorse.fsm.context.config.cache.FsmBehavior;
import io.github.zeromorse.fsm.context.model.GenericAction;
import io.github.zeromorse.fsm.context.model.GenericContext;
import io.github.zeromorse.fsm.context.model.GenericStateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class GenericExitAction extends GenericAction {
    private static Logger logger = LoggerFactory.getLogger(GenericExitAction.class);
    public final static GenericExitAction INSTANCE = new GenericExitAction();

    private GenericExitAction() {
    }

    /**
     * 离开时，仅 from、context、event、stateMachine 有值
     */
    @Override
    public void execute(final String from, String to, String event, GenericContext context, GenericStateMachine stateMachine) {
        logger.debug("exit {} start...", from);
        doExecute(context, stateMachine, new Provider() {
            @Override
            public List<String> get(FsmBehavior behavior) {
                return behavior.getExitAction(from);
            }
        });
    }
}
