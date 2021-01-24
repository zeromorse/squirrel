package io.github.zeromorse.fsm.context.model.impl;

import io.github.zeromorse.fsm.context.config.cache.FsmBehavior;
import io.github.zeromorse.fsm.context.model.GenericAction;
import io.github.zeromorse.fsm.context.model.GenericContext;
import io.github.zeromorse.fsm.context.model.GenericStateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class GenericEntryAction extends GenericAction {
    public final static GenericEntryAction INSTANCE = new GenericEntryAction();
    private static Logger logger = LoggerFactory.getLogger(GenericEntryAction.class);

    private GenericEntryAction() {
    }

    /**
     * 进入时，仅 to、context、stateMachine 有值
     */
    @Override
    public void execute(String from, final String to, String event, GenericContext context, GenericStateMachine stateMachine) {
        logger.debug("entry {} start...", to);
        if (context.getFromState().equals(to)) {
            return;
        }
        doExecute(context, stateMachine, new Provider() {
            @Override
            public List<String> get(FsmBehavior behavior) {
                return behavior.getEntryAction(to);
            }
        });
    }
}
