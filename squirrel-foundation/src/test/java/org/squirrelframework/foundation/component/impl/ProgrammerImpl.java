package org.squirrelframework.foundation.component.impl;

import javax.annotation.PostConstruct;

import org.squirrelframework.foundation.component.PersonImpl;
import org.squirrelframework.foundation.component.Programmer;

public class ProgrammerImpl extends PersonImpl implements Programmer {
    
    private String lang;

    // 会被 PostConstructPostProcessorImpl 处理增强
    @PostConstruct
    public void postConstruct() {
        setName("Henry");
        lang = "Java";
    }

    @Override
    public String getLanguage() {
        return lang;
    }
}
