package io.github.zeromorse.fsm.util;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 方法签名生成器
 */
@Slf4j
public class SpringMethodSignature {
    private static final LocalVariableTableParameterNameDiscoverer paramNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private static final Joiner commaJoiner = Joiner.on(',');
    private static final ImmutableList objectMethods
            = ImmutableList.of("getClass", "hashCode", "equals", "toString", "notify", "notifyAll", "wait");

    /**
     * 发现特定方法
     */
    public static String discover(Class clazz, String methodName) {
        String beanName = getBeanName(clazz);

        Method method = null;
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(methodName)) {
                method = m;
                break;
            }
        }
        if (method == null) {
            throw new RuntimeException("can't find method");
        }

        return getSignature(beanName, method);
    }

    /**
     * 发现所有方法
     */
    public static List<String> discover(Class clazz) {
        String beanName = getBeanName(clazz);

        List<String> list = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            if (objectMethods.contains(method.getName())) {
                continue;
            }
            list.add(getSignature(beanName, method));
        }

        return list;
    }

    private static String getSignature(String beanName, Method method) {
        if (method.getParameterTypes().length == 0) {
            return beanName + "#" + method.getName();
        } else {
            return beanName + "#" + method.getName() + ":" + commaJoiner.join(paramNameDiscoverer.getParameterNames(method));
        }
    }

    private static String getBeanName(Class clazz) {
        String beanName;
        Annotation anno = clazz.getAnnotation(Component.class);
        if (anno != null) {
            beanName = ((Component) anno).value();
        } else {
            beanName = StringUtils.uncapitalize(clazz.getSimpleName());
        }
        return beanName;
    }
}
