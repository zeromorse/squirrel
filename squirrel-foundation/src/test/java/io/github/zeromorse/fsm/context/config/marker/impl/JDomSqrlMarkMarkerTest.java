package io.github.zeromorse.fsm.context.config.marker.impl;

import io.github.zeromorse.fsm.context.model.MyActions;
import io.github.zeromorse.fsm.context.model.MyCondition;
import io.github.zeromorse.fsm.context.model.MyContext;
import io.github.zeromorse.fsm.context.model.MyStateMachine;
import io.github.zeromorse.fsm.util.IOUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class JDomSqrlMarkMarkerTest {

    /**
     * 默认装饰
     */
    @Test
    public void decorate() {
        String scxml = getScxml();
        String decorated = new JDomScxmlMarker().decorate(scxml);
        log.debug(decorated);
    }

    /**
     * 定制化的装饰
     */
    @Test
    public void customDecorate() {
        String decorated = getDecoratedScxml();
        log.debug(decorated);
    }

    public String getDecoratedScxml() {
        String scxml = getScxml();
        SqrlMark sqrlMark = new SqrlMark.Builder()
                .types(MyStateMachine.class, String.class, String.class, MyContext.class)
                .instances(MyCondition.INSTANCE, MyActions.entry, MyActions.exit, MyActions.transit)
                .build();
        JDomScxmlMarker jDomScxmlMarker = new JDomScxmlMarker();
        jDomScxmlMarker.setSqrlMark(sqrlMark);
        return jDomScxmlMarker.decorate(scxml);
    }

    private String getScxml() {
        String resourceName = "scxml/demo.scxml";
        return IOUtil.getContent(resourceName);
    }
}