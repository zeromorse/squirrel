package io.github.zeromorse.fsm.context.config.loader.impl;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.sankuai.meituan.waimai.config.fsm.agent.context.config.cache.ConfigCache;
import com.sankuai.meituan.waimai.config.fsm.agent.context.config.cache.FsmConfig;
import com.sankuai.meituan.waimai.config.fsm.agent.context.config.cache.impl.FsmConfigInfo;
import com.sankuai.meituan.waimai.config.fsm.agent.context.config.loader.ConfigLoader;
import com.sankuai.meituan.waimai.config.fsm.agent.context.config.marker.ScxmlMarker;
import com.sankuai.meituan.waimai.config.fsm.agent.exception.CoreErrorCodes;
import com.sankuai.meituan.waimai.config.fsm.agent.exception.FsmAgentRuntimeException;
import com.sankuai.meituan.waimai.config.fsm.agent.util.ArrayUtil;
import com.sankuai.meituan.waimai.config.fsm.agent.util.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件配置载入器
 */
public class FileConfigLoader implements ConfigLoader {
    private static final Logger logger = LoggerFactory.getLogger(FileConfigLoader.class);

    private static final String RESOURCE_PATH = "fsm-local-config";
    private static final String META_FILE_NAME = "meta.json";
    private static final Pattern versionPattern = Pattern.compile("v(\\d)\\.(json|scxml)");

    FileParser fileParser;

    public FileConfigLoader() {
    }

    public void setScxmlMarker(ScxmlMarker scxmlMarker) {
        this.fileParser = new FileParser(scxmlMarker);
    }

    public void load(ConfigCache configCache) {
        logger.debug("load local config start...");

        // 加载资源
        Map<String, ConfigFile> configFileMap = resolveConfigFiles();
        if (configFileMap.isEmpty()) {
            logger.warn("can't load any config file, please check {}", RESOURCE_PATH);
            throw new FsmAgentRuntimeException(CoreErrorCodes.NO_VALID_CONFIG_FILE, RESOURCE_PATH);
        }
        logger.debug("resolve config files, configFileMap = {}", configFileMap);

        // 资源转化
        Map<String, FsmConfig> cache = new HashMap<>();
        for (Map.Entry<String, ConfigFile> entry : configFileMap.entrySet()) {
            try {
                FsmConfigInfo configInfo = fileParser.parseToFsmConfigInfo(entry.getValue());
                if (configInfo != null) {
                    cache.put(entry.getKey(), configInfo);
                }
            } catch (Exception e) {
                logger.warn("parse to fsmConfigInfo failure, the skipped fsm is {}", entry.getKey());
            }
        }

        // 检查资源
        if (cache.isEmpty()) {
            logger.warn("load config failure, please check");
            throw new FsmAgentRuntimeException(CoreErrorCodes.LOAD_CONFIG_FAILURE, "file");
        }
        logger.debug("load config finish, cache = {}", cache);

        configCache.putAll(cache);
    }

    /**
     * 在默认的资源路径下查找配置文件
     */
    @VisibleForTesting
    Map<String, ConfigFile> resolveConfigFiles() {
        String dirPath = IOUtil.getCanonicalPath(RESOURCE_PATH);
        File dir = new File(dirPath);
        if (!dir.isDirectory()) {
            logger.warn("{} should be a directory", RESOURCE_PATH);
            return Collections.emptyMap();
        }
        return resolveStringConfigFileMap(dir);
    }

    /**
     * 搜索目录，找到所有状态机的配置
     *
     * @param baseFsmDefDir 状态机配置的总目录
     * @return 状态机与其配置信息
     */
    Map<String, ConfigFile> resolveStringConfigFileMap(File baseFsmDefDir) {
        // 找资源路径的子目录
        File[] subDirs = baseFsmDefDir.listFiles();
        if (ArrayUtil.isEmpty(subDirs)) {
            return Collections.emptyMap();
        }

        Map<String, ConfigFile> retMap = new HashMap<>();
        // 子路径解析
        for (File fsmDefDir : subDirs) {
            logger.debug("resolve path {} for config file", fsmDefDir.getName());
            ConfigFile configFile = resolveConfigFile(fsmDefDir);
            if (configFile != null) {
                retMap.put(fsmDefDir.getName(), configFile);
            }
        }

        return retMap;
    }


    /**
     * 搜索目录，找到单个状态机的所有版本
     * 注意：ConfigFile.Version 不一定完整
     *
     * @param singleFsmDefDir 单个配置文件目录
     * @return 解析失败则返回空
     */
    @Nullable
    ConfigFile resolveConfigFile(File singleFsmDefDir) {
        // 目录校验
        if (!singleFsmDefDir.isDirectory()) {
            return null;
        }
        File[] fsmDefFiles = singleFsmDefDir.listFiles();
        if (ArrayUtil.isEmpty(fsmDefFiles)) {
            return null;
        }

        // 文件解析
        ArrayList<File> files = Lists.newArrayList(fsmDefFiles);
        // 寻找Meta文件
        Optional<File> metaFileOpt = Iterables.tryFind(files, new Predicate<File>() {
            @Override
            public boolean apply(File input) {
                return META_FILE_NAME.equals(input.getName());
            }
        });
        if (!metaFileOpt.isPresent()) {
            return null;
        }

        ConfigFile configFile = new ConfigFile();
        configFile.metaFile = metaFileOpt.get();

        // 寻找Version文件
        for (File file : files) {
            Matcher matcher = versionPattern.matcher(file.getName());
            if (!matcher.find()) {
                continue;
            }

            String version = matcher.group(1);
            int versionCode = Integer.parseInt(version);
            if (!configFile.versions.containsKey(versionCode)) {
                configFile.versions.put(versionCode, new ConfigFile.Version());
            }
            String type = matcher.group(2);
            if (type.equals("json")) {
                configFile.versions.get(versionCode).main = file;
            } else {
                configFile.versions.get(versionCode).scxml = file;
            }
        }

        return configFile;
    }

    @Override
    public boolean isReady() {
        return fileParser != null;
    }
}
