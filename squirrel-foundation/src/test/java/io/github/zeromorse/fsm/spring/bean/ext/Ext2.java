package io.github.zeromorse.fsm.spring.bean.ext;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

@Slf4j
public class Ext2 {
    @PostConstruct
    public void postConstruct() {
        log.info("Hello, I am Ext2");
    }
}
