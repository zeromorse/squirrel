package org.squirrelframework.foundation.self.model.typed;

import lombok.extern.slf4j.Slf4j;
import org.squirrelframework.foundation.fsm.AnonymousAction;

/**
 * 预设动作
 */
@Slf4j
public final class MyActions {
    public static final Transit transit = new Transit();
    public static final Entry entry = new Entry();
    public static final Exit exit = new Exit();

    private static class Transit extends AnonymousAction<MyStateMachine, MyState, MyEvent, MyContext> {
        @Override
        public void execute(MyState from, MyState to, MyEvent event, MyContext context, MyStateMachine stateMachine) {
            log.info("transit {}:{}:{}:{}:{}", from, to, event, context, stateMachine);
        }
    }

    private static class Entry extends AnonymousAction<MyStateMachine, MyState, MyEvent, MyContext> {
        @Override
        public void execute(MyState from, MyState to, MyEvent event, MyContext context, MyStateMachine stateMachine) {
            log.info("entry {}:{}:{}:{}:{}", from, to, event, context, stateMachine);
        }
    }

    private static class Exit extends AnonymousAction<MyStateMachine, MyState, MyEvent, MyContext> {
        @Override
        public void execute(MyState from, MyState to, MyEvent event, MyContext context, MyStateMachine stateMachine) {
            log.info("exit {}:{}:{}:{}:{}", from, to, event, context, stateMachine);
        }
    }
}
