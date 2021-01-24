package io.github.zeromorse.fsm.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Boot {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    }
}
