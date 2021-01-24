package io.github.zeromorse.fsm.context.invoker.impl;

import io.github.zeromorse.fsm.context.invoker.OriginException;
import io.github.zeromorse.fsm.exception.FsmAgentRuntimeException;
import io.github.zeromorse.fsm.spring.bean.PersonName;
import io.github.zeromorse.fsm.util.JsonUtil;
import io.github.zeromorse.fsm.util.SpringBasedTest;
import org.junit.Before;
import org.junit.Test;

public class SpringMethodInvokerTest extends SpringBasedTest {

    private SpringMethodInvoker springMethodInvoker;

    @Before
    public void setUp() throws Exception {
        springMethodInvoker = new SpringMethodInvoker();
    }

    @Test
    public void invokeMultiParam() {
        // 多参数完全匹配
        {
            String signature = "hosting#sayHello:firstName,lastName";
            String argsStr = "{\"firstName\":\"Twain\",\"lastName\":\"Mark\"}";
            springMethodInvoker.invoke(signature, argsStr);
        }

        // 多参数部分匹配
        {
            String signature = "hosting#sayHello:firstName,lastName";
            String argsStr = "{\"firstName\":\"Twain\",\"lastName\":\"Mark\",\"age\":18}";
            springMethodInvoker.invoke(signature, argsStr);
        }
    }

    @Test
    public void invokeSingleParam() {
        // 方法单参数部分匹配
        {
            String signature = "hosting#sayHello:name";
            String argsStr = "{\"age\":18,\"name\":\"Wang\"}";
            springMethodInvoker.invoke(signature, argsStr);
        }

        // 方法单参数直接匹配
        {
            String signature = "hosting#sayHello:personName";
            String argsStr = "{\"firstName\":\"Twain\",\"lastName\":\"Mark\"}";
            springMethodInvoker.invoke(signature, argsStr);
        }
    }

    @Test(expected = FsmAgentRuntimeException.class)
    public void disMatch() {
        String signature = "hosting#sayHello:name";
        PersonName p = new PersonName();
        p.setAge(11);
        springMethodInvoker.invoke(signature, JsonUtil.toJSONString(p));
    }

    /**
     * 调用方法有异常
     * <p>
     */
    @Test(expected = OriginException.class)
    public void invokeWithExp() throws Throwable {
        String signature = "hosting#expHello";
        String argsStr = "{\"firstName\":\"Twain\",\"lastName\":\"Mark\"}";
        springMethodInvoker.invoke(signature, argsStr);
    }
}