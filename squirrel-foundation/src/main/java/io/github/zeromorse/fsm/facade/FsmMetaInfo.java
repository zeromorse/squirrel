package io.github.zeromorse.fsm.facade;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * 状态机元信息
 */
public class FsmMetaInfo {
    private static final int latestVersion = 0; // 表示最新版本
    private String label; // 状态机标签，标识了当前操作的状态机
    private int version; // 状态机版本，默认为最新版本
    private String bizUniqId; // 业务侧唯一ID标识

    private FsmMetaInfo(String label, int version, String bizUniqId) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(label), "label must not empty");
        Preconditions.checkArgument(version >= 0, "version must not be less than zero");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(bizUniqId), "bizUniqId must not empty");

        this.label = label;
        this.version = version;
        this.bizUniqId = bizUniqId;
    }

    public static FsmMetaInfo ofLatestVersion(String label, String bizUniqId) {
        return new FsmMetaInfo(label, latestVersion, bizUniqId);
    }

    public static FsmMetaInfo of(String label, int version, String bizUniqId) {
        return new FsmMetaInfo(label, version, bizUniqId);
    }

    public String getLabel() {
        return label;
    }

    public int getVersion() {
        return version;
    }

    public String getBizUniqId() {
        return bizUniqId;
    }

    public boolean isLateVersion() {
        return version == latestVersion;
    }

    @Override
    public String toString() {
        return "FsmMetaInfo{" +
                "label='" + label + '\'' +
                ", version=" + version +
                ", bizUniqId='" + bizUniqId + '\'' +
                '}';
    }
}
