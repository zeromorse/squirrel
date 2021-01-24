package io.github.zeromorse.fsm.context.config.cache.impl;


import io.github.zeromorse.fsm.context.config.cache.FsmBehavior;
import io.github.zeromorse.fsm.context.config.cache.FsmVersion;

/**
 * 状态机版本默认实现
 */
public class FsmVersionInfo implements FsmVersion {
    private int id; // 版本ID
    private String type; // 当前版本类型
    private String scxml; // 状态机结构
    private String sqrlScxml; // 运行时状态机结构
    private String grayCondition; // 灰度判断签名，null表示不存在
    private FsmBehavior behavior; // 挂载逻辑

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSqrlScxml() {
        return sqrlScxml;
    }

    public void setSqrlScxml(String sqrlScxml) {
        this.sqrlScxml = sqrlScxml;
    }

    public String getGrayCondition() {
        return grayCondition;
    }

    public void setGrayCondition(String grayCondition) {
        this.grayCondition = grayCondition;
    }

    public FsmBehavior getBehavior() {
        return behavior;
    }

    public void setBehavior(FsmBehavior behavior) {
        this.behavior = behavior;
    }

    public String getScxml() {
        return scxml;
    }

    public void setScxml(String scxml) {
        this.scxml = scxml;
    }

    @Override
    public String toString() {
        return "FsmVersionInfo{" +
                "id=" + id +
                ", type=" + type +
                ", scxml='" + scxml + '\'' +
                ", sqrlScxml='" + sqrlScxml + '\'' +
                ", grayCondition='" + grayCondition + '\'' +
                ", behavior=" + behavior +
                '}';
    }
}
