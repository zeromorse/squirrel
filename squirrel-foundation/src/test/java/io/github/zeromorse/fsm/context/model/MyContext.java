package io.github.zeromorse.fsm.context.model;

import lombok.ToString;

@ToString
public class MyContext {
    public static final MyContext INSTANCE = new MyContext();

    private String name = "Mike";
    private int age = 12;

}