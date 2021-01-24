package io.github.zeromorse.fsm.context.config.loader;


import io.github.zeromorse.fsm.context.PostProcess;
import io.github.zeromorse.fsm.context.config.cache.ConfigCache;
import io.github.zeromorse.fsm.context.config.marker.ScxmlMarker;

/**
 * 配置加载器
 */
public interface ConfigLoader extends PostProcess {
    /**
     * 加载
     *
     * @param configCache 状态机配置
     */
    void load(ConfigCache configCache);

    // - 属性注入 -
    void setScxmlMarker(ScxmlMarker scxmlMarker);
}
