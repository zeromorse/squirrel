package io.github.zeromorse.fsm.context.finder.impl;

import com.sankuai.meituan.waimai.config.fsm.agent.context.config.cache.ConfigCache;
import com.sankuai.meituan.waimai.config.fsm.agent.context.config.cache.FsmConfig;
import com.sankuai.meituan.waimai.config.fsm.agent.context.config.cache.FsmVersion;
import com.sankuai.meituan.waimai.config.fsm.agent.context.finder.ConfigFinder;
import com.sankuai.meituan.waimai.config.fsm.agent.context.invoker.MethodInvoker;
import com.sankuai.meituan.waimai.config.fsm.agent.exception.CoreErrorCodes;
import com.sankuai.meituan.waimai.config.fsm.agent.exception.FsmAgentRuntimeException;
import com.sankuai.meituan.waimai.config.fsm.agent.facade.FsmMetaInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrayFirstConfigFinder implements ConfigFinder {
    private static final Logger logger = LoggerFactory.getLogger(GrayFirstConfigFinder.class);

    ConfigCache configCache;
    MethodInvoker methodInvoker;

    public GrayFirstConfigFinder() {
    }

    public FsmVersion searchFsmVersion(FsmMetaInfo meta, String assistant) {
        logger.debug("search fsm version start, meta = {}, assistant = {}", meta, assistant);
        // 查找状态机
        FsmConfig fsmConfig = findFsmConfig(meta.getLabel());

        // 查找版本
        FsmVersion fsmVersion = findFsmVersion(meta, assistant, fsmConfig);
        logger.debug("search fsm version finish, meta = {}, fsmVersion = {}", meta, fsmVersion);
        return fsmVersion;
    }

    @Override
    public void setConfigCache(ConfigCache configCache) {
        this.configCache = configCache;
    }

    @Override
    public void setMethodInvoker(MethodInvoker methodInvoker) {
        this.methodInvoker = methodInvoker;
    }

    /**
     * 寻找状态机版本
     */
    FsmVersion findFsmVersion(FsmMetaInfo meta, String ctxJson, FsmConfig fsmConfig) {
        logger.debug("find fsm version start, meta = {}, ctxJson = {}, fsmConfig = {}", meta, ctxJson, fsmConfig);
        String label = meta.getLabel();
        int version = meta.getVersion();

        FsmVersion targetVersion;
        if (meta.isLateVersion()) {
            // 先尝试灰度策略，命中则走灰度，否则走全量
            if (fsmConfig.hasGrayVersion()) {
                FsmVersion grayVersion = fsmConfig.getVersion(fsmConfig.getGrayVerId());
                String graySignature = grayVersion.getGrayCondition();
                Object ret = methodInvoker.invoke(graySignature, ctxJson);
                if (((boolean) ret)) {
                    targetVersion = grayVersion;
                } else {
                    targetVersion = fsmConfig.getVersion(fsmConfig.getMainVerId());
                }
            } else {
                targetVersion = fsmConfig.getVersion(fsmConfig.getMainVerId());
            }
        } else {
            // 直接触发特定版本，不考虑灰度策略，找不到策略则报错
            targetVersion = fsmConfig.getVersion(version);
            if (targetVersion == null) {
                logger.error("can't find fsmVersion from cache, label is {}, versionCode is {}", label, version);
                throw new FsmAgentRuntimeException(CoreErrorCodes.CONFIG_VERSION_NOT_FOUND, label, version);
            }
        }
        return targetVersion;
    }

    /**
     * 寻找状态机配置
     */
    FsmConfig findFsmConfig(String label) {
        FsmConfig fsmConfig = configCache.get(label);
        if (fsmConfig == null) {
            logger.error("can't find fsmConfig from cache, label is {}", label);
            throw new FsmAgentRuntimeException(CoreErrorCodes.CONFIG_NOT_FOUND, label);
        }
        return fsmConfig;
    }

    @Override
    public boolean isReady() {
        return configCache != null && methodInvoker != null;
    }
}
