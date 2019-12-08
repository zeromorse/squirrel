package org.squirrelframework.foundation.self;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.squirrelframework.foundation.fsm.*;
import org.squirrelframework.foundation.self.model.MyLogger;
import org.squirrelframework.foundation.self.model.typed.*;

public class FsmBuilderTest {

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
     * 测试状态迁移中触发的各种动作的构造
     * - 状态相关：entry/exit
     * - 迁移相关：from...to...on...[when...perform...] 子句
     */
    @Test
    public void testActions() {
        final MyLogger logger = new MyLogger();
        final String byeA = "bye, A;";
        final String heyB = "hey, B;";
        final String checkAB = "check, ab;";
        final String transitAB = "transit, ab;";

        builder.defineState(MyState.A).addExitAction(new AnonymousAction<MyStateMachine, MyState, MyEvent, MyContext>() {
            @Override
            public void execute(MyState from, MyState to, MyEvent event, MyContext context, MyStateMachine stateMachine) {
                logger.append(byeA);
            }
        });
        builder.defineState(MyState.B).addEntryAction(new AnonymousAction<MyStateMachine, MyState, MyEvent, MyContext>() {
            @Override
            public void execute(MyState from, MyState to, MyEvent event, MyContext context, MyStateMachine stateMachine) {
                logger.append(heyB);
            }
        });
        builder.externalTransition().from(MyState.A).to(MyState.B).on(MyEvent.ab).when(new AnonymousCondition<MyContext>() {
            @Override
            public boolean isSatisfied(MyContext context) {
                logger.append(checkAB);
                return true;
            }
        }).perform(new AnonymousAction<MyStateMachine, MyState, MyEvent, MyContext>() {
            @Override
            public void execute(MyState from, MyState to, MyEvent event, MyContext context, MyStateMachine stateMachine) {
                logger.append(transitAB);
            }
        });

        MyStateMachine myStateMachine = builder.newStateMachine(MyState.A);
        myStateMachine.start();
        myStateMachine.fire(MyEvent.ab);

        String log = logger.toString();
        Assert.assertEquals(checkAB + byeA + transitAB + heyB, log);
    }

    /**
     * 测试 between...and...onMutual... 子句
     */
    @Test
    public void testMutual() {
        MyLogger logger = new MyLogger();

        // 不需要事先定义状态
        builder.externalTransitions().between(MyState.A).and(MyState.B).onMutual(MyEvent.ab, MyEvent.ba);
        MyStateMachine myStateMachine = builder.newStateMachine(MyState.A);
        watchTransit(myStateMachine, logger);

        myStateMachine.start();
        myStateMachine.fire(MyEvent.ab);
        myStateMachine.fire(MyEvent.ba);
        myStateMachine.terminate();
    }

    /**
     * 测试 from...toAmong...onEach...when...perform 子句
     */
    @Test
    public void testToAmong() {
        final MyLogger logger = new MyLogger();

        builder.externalTransitions()
                .from(MyState.A).toAmong(MyState.B, MyState.C).onEach(MyEvent.ab, MyEvent.ac)
                .when(new AnonymousCondition<MyContext>() {
                    @Override
                    public boolean isSatisfied(MyContext context) {
                        logger.append("everyone is welcome!");
                        return true;
                    }
                })
                .perform(new AnonymousAction<MyStateMachine, MyState, MyEvent, MyContext>() {
                    @Override
                    public void execute(MyState from, MyState to, MyEvent event, MyContext context, MyStateMachine stateMachine) {
                        logger.append("%s-[%s]->%s", from, event, to);
                    }
                });

        MyStateMachine myStateMachine = builder.newStateMachine(MyState.A);
        myStateMachine.fire(MyEvent.ab);

        MyStateMachine myStateMachine2 = builder.newStateMachine(MyState.A);
        myStateMachine2.fire(MyEvent.ac);
    }

    /**
     * 测试 fromAmong...to...on...when...perform 子句
     */
    @Test
    public void testFromAmong() {
        final MyLogger logger = new MyLogger();

        builder.externalTransitions()
                .fromAmong(MyState.A, MyState.B)
                .to(MyState.C)
                .on(MyEvent._c)
                .perform(new AnonymousAction<MyStateMachine, MyState, MyEvent, MyContext>() {
                    @Override
                    public void execute(MyState from, MyState to, MyEvent event, MyContext context, MyStateMachine stateMachine) {
                        logger.append("%s-[%s]->%s", from, event, to);
                    }
                });

        MyStateMachine myStateMachine = builder.newStateMachine(MyState.A);
        myStateMachine.fire(MyEvent._c);

        MyStateMachine myStateMachine2 = builder.newStateMachine(MyState.B);
        myStateMachine2.fire(MyEvent._c);
    }

    /**
     * 测试内部状态
     */
    @Test
    public void testInternalTransit() {
        final MyLogger logger = new MyLogger();

        builder.internalTransition()
                .within(MyState.A)
                .on(MyEvent.it)
                .perform(new AnonymousAction<MyStateMachine, MyState, MyEvent, MyContext>() {
                    @Override
                    public void execute(MyState from, MyState to, MyEvent event, MyContext context, MyStateMachine stateMachine) {
                        logger.append("%s-[%s]->%s", from, event, to);
                    }
                });

        MyStateMachine myStateMachine = builder.newStateMachine(MyState.A);
        myStateMachine.fire(MyEvent.it);
        Assert.assertEquals(MyState.A, myStateMachine.getCurrentState());
    }

    /**
     * 测试本地迁移
     */
    @Test
    public void testLocalTransit() {
        final MyLogger logger = new MyLogger();

        MutableState<MyStateMachine, MyState, MyEvent, MyContext> stA = builder.defineState(MyState.A);
        MutableState<MyStateMachine, MyState, MyEvent, MyContext> stAa = builder.defineState(MyState.Aa);
        MutableState<MyStateMachine, MyState, MyEvent, MyContext> stAb = builder.defineState(MyState.Ab);

        stA.addChildState(stAa);
        stA.addChildState(stAb);
        stA.setInitialState(stAa);
        //stAb.setFinal(true); 设置完成不能使状态回复到父节点
        builder.localTransition().from(MyState.Aa).to(MyState.Ab).on(MyEvent.lc);
        builder.externalTransition().from(MyState.B).to(MyState.C).on(MyEvent.bc);

        MyStateMachine myStateMachine = builder.newStateMachine(MyState.A);
        watchTransit(myStateMachine, logger);

        // 本地迁移不会触发所有迁移动作，所以无法监听到
        myStateMachine.fire(MyEvent.lc);
        Assert.assertEquals(MyState.Ab, myStateMachine.getCurrentState());

        myStateMachine = builder.newStateMachine(MyState.B);
        watchTransit(myStateMachine, logger);
        // 外部迁移可以监听到
        myStateMachine.fire(MyEvent.bc);
        Assert.assertEquals(MyState.C, myStateMachine.getCurrentState());

    }


    /**
     * 测试延迟绑定动作
     */
    @Test
    public void testDeferBound() {
        final MyLogger logger = new MyLogger();

        builder.defineState(MyState.A);
        builder.defineState(MyState.B);
        builder.transition().from(MyState.A).to(MyState.B).on(MyEvent.ab);
        builder.transit().fromAny().to(MyState.B).onAny().perform(new AnonymousAction<MyStateMachine, MyState, MyEvent, MyContext>() {
            @Override
            public void execute(MyState from, MyState to, MyEvent event, MyContext context, MyStateMachine stateMachine) {
                logger.append("defer");
            }
        });

        MyStateMachine myStateMachine = builder.newStateMachine(MyState.A);
        watchTransit(myStateMachine, logger);

        myStateMachine.start();
        myStateMachine.fire(MyEvent.ab);

        Assert.assertEquals(MyState.B, myStateMachine.getCurrentState());
    }

    // 监控Transit
    private void watchTransit(MyStateMachine myStateMachine, final MyLogger logger) {
        myStateMachine.addTransitionCompleteListener(new StateMachine.TransitionCompleteListener<MyStateMachine, MyState, MyEvent, MyContext>() {
            @Override
            public void transitionComplete(StateMachine.TransitionCompleteEvent<MyStateMachine, MyState, MyEvent, MyContext> event) {
                logger.append("%s-[%s]->%s", event.getSourceState(), event.getCause(), event.getTargetState());
            }
        });
    }
}
