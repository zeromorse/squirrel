package io.github.zeromorse.fsm.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JsonUtilTest {
    @Test
    public void toJSONString() {
        String s = JsonUtil.toJSONString(null);
        JsonNode node = JsonUtil.toJSONTree(s);
        assertNotNull(node);
        assertTrue(node.isEmpty(null));
    }
}