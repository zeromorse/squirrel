package io.github.zeromorse.fsm.context.model;

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

    /**
     * 迁移时，均有值
     */
    private static class Transit extends AnonymousAction<MyStateMachine, String, String, MyContext> {
        @Override
        public void execute(String from, String to, String event, MyContext context, MyStateMachine stateMachine) {
            log.info("transit from = {},  to = {},  event = {},  context = {},  stateMachine = {}", from, to, event, context, stateMachine);
        }
    }

    /**
     * 进入时，仅 to、context、stateMachine 有值
     */
    private static class Entry extends AnonymousAction<MyStateMachine, String, String, MyContext> {
        @Override
        public void execute(String from, String to, String event, MyContext context, MyStateMachine stateMachine) {
            log.info("entry from = {},  to = {},  event = {},  context = {},  stateMachine = {}", from, to, event, context, stateMachine);
        }
    }

    /**
     * 离开时，仅 from、context、event、stateMachine 有值
     */
    private static class Exit extends AnonymousAction<MyStateMachine, String, String, MyContext> {
        @Override
        public void execute(String from, String to, String event, MyContext context, MyStateMachine stateMachine) {
            log.info("exit from = {},  to = {},  event = {},  context = {},  stateMachine = {}", from, to, event, context, stateMachine);
        }
    }
}
