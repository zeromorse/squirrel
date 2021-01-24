package io.github.zeromorse.fsm.context.model;

import com.sankuai.meituan.waimai.config.fsm.agent.context.FsmAgentConfiguration;
import com.sankuai.meituan.waimai.config.fsm.agent.facade.FsmMetaInfo;

public class GenericContext {
    private FsmAgentConfiguration configuration;
    private FsmMetaInfo meta;
    private String argStr;
    private String fromState = "";

    public GenericContext(FsmAgentConfiguration configuration, FsmMetaInfo meta, String argStr, String formState) {
        this.configuration = configuration;
        this.meta = meta;
        this.argStr = argStr;
        this.fromState = formState;
    }

    public FsmAgentConfiguration getConfiguration() {
        return configuration;
    }

    public String getArgStr() {
        return argStr;
    }

    public FsmMetaInfo getMeta() {
        return meta;
    }

    public String getFromState() {
        return fromState;
    }
}
