package io.github.zeromorse.fsm.context.model.impl;

import io.github.zeromorse.fsm.context.model.GenericCondition;
import io.github.zeromorse.fsm.context.model.GenericContext;
import io.github.zeromorse.fsm.context.model.GenericStateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GenericTransitCondition extends GenericCondition {
    public final static GenericTransitCondition INSTANCE = new GenericTransitCondition();
    private static Logger logger = LoggerFactory.getLogger(GenericTransitCondition.class);

    private GenericTransitCondition() {
    }

    @Override
    public boolean isSatisfied(String from, String to, String event, GenericContext context, GenericStateMachine stateMachine) {
        logger.debug("judge {} to {} by {} start...", from, to, event);
        boolean satisfied = super.isSatisfied(from, to, event, context, stateMachine);
        logger.debug("judge {} to {} by {} finish, ret = {}", from, to, event, satisfied);
        return satisfied;
    }
}
