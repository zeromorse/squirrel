package io.github.zeromorse.fsm.context.invoker;

/**
 * 原始异常信息的包装
 */
public class OriginException extends RuntimeException {
    private Throwable origin; // 原始异常

    public OriginException(Throwable origin) {
        this.origin = origin;
    }

    public Throwable getOrigin() {
        return origin;
    }
}
