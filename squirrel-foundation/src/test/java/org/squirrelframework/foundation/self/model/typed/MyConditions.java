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

    private static class Satisfied extends AnonymousCondition<MyContext> {
        @Override
        public boolean isSatisfied(MyContext context) {
            log.info("satisfied");
            return true;
        }
    }

    private static class Unsatisfied extends AnonymousCondition<MyContext> {
        @Override
        public boolean isSatisfied(MyContext context) {
            log.info("unsatisfied");
            return false;
        }
    }
}
