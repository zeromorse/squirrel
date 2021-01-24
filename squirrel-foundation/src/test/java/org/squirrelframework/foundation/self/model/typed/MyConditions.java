package org.squirrelframework.foundation.self.model.typed;

import lombok.extern.slf4j.Slf4j;
import org.squirrelframework.foundation.fsm.AnonymousCondition;

/**
 * 预设条件
 */
@Slf4j
public final class MyConditions {
    public static final Satisfied satisfied = new Satisfied();
    public static final Unsatisfied unsatisfied = new Unsatisfied();

    private static class Satisfied extends AnonymousCondition<MyStateMachine, MyState, MyEvent, MyContext> {
        @Override
        public boolean isSatisfied(MyState from, MyState to, MyEvent event, MyContext context, MyStateMachine stateMachine) {
            log.info("satisfied");
            return true;
        }
    }

    private static class Unsatisfied extends AnonymousCondition<MyStateMachine, MyState, MyEvent, MyContext> {

        @Override
        public boolean isSatisfied(MyState from, MyState to, MyEvent event, MyContext context, MyStateMachine stateMachine) {
            log.info("unsatisfied");
            return false;
        }
    }
}
