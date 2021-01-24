package io.github.zeromorse.fsm.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zeromorse.fsm.exception.CoreErrorCodes;
import io.github.zeromorse.fsm.exception.FsmAgentRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

public final class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String toJSONString(@Nullable Object obj) {
        if (obj == null) {
            return "{}";
        }

        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("parse object to json failure, object is {}", obj);
            throw new FsmAgentRuntimeException(e, CoreErrorCodes.JSONIZE_FAILURE, obj);
        }
    }

    public static JsonNode toJSONTree(String jsonStr) {
        try {
            return objectMapper.readTree(jsonStr);
        } catch (IOException e) {
            logger.error("jsonize string content failure, string is {}", jsonStr);
            throw new FsmAgentRuntimeException(CoreErrorCodes.JSONIZE_FAILURE, jsonStr);
        }
    }

    public static JsonNode toJSONTree(File jsonFile) {
        try {
            return objectMapper.readTree(jsonFile);
        } catch (IOException e) {
            logger.error("jsonize file content failure, file is {}", jsonFile);
            throw new FsmAgentRuntimeException(CoreErrorCodes.JSONIZE_FAILURE, jsonFile);
        }
    }

    public static <T> T toValue(String jsonStr, Class<T> valueClazz) {
        try {
            return objectMapper.readValue(jsonStr, valueClazz);
        } catch (IOException e) {
            logger.error("Parse JSON failure", e);
            throw new FsmAgentRuntimeException(e, CoreErrorCodes.PARSE_JSON_ERROR, jsonStr);
        }
    }
}
