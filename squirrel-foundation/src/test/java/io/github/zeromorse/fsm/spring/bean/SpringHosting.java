package io.github.zeromorse.fsm.spring.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 映射失败
 * hosting#sayHello:name
 *
 * 执行行为
 * hosting#sayHello
 * hosting#sayHello:firstName,lastName
 * hosting#sayHello:personName
 *
 * 抛出异常
 * hosting#expHello
 *
 * 灰度判断
 * hosting#grayJudge:judge
 *
 * 进出动作
 * hosting#enter
 * hosting#exit
 *
 * 迁移条件
 * hosting#transitCond:condition
 */
@Slf4j
@Component("hosting")
public class SpringHosting {
    public void sayHello() {
        log.info("Hello");
    }

    public void sayHello(String name) {
        log.info("Hello, {}", name);
    }

    public void sayHello(String firstName, String lastName) {
        log.info("Hello, {}・{}", lastName, firstName);
    }

    public void sayHello(PersonName personName) {
        log.info("Hello, {}・{}.And your age is {}, am I right?", personName.getLastName(), personName.getFirstName(), personName.getAge());
    }

    public void enter() {
        log.info("Enter");
    }

    public void exit() {
        log.info("Exit");
    }

    public void expHello() throws MyException {
        log.info("Hello, Exception");
        throw new MyException("Hello");
    }

    public boolean grayJudge(boolean judge) {
        log.info("judge = {}", judge);
        return judge;
    }

    public boolean transitCond(boolean condition) {
        log.info("condition = {}", condition);
        return condition;
    }
}
