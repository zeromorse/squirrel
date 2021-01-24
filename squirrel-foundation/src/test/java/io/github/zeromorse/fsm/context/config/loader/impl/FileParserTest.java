package io.github.zeromorse.fsm.context.config.loader.impl;

import com.google.common.collect.Lists;
import io.github.zeromorse.fsm.context.config.cache.FsmBehavior;
import io.github.zeromorse.fsm.context.config.cache.FteTriple;
import io.github.zeromorse.fsm.context.config.cache.impl.FsmVersionInfo;
import io.github.zeromorse.fsm.context.config.marker.ScxmlMarker;
import io.github.zeromorse.fsm.context.config.marker.impl.JDomScxmlMarker;
import io.github.zeromorse.fsm.util.IOUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

@Slf4j
public class FileParserTest {
    private ScxmlMarker scxmlMarker = new JDomScxmlMarker();
    private FileParser fileParser = new FileParser(scxmlMarker);

    @Test
    public void test() {
        String v1JsonPath = IOUtil.getCanonicalPath("fsm-local-config/switch/v1.json");
        File v1JsonFile = new File(v1JsonPath);

        String v1ScxmlPath = IOUtil.getCanonicalPath("fsm-local-config/switch/v1.scxml");
        File v1ScxmlFile = new File(v1ScxmlPath);

        FsmVersionInfo versionInfo = fileParser.parseToFsmVersionInfo(v1JsonFile, v1ScxmlFile);

        Assert.assertEquals(1, versionInfo.getId());
        Assert.assertEquals("archive", versionInfo.getType());
        Assert.assertEquals("gray", versionInfo.getGrayCondition());
        Assert.assertNotNull(versionInfo.getSqrlScxml());
        log.debug(versionInfo.getSqrlScxml());

        FsmBehavior behavior = versionInfo.getBehavior();
        Assert.assertEquals(Lists.newArrayList("exit", "exit2"), behavior.getExitAction("off"));
        Assert.assertEquals(Lists.newArrayList("entry", "entry2"), behavior.getEntryAction("off"));

        FteTriple fte = new FteTriple("off", "on", "flip");
        Assert.assertEquals(Lists.newArrayList("transit", "transit2"), behavior.getTransitAction(fte));
        Assert.assertEquals("condition", behavior.getTransitCondition(fte));

        FteTriple reversedFte = new FteTriple("on", "off", "flip");
        Assert.assertNull("condition", behavior.getTransitCondition(reversedFte));
    }
}