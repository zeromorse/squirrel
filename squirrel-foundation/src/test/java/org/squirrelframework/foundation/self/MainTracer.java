package org.squirrelframework.foundation.self;

import org.squirrelframework.foundation.fsm.StateMachineBuilder;
import org.squirrelframework.foundation.fsm.StateMachineBuilderFactory;
import org.squirrelframework.foundation.fsm.annotation.structure.ContextInsensitive;
import org.squirrelframework.foundation.fsm.impl.AbstractStateMachine;

public class MainTracer {
    public static void main(String[] args) {
        // 创建一个简单的状态机，然后追踪路径
        StateMachineBuilder<SwitchSM, State, Event, Void> builder = StateMachineBuilderFactory.create(SwitchSM.class, State.class, Event.class, Void.class);
        builder.onExit(State.off).callMethod("exitLog");
        builder.onEntry(State.on).callMethod("entryLog");
        builder.externalTransition().from(State.off).to(State.on).on(Event.switch_flipped).callMethod("transitLog");
        builder.externalTransition().from(State.on).to(State.off).on(Event.switch_flipped).callMethod("transitLog");

        SwitchSM switchSM = builder.newStateMachine(State.off);
        switchSM.start();
        switchSM.fire(Event.switch_flipped);
        System.out.println("switchSM.logString() = " + switchSM.logString());
    }

    enum State {
        on, off
    }

    enum Event {
        switch_flipped
    }

    @ContextInsensitive
    static class SwitchSM extends AbstractStateMachine<SwitchSM, State, Event, Void> {

        private StringBuilder logger = new StringBuilder();

        private void entryLog(State from, State to, Event e) {
            logger.append(String.format("entry %s by %s,", to, e));
        }

        private void exitLog(State from, State to, Event e) {
            logger.append(String.format("exit %s by %s,", from, e));
        }

        private void transitLog(State from, State to, Event e) {
            logger.append(String.format("transit from %s to %s by %s,", from, to, e));
        }

        private String logString() {
            return logger.toString();
        }
    }
}
