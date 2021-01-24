package io.github.zeromorse.fsm.util;

import io.github.zeromorse.fsm.spring.bean.SpringHosting;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;

@Slf4j
public class SpringMethodSignatureTest {

    @Test
    public void discover() {
        String ret = SpringMethodSignature.discover(SpringHosting.class, "grayJudge");
        log.info("ret = {}", ret);
    }

    @Test
    public void discoverAll() {
        List<String> list = SpringMethodSignature.discover(SpringHosting.class);
        log.info("list = {}", list);
    }
}