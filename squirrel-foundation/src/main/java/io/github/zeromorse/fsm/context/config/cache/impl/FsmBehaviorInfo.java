package io.github.zeromorse.fsm.context.config.cache.impl;

import io.github.zeromorse.fsm.context.config.cache.FsmBehavior;
import io.github.zeromorse.fsm.context.config.cache.FteTriple;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 状态机挂载行为默认实现
 */
public class FsmBehaviorInfo implements FsmBehavior {
    private Map<String, List<String>> entryActions = Collections.emptyMap(); // 状态进入动作列表，动作需有序
    private Map<String, List<String>> exitActions = Collections.emptyMap(); // 状态离开动作列表，动作需有序
    private Map<FteTriple, String> transitConditions = Collections.emptyMap(); // FTE到触发迁移条件
    private Map<FteTriple, List<String>> transitActions = Collections.emptyMap(); // FTE到迁移触发的行为

    @Nullable
    public String getTransitCondition(FteTriple fteTriple) {
        return transitConditions.get(fteTriple);
    }

    public List<String> getTransitAction(FteTriple fteTriple) {
        return transitActions.get(fteTriple);
    }

    @Override
    public String getTransitCondition(FteTriple fteTriple) {
        return null;
    }

    public List<String> getEntryAction(String state) {
        return entryActions.get(state);
    }

    public List<String> getExitAction(String state) {
        return exitActions.get(state);
    }

    public void setEntryActions(Map<String, List<String>> entryActions) {
        this.entryActions = entryActions;
    }

    public void setExitActions(Map<String, List<String>> exitActions) {
        this.exitActions = exitActions;
    }

    public void setTransitConditions(Map<FteTriple, String> transitConditions) {
        this.transitConditions = transitConditions;
    }

    public void setTransitActions(Map<FteTriple, List<String>> transitActions) {
        this.transitActions = transitActions;
    }

    @Override
    public String toString() {
        return "FsmBehaviorInfo{" +
                "entryActions=" + entryActions +
                ", exitActions=" + exitActions +
                ", transitConditions=" + transitConditions +
                ", transitActions=" + transitActions +
                '}';
    }
}
