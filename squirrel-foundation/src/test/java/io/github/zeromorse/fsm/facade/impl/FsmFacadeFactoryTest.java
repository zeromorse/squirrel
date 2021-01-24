package io.github.zeromorse.fsm.facade.impl;

import io.github.zeromorse.fsm.context.FsmAgentConfiguration;
import io.github.zeromorse.fsm.facade.FsmFacade;
import io.github.zeromorse.fsm.facade.FsmMetaInfo;
import io.github.zeromorse.fsm.facade.FsmReqInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

@Slf4j
public class FsmFacadeFactoryTest {
    private FsmFacade fsmFacade;

    @Before
    public void setUp() throws Exception {
        // 设置代理，仅支持start方法
        FsmFacadeFactory.setFacadeProxy(new FsmFacadeFactory.FacadeProxy() {
            private FsmFacadeImpl fsmFacadeImpl;

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (fsmFacadeImpl == null) {
                    fsmFacadeImpl = new FsmFacadeImpl(configuration);
                }

                String methodName = method.getName();
                // 校验方法签名
                if (methodName.equals("start")
                        && args.length == 2
                        && FsmMetaInfo.class.isAssignableFrom(args[0].getClass())
                        && FsmReqInfo.class.isAssignableFrom(args[1].getClass())
                ) {
                    log.info("before start method");
                    return fsmFacadeImpl.start((FsmMetaInfo) args[0], (FsmReqInfo) args[1]);
                }

                throw new UnsupportedOperationException();
            }
        });

        fsmFacade = FsmFacadeFactory.create(new FsmAgentConfiguration.Builder().build());
    }

    @Test(expected = Exception.class)
    public void testSetFacadeProxy1() {
        fsmFacade.start(FsmMetaInfo.ofLatestVersion("key", "123"), FsmReqInfo.empty());
        // 应当会打印日志
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFacadeProxy2() {
        fsmFacade.fire(FsmMetaInfo.ofLatestVersion("key", "123"), FsmReqInfo.empty());
    }
}