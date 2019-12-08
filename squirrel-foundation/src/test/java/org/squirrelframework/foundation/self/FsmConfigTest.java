package org.squirrelframework.foundation.self;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.squirrelframework.foundation.fsm.StateMachineBuilder;
import org.squirrelframework.foundation.fsm.StateMachineBuilderFactory;
import org.squirrelframework.foundation.fsm.StateMachineConfiguration;
import org.squirrelframework.foundation.fsm.StateMachineLogger;
import org.squirrelframework.foundation.self.model.typed.MyContext;
import org.squirrelframework.foundation.self.model.typed.MyEvent;
import org.squirrelframework.foundation.self.model.typed.MyState;
import org.squirrelframework.foundation.self.model.typed.MyStateMachine;

public class FsmConfigTest {
    private StateMachineBuilder<MyStateMachine, MyState, MyEvent, MyContext> builder;

    // - Execute Around (BEGIN) -
    @Before
    public void setUp() {
        if (builder == null) {
            builder = StateMachineBuilderFactory.create(MyStateMachine.class, MyState.class, MyEvent.class, MyContext.class);
        }
    }

    @After
    public void tearDown() {
        builder = null;
    }
    // - Execute Around (END) -


    /**
     * 测试Debug模式，有 {@link StateMachineLogger} 中的日志
     */
    @Test
    public void testDebugMode() {
        builder.transition().from(MyState.A).to(MyState.B).on(MyEvent.ab);
        StateMachineConfiguration config = StateMachineConfiguration.create();
        config.enableDebugMode(true);
        builder.setStateMachineConfiguration(config);

        MyStateMachine myStateMachine = builder.newStateMachine(MyState.A);
        myStateMachine.fire(MyEvent.ab);
    }
}
