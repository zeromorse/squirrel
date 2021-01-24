package io.github.zeromorse.fsm.context.model;

import lombok.extern.slf4j.Slf4j;
import org.squirrelframework.foundation.fsm.AnonymousCondition;

/**
 * 预设条件
 */
@Slf4j
public class MyCondition extends AnonymousCondition<MyStateMachine, String, String, MyContext> {
    public static final MyCondition INSTANCE = new MyCondition();

    /**
     * 判断时，均有值
     */
    @Override
    public boolean isSatisfied(String from, String to, String event, MyContext context, MyStateMachine stateMachine) {
        log.info("guard from = {},  to = {},  event = {},  context = {},  stateMachine = {}", from, to, event, context, stateMachine);
        return true;
    }
}
