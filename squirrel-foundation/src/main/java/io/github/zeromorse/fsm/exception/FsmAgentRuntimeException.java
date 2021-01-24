package io.github.zeromorse.fsm.exception;

public class FsmAgentRuntimeException extends RuntimeException {
    private int errorCodeId;

    private Throwable targetException;

    private String errorMessage;

//    private String localizedErrorMessage;

    public FsmAgentRuntimeException(ErrorCode errorCode) {
        this.errorCodeId = errorCode.getCode();
        this.errorMessage = String.format("%08d : %s.", getErrorCodeId(), errorCode.getDescription());
    }

    public FsmAgentRuntimeException(Throwable targetException, ErrorCode errorCode) {
        this(errorCode);
        this.targetException = targetException;
    }

    public FsmAgentRuntimeException(ErrorCode errorCode, Object... parameters) {
        this.errorCodeId = errorCode.getCode();
        this.errorMessage = String.format("%08d : %s.", getErrorCodeId(),
                String.format(errorCode.getDescription(), parameters));
    }

    public FsmAgentRuntimeException(Throwable targetException, ErrorCode errorCode, Object... parameters) {
        this(errorCode, parameters);
        this.targetException = targetException;
    }

    public int getErrorCodeId() {
        return errorCodeId;
    }

    public Throwable getTargetException() {
        return targetException;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
