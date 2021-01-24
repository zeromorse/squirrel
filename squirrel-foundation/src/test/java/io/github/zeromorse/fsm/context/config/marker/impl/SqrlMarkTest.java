package io.github.zeromorse.fsm.context.config.marker.impl;

import io.github.zeromorse.fsm.context.model.GenericContext;
import io.github.zeromorse.fsm.context.model.impl.GenericEntryAction;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class SqrlMarkTest {

    @Test
    public void testClassName() {
        log.info(GenericContext.class.getCanonicalName());
    }

    @Test
    public void testSQRLName() {
        log.info(GenericEntryAction.INSTANCE.toString());
    }
}