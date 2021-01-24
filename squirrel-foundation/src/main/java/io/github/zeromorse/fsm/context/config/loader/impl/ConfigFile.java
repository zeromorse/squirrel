package io.github.zeromorse.fsm.context.config.loader.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置文件
 */
class ConfigFile {
    File metaFile;
    Map<Integer, Version> versions = new HashMap<>();

    static class Version {
        File main; // 主配置文件
        File scxml; // scxml配置文件

        boolean isComplete() {
            return main != null && scxml != null;
        }

        public String toString() {
            return main + ";" + scxml;
        }
    }

    @Override
    public String toString() {
        return "ConfigFile{" +
                "metaFile=" + metaFile +
                ", versions=" + versions +
                '}';
    }
}