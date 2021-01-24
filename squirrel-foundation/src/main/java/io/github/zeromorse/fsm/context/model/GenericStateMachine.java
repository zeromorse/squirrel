package io.github.zeromorse.fsm.context.model;

import com.sankuai.meituan.waimai.config.fsm.agent.context.config.cache.FsmVersion;
import org.squirrelframework.foundation.fsm.impl.AbstractStateMachine;

public class GenericStateMachine extends AbstractStateMachine<GenericStateMachine, String, String, GenericContext> {
    private FsmVersion version; // 状态机对应配置的版本

    public FsmVersion getVersion() {
        return version;
    }

    public void setVersion(FsmVersion version) {
        this.version = version;
    }
}