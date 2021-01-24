package io.github.zeromorse.fsm.facade;
import io.github.zeromorse.fsm.context.FsmAgentConfiguration;
import io.github.zeromorse.fsm.facade.impl.FsmFacadeFactory;
import io.github.zeromorse.fsm.spring.bean.PersonName;
import io.github.zeromorse.fsm.util.SpringBasedTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FsmFacadeTest extends SpringBasedTest {
    private final FsmMetaInfo metaInfo = FsmMetaInfo.ofLatestVersion("switch", "123");
    private FsmFacade fsmFacade;

    @Before
    public void setUp() throws Exception {
        FsmAgentConfiguration configuration = new FsmAgentConfiguration.Builder().build();
        fsmFacade = FsmFacadeFactory.create(configuration);
    }

    /**
     * 测试 start 方法与灰度
     */
    @Test
    public void start() {
        PersonName gray = genPersonName();
        gray.setJudge(true);
        FsmRespInfo grayStart = fsmFacade.start(metaInfo, FsmReqInfo.forDefaultStart(gray));
        Assert.assertEquals(3, grayStart.getVersion());
        Assert.assertEquals("on", grayStart.getCurState());

        PersonName main = genPersonName();
        main.setJudge(false);
        FsmRespInfo mainStart = fsmFacade.start(metaInfo, FsmReqInfo.forDefaultStart(main));
        Assert.assertEquals(2, mainStart.getVersion());
        Assert.assertEquals("off", mainStart.getCurState());
    }

    private PersonName genPersonName() {
        PersonName gray = new PersonName();
        gray.setFirstName("Button");
        gray.setLastName("Bob");
        gray.setAge(17);
        gray.setJudge(true);
        gray.setCondition(true);
        return gray;
    }

    /**
     * 测试 fire 方法
     */
    @Test
    public void fire() {
        {
            PersonName ctx = genPersonName();
            ctx.setJudge(false);
            FsmRespInfo resp = fsmFacade.fire(metaInfo, FsmReqInfo.forFire("on", "flip", ctx));
            Assert.assertEquals(2, resp.getVersion());
            Assert.assertEquals("off", resp.getCurState());
        }

        // 测试 condition失败
        {
            PersonName ctx = genPersonName();
            ctx.setJudge(true);
            ctx.setCondition(false);
            FsmRespInfo resp = fsmFacade.fire(metaInfo, FsmReqInfo.forFire("off", "flip", ctx));
            Assert.assertEquals(3, resp.getVersion());
            Assert.assertEquals("off", resp.getCurState());
        }
    }
}