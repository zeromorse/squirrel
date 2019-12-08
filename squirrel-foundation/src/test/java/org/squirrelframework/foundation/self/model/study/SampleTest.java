package org.squirrelframework.foundation.self.model.study;

import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.squirrelframework.foundation.component.SquirrelProvider;
import org.squirrelframework.foundation.fsm.*;
import org.squirrelframework.foundation.self.model.typed.*;
import org.squirrelframework.foundation.util.TypeReference;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 样本的测试
 */
@Slf4j
public class SampleTest {

    private StateMachineBuilder<MyStateMachine, MyState, MyEvent, MyContext> builder
            = StateMachineBuilderFactory.create(MyStateMachine.class, MyState.class, MyEvent.class, MyContext.class);

    @Test
    public void testExport() {
        MutableState<MyStateMachine, MyState, MyEvent, MyContext> B = builder.defineState(MyState.B);
        B.addEntryAction(MyActions.entry);
        B.addExitAction(MyActions.exit);

        builder.transition().from(MyState.A).to(MyState.B).on(MyEvent.ab).when(MyConditions.satisfied).perform(MyActions.transit);
        builder.transition().from(MyState.A).to(MyState.C).on(MyEvent.ac).perform(MyActions.transit);
        builder.transition().from(MyState.A).to(MyState.D).on(MyEvent.ad);
        builder.transition().from(MyState.B).to(MyState.C).on(MyEvent.bc).when(MyConditions.satisfied);
        builder.transition().from(MyState.C).to(MyState.D).on(MyEvent.cd).when(MyConditions.unsatisfied).perform(MyActions.transit);

        MyStateMachine myStateMachine = builder.newStateMachine(MyState.A);
        SCXMLVisitor visitor = SquirrelProvider.getInstance().newInstance(SCXMLVisitor.class);
        myStateMachine.accept(visitor);
        visitor.convertSCXMLFile("MyStateMachine", true);
    }

    @Test
    public void testImportWithUptyped() throws IOException {
        String scxml = Resources.toString(Resources.getResource("scxml/MyStateMachine.scxml"), Charset.defaultCharset());
        log.debug(scxml);
        UntypedStateMachineBuilder builder = new UntypedStateMachineImporter().importDefinition(scxml);

        MyStateMachine myStateMachine = builder.newAnyStateMachine(MyState.A);
        myStateMachine.fire(MyEvent.ab);
        Assert.assertEquals(MyState.B, myStateMachine.getCurrentState());
    }

    @Test
    public void testImportWithTyped() throws IOException {
        String scxml = Resources.toString(Resources.getResource("scxml/MyStateMachine.scxml"), Charset.defaultCharset());
        log.debug(scxml);
        StateMachineImporter<MyStateMachine, MyState, MyEvent, MyContext> importer = SquirrelProvider.getInstance().newInstance(new TypeReference<StateMachineImporter<MyStateMachine, MyState, MyEvent, MyContext>>() {
        });
        StateMachineBuilder<MyStateMachine, MyState, MyEvent, MyContext> stateMachineBuilder = importer.importFromString(scxml);
        MyStateMachine myStateMachine = stateMachineBuilder.newStateMachine(MyState.A);
        myStateMachine.fire(MyEvent.ab);
        Assert.assertEquals(MyState.B, myStateMachine.getCurrentState());
    }

}
