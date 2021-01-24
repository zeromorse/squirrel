package io.github.zeromorse.fsm.context.config.cache;

/**
 * 状态机版本信息
 */
public interface FsmVersion {

    int getId();

    String getType();

    String getSqrlScxml();

    String getGrayCondition();

    FsmBehavior getBehavior();
}
