package io.github.zeromorse.fsm.context.model;

import io.github.zeromorse.fsm.context.config.marker.impl.JDomSqrlMarkMarkerTest;
import org.junit.Assert;
import org.junit.Test;
import org.squirrelframework.foundation.component.SquirrelProvider;
import org.squirrelframework.foundation.fsm.StateMachineBuilder;
import org.squirrelframework.foundation.fsm.StateMachineImporter;
import org.squirrelframework.foundation.util.TypeReference;

public class StateMachineTest {
    @Test
    public void tryFire() {
        String scxml = new JDomSqrlMarkMarkerTest().getDecoratedScxml();

        StateMachineImporter<MyStateMachine, String, String, MyContext> importer = SquirrelProvider.getInstance().newInstance(new TypeReference<StateMachineImporter<MyStateMachine, String, String, MyContext>>() {
        });
        StateMachineBuilder<MyStateMachine, String, String, MyContext> stateMachineBuilder = importer.importFromString(scxml);
        MyStateMachine myStateMachine = stateMachineBuilder.newStateMachine(null);
        myStateMachine.start(MyContext.INSTANCE);
        Assert.assertEquals("A", myStateMachine.getCurrentState());

        myStateMachine.fire("ab", MyContext.INSTANCE);
        Assert.assertEquals("B", myStateMachine.getCurrentState());
    }
}
