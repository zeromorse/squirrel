package io.github.zeromorse.fsm.exception;

/**
 * 标准错误码
 */
public interface ErrorCode {

    /**
     * 错误描述
     */
    String getDescription();

    /**
     * 错误代码
     */
    int getCode();
}
