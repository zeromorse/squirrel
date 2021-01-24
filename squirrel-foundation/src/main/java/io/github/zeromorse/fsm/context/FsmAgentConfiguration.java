package io.github.zeromorse.fsm.context;

import io.github.zeromorse.fsm.context.config.cache.ConfigCache;
import io.github.zeromorse.fsm.context.config.loader.ConfigLoader;
import io.github.zeromorse.fsm.context.config.loader.impl.FileConfigLoader;
import io.github.zeromorse.fsm.context.config.marker.ScxmlMarker;
import io.github.zeromorse.fsm.context.config.marker.impl.JDomScxmlMarker;
import io.github.zeromorse.fsm.context.finder.ConfigFinder;
import io.github.zeromorse.fsm.context.finder.impl.GrayFirstConfigFinder;
import io.github.zeromorse.fsm.context.invoker.MethodInvoker;
import io.github.zeromorse.fsm.context.invoker.impl.SpringMethodInvoker;
import io.github.zeromorse.fsm.exception.CoreErrorCodes;
import io.github.zeromorse.fsm.exception.FsmAgentRuntimeException;
import io.github.zeromorse.fsm.util.SPIUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 全局配置
 * 配置优先等级：用户自定义 > SPI获取 > 默认实现
 */
public class FsmAgentConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(FsmAgentConfiguration.class);

    private final ConfigCache configCache;
    private final ConfigLoader configLoader;
    private final MethodInvoker methodInvoker;
    private final ConfigFinder configFinder;
    private final ScxmlMarker scxmlMarker;

    private FsmAgentConfiguration(ConfigCache configCache, ConfigLoader configLoader, MethodInvoker methodInvoker, ConfigFinder configFinder, ScxmlMarker scxmlMarker) {
        this.configCache = configCache;
        this.configLoader = configLoader;
        this.methodInvoker = methodInvoker;
        this.configFinder = configFinder;
        this.scxmlMarker = scxmlMarker;

        // 加载缓存
        configLoader.load(configCache);
    }

    public ConfigCache getConfigCache() {
        return configCache;
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    public MethodInvoker getMethodInvoker() {
        return methodInvoker;
    }

    public ConfigFinder getConfigFinder() {
        return configFinder;
    }

    public ScxmlMarker getScxmlMarker() {
        return scxmlMarker;
    }

    /**
     * builder提供用户可定制化的能力
     */
    public static class Builder {
        private MethodInvoker methodInvoker;
        private ConfigFinder configFinder;
        private ConfigLoader configLoader;
        private ScxmlMarker scxmlMarker;

        public Builder scxmlMarker(ScxmlMarker scxmlMarker) {
            this.scxmlMarker = scxmlMarker;
            return this;
        }

        public Builder configLoader(ConfigLoader configLoader) {
            this.configLoader = configLoader;
            return this;
        }

        public Builder methodInvoker(MethodInvoker methodInvoker) {
            this.methodInvoker = methodInvoker;
            return this;
        }

        public Builder configFinder(ConfigFinder configFinder) {
            this.configFinder = configFinder;
            return this;
        }

        public FsmAgentConfiguration build() {
            ConfigCache configCache = new ConfigCache();

            if (scxmlMarker == null) {
                scxmlMarker = SPIFirstLoader.loadScxmlMarker();
            }

            if (configLoader == null) {
                configLoader = SPIFirstLoader.loadConfigLoader(scxmlMarker);
            }
            checkReady(configLoader);

            if (methodInvoker == null) {
                methodInvoker = SPIFirstLoader.loadMethodInvoker();
            }

            if (configFinder == null) {
                configFinder = SPIFirstLoader.loadConfigFinder(configCache, methodInvoker);
            }
            checkReady(configFinder);

            return new FsmAgentConfiguration(configCache, configLoader, methodInvoker, configFinder, scxmlMarker);
        }

        private void checkReady(PostProcess postProcess) {
            if (!postProcess.isReady()) {
                logger.error("component {} is not ready, please inject property", postProcess.getClass().getSimpleName());
                throw new FsmAgentRuntimeException(CoreErrorCodes.COMPONENT_NOT_READY, postProcess.getClass().getSimpleName());
            }
        }
    }

    /**
     * 加载组件，优先使用SPI
     */
    private static class SPIFirstLoader {
        static ScxmlMarker loadScxmlMarker() {
            return SPIUtil.load(ScxmlMarker.class, JDomScxmlMarker.class);
        }

        private static ConfigLoader loadConfigLoader(ScxmlMarker scxmlMarker) {
            ConfigLoader configLoader = SPIUtil.load(ConfigLoader.class, FileConfigLoader.class);
            configLoader.setScxmlMarker(scxmlMarker);
            return configLoader;
        }

        private static MethodInvoker loadMethodInvoker() {
            return SPIUtil.load(MethodInvoker.class, SpringMethodInvoker.class);
        }

        private static ConfigFinder loadConfigFinder(ConfigCache configCache, MethodInvoker methodInvoker) {
            ConfigFinder configFinder = SPIUtil.load(ConfigFinder.class, GrayFirstConfigFinder.class);
            configFinder.setConfigCache(configCache);
            configFinder.setMethodInvoker(methodInvoker);
            return configFinder;
        }
    }

}
