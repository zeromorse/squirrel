package io.github.zeromorse.fsm.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class IOUtilTest {

    @Test
    public void getContent() {
        String content = IOUtil.getContent("scxml/demo.scxml");
        log.info(content);
    }
}