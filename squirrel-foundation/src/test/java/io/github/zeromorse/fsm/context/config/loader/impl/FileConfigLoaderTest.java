package io.github.zeromorse.fsm.context.config.loader.impl;

import com.google.common.collect.Sets;
import io.github.zeromorse.fsm.context.config.marker.impl.JDomScxmlMarker;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Map;

@Slf4j
public class FileConfigLoaderTest {
    private FileConfigLoader fileConfigLoader = new FileConfigLoader();

    {
        fileConfigLoader.setScxmlMarker(new JDomScxmlMarker());
    }

    @Test
    public void testResolveConfigFiles() {
        Map<String, ConfigFile> configFiles = fileConfigLoader.resolveConfigFiles();
        String Switch = "switch";
        String betterSwitch = "betterSwitch";
        String badSwitch = "badSwitch";

        {
            HashSet<String> expected = Sets.newHashSet(Switch, betterSwitch, badSwitch);
            Assert.assertTrue(Sets.difference(expected, configFiles.keySet()).isEmpty());
        }

        ConfigFile switchConfig = configFiles.get(Switch);
        {
            HashSet<Integer> expected = Sets.newHashSet(1, 2, 3);
            Assert.assertTrue(Sets.difference(expected, switchConfig.versions.keySet()).isEmpty());
            for (ConfigFile.Version version : switchConfig.versions.values()) {
                Assert.assertTrue(version.isComplete());
            }
        }

        ConfigFile badSwitchConfig = configFiles.get(badSwitch);
        {
            Assert.assertNotNull(badSwitchConfig.metaFile);
            Assert.assertEquals(1, badSwitchConfig.versions.size());
            ConfigFile.Version version = badSwitchConfig.versions.get(1);
            Assert.assertFalse(version.isComplete());
        }
    }

}