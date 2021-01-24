package io.github.zeromorse.fsm.spring.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PersonName {
    private String firstName;
    private String lastName;
    private int age;
    private boolean judge;
    private boolean condition;
}
