package io.github.zeromorse.fsm.context.config.cache;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置缓存
 */
public class ConfigCache {
    private Map<String, FsmConfig> delegator = new ConcurrentHashMap<>(); // 状态机的键 -> 状态机配置

    @Nullable
    public FsmConfig get(String key) {
        return delegator.get(key);
    }

    public Set<String> getKeys() {
        return delegator.keySet();
    }

    public void put(String key, FsmConfig fsmConfig) {
        delegator.put(key, fsmConfig);
    }

    public void putAll(Map<String, FsmConfig> fsmConfigs) {
        delegator.putAll(fsmConfigs);
    }

    @Override
    public String toString() {
        return delegator.toString();
    }
}
