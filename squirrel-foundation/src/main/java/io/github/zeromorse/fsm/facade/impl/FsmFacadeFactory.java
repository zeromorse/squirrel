package io.github.zeromorse.fsm.facade.impl;


import io.github.zeromorse.fsm.context.FsmAgentConfiguration;
import io.github.zeromorse.fsm.facade.FsmFacade;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class FsmFacadeFactory {
    private static FacadeProxy facadeProxy;

    public static FsmFacade create(final FsmAgentConfiguration configuration) {
        if (facadeProxy != null) {
            facadeProxy.configuration = configuration;
            return (FsmFacade) Proxy.newProxyInstance(FsmFacade.class.getClassLoader(),
                    new Class[]{FsmFacade.class},
                    facadeProxy);
        }

        return new FsmFacadeImpl(configuration);
    }

    static void setFacadeProxy(FacadeProxy facadeProxy) {
        FsmFacadeFactory.facadeProxy = facadeProxy;
    }

    /**
     * 静态代理执行器，供扩展 FsmFacade 默认实现
     */
    static abstract class FacadeProxy implements InvocationHandler {
        FsmAgentConfiguration configuration;
    }
}
