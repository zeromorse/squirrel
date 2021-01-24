package io.github.zeromorse.fsm.context.invoker;

/**
 * 方法执行器
 */
public interface MethodInvoker {

    /**
     * 执行方法，方法原始异常会被 {@link OriginException} 包裹
     *
     * @param signature 方法签名
     * @param argsStr   字符串化的入参
     */
    Object invoke(String signature, String argsStr);
}
