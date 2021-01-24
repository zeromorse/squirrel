package io.github.zeromorse.fsm.context.config.cache;

/**
 * 单个状态机配置
 */
public interface FsmConfig {

    int getMainVerId();

    int getGrayVerId();

    /**
     * 是否有灰度版本？
     */
    boolean hasGrayVersion();

    FsmVersion getVersion(int verId);
}
