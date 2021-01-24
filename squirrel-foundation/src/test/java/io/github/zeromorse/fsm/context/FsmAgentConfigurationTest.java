package io.github.zeromorse.fsm.context;

import com.google.common.collect.Sets;
import io.github.zeromorse.fsm.context.config.cache.ConfigCache;
import io.github.zeromorse.fsm.util.SpringBasedTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class FsmAgentConfigurationTest extends SpringBasedTest {
    @Test
    public void testCacheLoaded() {
        FsmAgentConfiguration configuration = new FsmAgentConfiguration.Builder().build();
        ConfigCache configCache = configuration.getConfigCache();
        Assert.assertEquals(Sets.newHashSet("switch", "betterSwitch"), configCache.getKeys());
    }
}