package io.github.zeromorse.fsm.context.config.cache.impl;


import io.github.zeromorse.fsm.context.config.cache.FsmConfig;
import io.github.zeromorse.fsm.context.config.cache.FsmVersion;

import java.util.Collections;
import java.util.Map;

/**
 * 状态机配置默认实现
 */
public class FsmConfigInfo implements FsmConfig {
    private static final int INVALID = -1;

    private String key; // 状态机的键，需要同一个应用下不重复
    private int mainVerId; // 主版本ID
    private int grayVerId = INVALID; // 灰度版本ID，可选
    private Map<Integer, FsmVersion> versions = Collections.emptyMap(); // 版本ID到版本配置

    public String getKey() {
        return key;
    }

    public int getMainVerId() {
        return mainVerId;
    }

    public int getGrayVerId() {
        return grayVerId;
    }

    /**
     * 是否有灰度版本？
     */
    public boolean hasGrayVersion() {
        return grayVerId != INVALID;
    }

    public FsmVersion getVersion(int verId) {
        return versions.get(verId);
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setMainVerId(int mainVerId) {
        this.mainVerId = mainVerId;
    }

    public void setGrayVerId(int grayVerId) {
        this.grayVerId = grayVerId;
    }

    public void setVersions(Map<Integer, FsmVersion> versions) {
        this.versions = versions;
    }

    public Map<Integer, FsmVersion> getVersions() {
        return versions;
    }

    @Override
    public String toString() {
        return "FsmConfigInfo{" +
                "key='" + key + '\'' +
                ", mainVerId=" + mainVerId +
                ", grayVerId=" + grayVerId +
                ", versions=" + versions +
                '}';
    }
}
