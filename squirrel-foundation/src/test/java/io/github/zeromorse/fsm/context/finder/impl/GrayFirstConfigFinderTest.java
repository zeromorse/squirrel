package io.github.zeromorse.fsm.context.finder.impl;

import io.github.zeromorse.fsm.context.config.cache.ConfigCache;
import io.github.zeromorse.fsm.context.config.cache.FsmConfig;
import io.github.zeromorse.fsm.context.config.cache.FsmVersion;
import io.github.zeromorse.fsm.context.config.cache.impl.FsmConfigInfo;
import io.github.zeromorse.fsm.context.config.cache.impl.FsmVersionInfo;
import io.github.zeromorse.fsm.context.invoker.impl.SpringMethodInvoker;
import io.github.zeromorse.fsm.facade.FsmMetaInfo;
import io.github.zeromorse.fsm.spring.bean.PersonName;
import io.github.zeromorse.fsm.spring.bean.SpringHosting;
import io.github.zeromorse.fsm.util.JsonUtil;
import io.github.zeromorse.fsm.util.SpringBasedTest;
import io.github.zeromorse.fsm.util.SpringMethodSignature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class GrayFirstConfigFinderTest extends SpringBasedTest {
    private final FsmVersionInfo mainVersion = new FsmVersionInfo();
    private final FsmVersionInfo grayVersion = new FsmVersionInfo();
    private final String fsmKey = "fsmKey";
    private GrayFirstConfigFinder grayFirstConfigFinder;

    @Before
    public void setUp() throws Exception {
        ConfigCache configCache = new ConfigCache();
        fillUp(configCache);

        grayFirstConfigFinder = new GrayFirstConfigFinder();
        grayFirstConfigFinder.setConfigCache(configCache);
        grayFirstConfigFinder.setMethodInvoker(new SpringMethodInvoker());

        if (!grayFirstConfigFinder.isReady()) {
            throw new RuntimeException();
        }
    }

    private void fillUp(ConfigCache configCache) {
        grayVersion.setGrayCondition(SpringMethodSignature.discover(SpringHosting.class, "grayJudge"));

        HashMap<Integer, FsmVersion> versions = new HashMap<>();
        versions.put(1, mainVersion);
        versions.put(2, grayVersion);

        FsmConfigInfo fsmConfigInfo = new FsmConfigInfo();
        fsmConfigInfo.setKey(fsmKey);
        fsmConfigInfo.setMainVerId(1);
        fsmConfigInfo.setGrayVerId(2);
        fsmConfigInfo.setVersions(versions);

        HashMap<String, FsmConfig> fsmConfigs = new HashMap<>();
        fsmConfigs.put(fsmKey, fsmConfigInfo);
        configCache.putAll(fsmConfigs);
    }

    @Test
    public void searchFsmVersion() {
        FsmMetaInfo meta = FsmMetaInfo.ofLatestVersion(fsmKey, "1");

        PersonName grayVo = new PersonName();
        grayVo.setJudge(true);
        FsmVersion gv = grayFirstConfigFinder.searchFsmVersion(meta, JsonUtil.toJSONString(grayVo));
        Assert.assertEquals(grayVersion, gv);

        PersonName mainVo = new PersonName();
        mainVo.setJudge(false);
        FsmVersion mv = grayFirstConfigFinder.searchFsmVersion(meta, JsonUtil.toJSONString(mainVo));
        Assert.assertEquals(mainVersion, mv);
    }
}