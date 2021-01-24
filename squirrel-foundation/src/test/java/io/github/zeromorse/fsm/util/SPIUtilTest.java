package io.github.zeromorse.fsm.util;

import com.google.common.collect.Lists;
import io.github.zeromorse.fsm.context.model.GenericStateMachineEnhancer;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;

public class SPIUtilTest {

    @Test
    public void load() {
        Iterator<GenericStateMachineEnhancer> iter = SPIUtil.load(GenericStateMachineEnhancer.class);
        ArrayList<GenericStateMachineEnhancer> list = Lists.newArrayList(iter);
        Assert.assertEquals(1, list.size());
    }
}