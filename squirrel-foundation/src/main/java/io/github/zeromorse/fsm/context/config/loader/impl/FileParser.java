package io.github.zeromorse.fsm.context.config.loader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.github.zeromorse.fsm.context.config.cache.FsmVersion;
import io.github.zeromorse.fsm.context.config.cache.FteTriple;
import io.github.zeromorse.fsm.context.config.cache.VersionType;
import io.github.zeromorse.fsm.context.config.cache.impl.FsmBehaviorInfo;
import io.github.zeromorse.fsm.context.config.cache.impl.FsmConfigInfo;
import io.github.zeromorse.fsm.context.config.cache.impl.FsmVersionInfo;
import io.github.zeromorse.fsm.context.config.marker.ScxmlMarker;
import io.github.zeromorse.fsm.exception.CoreErrorCodes;
import io.github.zeromorse.fsm.exception.FsmAgentRuntimeException;
import io.github.zeromorse.fsm.util.IOUtil;
import io.github.zeromorse.fsm.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件解析器
 */
class FileParser {
    private static final Logger logger = LoggerFactory.getLogger(FileParser.class);

    private ScxmlMarker scxmlMarker;
    private Function<JsonNode, String> toTextFunction = new Function<JsonNode, String>() {
        @Nullable
        @Override
        public String apply(JsonNode input) {
            return input.asText();
        }
    };

    FileParser(ScxmlMarker scxmlMarker) {
        this.scxmlMarker = scxmlMarker;
    }

    /**
     * 从配置文件中获取配置信息
     *
     * @return 配置项；null-无效配置
     */
    @Nullable
    FsmConfigInfo parseToFsmConfigInfo(ConfigFile configFile) {
        FsmConfigInfo fsmConfigInfo = new FsmConfigInfo();

        File metaFile = configFile.metaFile;
        fillUpMetaInfo(fsmConfigInfo, metaFile);

        Map<Integer, ConfigFile.Version> versionFiles = configFile.versions;
        fillUpVersionInfo(fsmConfigInfo, versionFiles);

        // 一个可用版本都没有的话，则认为是无效配置
        if (fsmConfigInfo.getVersions().isEmpty()) {
            return null;
        }

        return fsmConfigInfo;
    }

    /**
     * 填充配置的版本信息
     *
     * @param fsmConfigInfo 配置信息
     * @param versionFiles  版本文件
     */
    void fillUpVersionInfo(FsmConfigInfo fsmConfigInfo, Map<Integer, ConfigFile.Version> versionFiles) {
        Map<Integer, FsmVersion> versions = new HashMap<>();
        for (Map.Entry<Integer, ConfigFile.Version> entry : versionFiles.entrySet()) {
            Integer code = entry.getKey();
            ConfigFile.Version version = entry.getValue();

            if (!version.isComplete()) {
                continue;
            }

            FsmVersionInfo fsmVersionInfo;
            try {
                fsmVersionInfo = parseToFsmVersionInfo(version.main, version.scxml);
            } catch (Exception e) {
                logger.warn("parse to fsmVersionInfo failure, skip version {}", code, e);
                continue;
            }

            versions.put(code, fsmVersionInfo);
        }
        fsmConfigInfo.setVersions(versions);
    }

    /**
     * 填充配置相关的元信息
     *
     * @param fsmConfigInfo 配置信息
     * @param metaFile      元信息文件
     */
    void fillUpMetaInfo(FsmConfigInfo fsmConfigInfo, File metaFile) {
        JsonNode jsonRoot = JsonUtil.toJSONTree(metaFile);
        try {
            fsmConfigInfo.setKey(jsonRoot.get("key").asText());
            fsmConfigInfo.setMainVerId(jsonRoot.get("mainVerId").asInt());
            JsonNode grayVerIdNode = jsonRoot.get("grayVerId");
            if (grayVerIdNode != null) {
                fsmConfigInfo.setGrayVerId(grayVerIdNode.asInt());
            }
        } catch (Exception e) {
            logger.error("parsing json to build fsmVersionInfo failure, json is {}", jsonRoot, e);
            throw new FsmAgentRuntimeException(CoreErrorCodes.PARSE_JSON_ERROR, jsonRoot);
        }
    }

    /**
     * 根据两个文件来解析为 {@link FsmVersionInfo}
     * 注意：解析过程中的异常会抛出 {@link FsmAgentRuntimeException}
     */
    @VisibleForTesting
    FsmVersionInfo parseToFsmVersionInfo(File jsonFile, File scxmlFile) {
        // 为什么不用JSON直接转化？
        // 本地手工配置文件质量难以把控，使用jsonTree的方式容错更高，也更易于扩展，原理同beanCopy
        JsonNode jsonRoot = JsonUtil.toJSONTree(jsonFile);

        FsmVersionInfo versionInfo = new FsmVersionInfo();
        try {
            versionInfo.setId(jsonRoot.get("id").asInt());

            String type = jsonRoot.get("type").asText();
            if (!VersionType.isValid(type)) {
                throw new IllegalStateException("状态机配置文件类型非法，非法类型为" + type);
            }
            versionInfo.setType(type);

            JsonNode grayConditionNode = jsonRoot.get("grayCondition");
            String grayCondition = null;
            if (grayConditionNode != null && !Strings.isNullOrEmpty(grayConditionNode.asText())) {
                grayCondition = grayConditionNode.asText();
            }
            versionInfo.setGrayCondition(grayCondition);

            JsonNode behavior = jsonRoot.get("behavior");
            Map<String, List<String>> entryActions = new HashMap<>();
            Map<String, List<String>> exitActions = new HashMap<>();
            JsonNode states = behavior.get("states");
            for (JsonNode singleState : states) {
                Iterable<String> entries = Iterables.transform(singleState.get("entryActions"), toTextFunction);
                Iterable<String> exits = Iterables.transform(singleState.get("exitActions"), toTextFunction);

                String stateName = singleState.get("name").asText();
                entryActions.put(stateName, Lists.newArrayList(entries));
                exitActions.put(stateName, Lists.newArrayList(exits));
            }

            Map<FteTriple, List<String>> transitActions = new HashMap<>();
            Map<FteTriple, String> transitionConditions = new HashMap<>();
            JsonNode transitions = behavior.get("transitions");
            for (JsonNode singleTransition : transitions) {
                Iterable<String> transits = Iterables.transform(singleTransition.get("transitActions"), toTextFunction);
                String to = singleTransition.get("to").asText();
                String from = singleTransition.get("from").asText();
                String event = singleTransition.get("event").asText();
                FteTriple fte = new FteTriple(from, to, event);
                transitActions.put(fte, Lists.newArrayList(transits));

                JsonNode condNode = singleTransition.get("condition");
                if (condNode != null && !Strings.isNullOrEmpty(condNode.asText())) {
                    String condition = condNode.asText();
                    transitionConditions.put(fte, condition);
                }
            }

            FsmBehaviorInfo behaviorInfo = new FsmBehaviorInfo();
            behaviorInfo.setEntryActions(entryActions);
            behaviorInfo.setExitActions(exitActions);
            behaviorInfo.setTransitActions(transitActions);
            behaviorInfo.setTransitConditions(transitionConditions);

            versionInfo.setBehavior(behaviorInfo);
        } catch (Exception e) {
            logger.error("parsing json to build fsmVersionInfo failure, json is {}", jsonRoot, e);
            throw new FsmAgentRuntimeException(e, CoreErrorCodes.PARSE_JSON_ERROR, jsonRoot);
        }

        // 装饰scxml
        try {
            FileInputStream is = new FileInputStream(scxmlFile);

            String scxml = IOUtil.is2String(is);
            versionInfo.setScxml(scxml);

            String sqrlScxml = scxmlMarker.decorate(scxml);
            versionInfo.setSqrlScxml(sqrlScxml);
        } catch (FileNotFoundException e) {
            logger.error("scxml file {} not found，really weird! ", scxmlFile);
            throw new FsmAgentRuntimeException(e, CoreErrorCodes.UNKNOWN_ERROR);
        }

        return versionInfo;
    }
}
