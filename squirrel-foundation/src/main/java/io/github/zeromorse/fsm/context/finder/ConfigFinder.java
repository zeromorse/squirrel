package io.github.zeromorse.fsm.context.finder;

import com.sankuai.meituan.waimai.config.fsm.agent.context.PostProcess;
import com.sankuai.meituan.waimai.config.fsm.agent.context.config.cache.ConfigCache;
import com.sankuai.meituan.waimai.config.fsm.agent.context.config.cache.FsmVersion;
import com.sankuai.meituan.waimai.config.fsm.agent.context.invoker.MethodInvoker;
import com.sankuai.meituan.waimai.config.fsm.agent.facade.FsmMetaInfo;

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
