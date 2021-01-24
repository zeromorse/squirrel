package io.github.zeromorse.fsm.context.invoker.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.sankuai.meituan.waimai.config.fsm.agent.context.invoker.MethodInvoker;
import com.sankuai.meituan.waimai.config.fsm.agent.context.invoker.OriginException;
import com.sankuai.meituan.waimai.config.fsm.agent.exception.CoreErrorCodes;
import com.sankuai.meituan.waimai.config.fsm.agent.exception.FsmAgentRuntimeException;
import com.sankuai.meituan.waimai.config.fsm.agent.util.JsonUtil;
import com.sankuai.meituan.waimai.config.fsm.agent.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Spring托管bean的方法执行器
 */
public class SpringMethodInvoker implements MethodInvoker {
    private static final Logger logger = LoggerFactory.getLogger(SpringMethodInvoker.class);
    private static final LocalVariableTableParameterNameDiscoverer paramNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private static final Joiner commaJoiner = Joiner.on(',');
    private ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();

    public SpringMethodInvoker() {
    }

    public Object invoke(String signature, String argsStr) {
        logger.debug("invoke method start, signature = {}, argsStr = {}", signature, argsStr);
        // 解析签名
        MethodSignature ms = MethodSignature.of(signature);
        // 寻找BEAN
        Object obj = getTargetObject(ms);
        // 寻找方法
        Method targetMethod = findTargetMethod(ms, obj);
        // 参数映射
        Object[] args = paramMapping(ms, argsStr, targetMethod);
        // 执行方法
        Object ret = doInvoke(obj, targetMethod, args);
        logger.debug("invoke method finish, signature = {}, argsStr = {}, ret = {}", signature, argsStr, ret);
        return ret;
    }

    Object doInvoke(Object obj, Method targetMethod, Object[] args) {
        try {
            if (!targetMethod.isAccessible()) {
                targetMethod.setAccessible(true);
            }
            return targetMethod.invoke(obj, args);
        } catch (IllegalAccessException e) {
            logger.error("Cannot access method, targetMethod = {}", targetMethod, e);
            throw new FsmAgentRuntimeException(e, CoreErrorCodes.METHOD_INACCESSIBLE, targetMethod);
        } catch (InvocationTargetException e) {
            String realArgsStr = Arrays.toString(args);
            logger.warn("Invoke method failure, targetMethod = {}, obj = {}, args = {}", targetMethod, obj, realArgsStr, e);
            throw new OriginException(e.getCause());
        }
    }

    Object[] paramMapping(MethodSignature ms, String argsStr, Method targetMethod) {
        Class<?>[] parameterTypes = targetMethod.getParameterTypes();
        if (parameterTypes.length == 0) {
            return new Object[0];
        }

        List<String> argNameList = ms.getArgNameList();
        Object[] args = new Object[argNameList.size()];
        JsonNode rootJsonNode = JsonUtil.toJSONTree(argsStr);

        if (argNameList.size() == 1) { // 仅有一个参数的处理
            // 优先拿子节点中的对象去拼装，若失败则将上下文直接转化为入参
            String argName = argNameList.get(0);
            JsonNode argRaw = rootJsonNode.get(argName);
            Class<?> argClazz = parameterTypes[0];
            args[0] = JsonUtil.toValue(argRaw != null ? argRaw.toString() : argsStr, argClazz);
        } else { // 有多个参数则从rootJsonNode中按名字匹配
            for (int i = 0; i < argNameList.size(); i++) {
                String argName = argNameList.get(i);
                JsonNode argRaw = rootJsonNode.get(argName);
                logger.debug("{}:{}:{}", i, argName, argRaw);
                args[i] = (argRaw == null) ? null : JsonUtil.toValue(argRaw.toString(), parameterTypes[i]);
            }
        }
        return args;
    }

    Object getTargetObject(MethodSignature ms) {
        String beanName = ms.getBeanName();
        Object obj;
        try {
            obj = applicationContext.getBean(beanName);
        } catch (BeansException e) {
            logger.error("Target bean not found, the required is {}", beanName);
            throw new FsmAgentRuntimeException(e, CoreErrorCodes.OBJECT_NOT_FOUND, ms.getOriginal(), this.getClass().getSimpleName());
        }
        return obj;
    }

    Method findTargetMethod(MethodSignature ms, Object obj) {
        Method targetMethod = null;
        for (Method method : obj.getClass().getMethods()) {
            String[] parameterNames = paramNameDiscoverer.getParameterNames(method);
            String curParamName = commaJoiner.join(MoreObjects.firstNonNull(parameterNames, new String[0]));
            if (curParamName.equals(ms.getArgsName()) && method.getName().equals(ms.getMethodName())) {
                targetMethod = method;
                break;
            }
        }
        if (targetMethod == null) {
            logger.error("Target method not found, the required is {}", ms.getMethodAndArgsName());
            throw new FsmAgentRuntimeException(CoreErrorCodes.METHOD_NOT_FOUND, ms.getOriginal(), this.getClass().getSimpleName());
        }
        return targetMethod;
    }

    /**
     * 方法签名分析
     * <p>
     * 签名组成 beanName#method:arg1,arg2,...
     * ctx仅支持基本类型和POJO，不支持Map等复杂数据结构
     * 注：不支持方法重载
     */
    static final class MethodSignature {
        private static final Splitter sharpSplitter = Splitter.on('#');
        private static final Splitter colonSplitter = Splitter.on(':');
        private static final Splitter commaSplitter = Splitter.on(',');

        private final String original; // 原始签名
        private final String beanName;
        private final String methodName;
        private final String argsName; // optional

        private MethodSignature(String original, String beanName, String methodName, String argsName) {
            this.original = original;
            this.beanName = beanName;
            this.methodName = methodName;
            this.argsName = argsName;
        }

        private static MethodSignature of(String signature) {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(signature), "signature couldn't be empty");

            String argsName = "";
            List<String> l1 = sharpSplitter.splitToList(signature);
            String beanName = l1.get(0);
            List<String> l2 = colonSplitter.splitToList(l1.get(1));
            String methodName = l2.get(0);
            if (l2.size() > 1) {
                argsName = l2.get(1);
            }

            return new MethodSignature(signature, beanName, methodName, argsName);
        }

        String getOriginal() {
            return original;
        }

        String getBeanName() {
            return beanName;
        }

        public String getMethodName() {
            return methodName;
        }

        String getArgsName() {
            return argsName;
        }

        String getMethodAndArgsName() {
            return methodName + ":" + argsName;
        }

        List<String> getArgNameList() {
            return commaSplitter.splitToList(argsName);
        }
    }
}
