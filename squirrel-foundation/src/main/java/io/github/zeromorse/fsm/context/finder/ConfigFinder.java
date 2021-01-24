package io.github.zeromorse.fsm.context.finder;

import io.github.zeromorse.fsm.context.PostProcess;
import io.github.zeromorse.fsm.context.config.cache.ConfigCache;
import io.github.zeromorse.fsm.context.config.cache.FsmVersion;
import io.github.zeromorse.fsm.context.invoker.MethodInvoker;
import io.github.zeromorse.fsm.facade.FsmMetaInfo;

/**
 * 配置寻找器
 */
public interface ConfigFinder extends PostProcess {
    /**
     * 搜寻特定的配置版本
     *
     * @param meta      状态机配置元信息
     * @param assistant 辅助信息
     */
    FsmVersion searchFsmVersion(FsmMetaInfo meta, String assistant);

    // - 属性注入 -
    void setConfigCache(ConfigCache configCache);

    void setMethodInvoker(MethodInvoker methodInvoker);
}
