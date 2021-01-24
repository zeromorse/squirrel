package io.github.zeromorse.fsm.context.config.cache;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 状态机配置状态
 */
public final class VersionType {
    private static Map<String, String> _typeRef = Maps.newHashMap();

    static {
        _typeRef.put("main", "主用中");
        _typeRef.put("gray", "灰度中");
        _typeRef.put("archive", "已归档");
    }

    public static void append(String type, String desc) {
        _typeRef.put(type, desc);
    }

    public static Map<String, String> view() {
        return ImmutableMap.copyOf(_typeRef);
    }

    public static boolean isValid(String type) {
        return _typeRef.containsKey(type);
    }
}
