package io.github.zeromorse.fsm.exception;

/**
 * 为了不与状态机引擎中CODE冲突，采用
 * CODE编码：20000-29999
 */
public enum CoreErrorCodes implements ErrorCode {


    METHOD_NOT_FOUND(20000, "method not found，with signature '%s' by '%s'"),
    PARSE_JSON_ERROR(20001, "parse json error, original json is '%s'"),
    METHOD_INACCESSIBLE(20002, "access method failure, target method is '%s'"),
    OBJECT_NOT_FOUND(20003, "object not found，with signature '%s' by '%s'"),
    BUILD_FSM_FAILURE(20004, "build fsm failure，sqrl-scxml is '%s'"),
    CONFIG_NOT_FOUND(20005, "fsm config not found, label is '%s'"),
    JSONIZE_FAILURE(20006, "jsonize failure, source is '%s'"),
    CONFIG_VERSION_NOT_FOUND(20007, "fsm config version not found, label is '%s' and version is '%s'"),
    NO_VALID_CONFIG_FILE(20008, "no valid fsm config file, please check files in '%s'"),
    ELEMENT_EXCESS_ERROR(20009, "size of %s element excess bound, limit is '%s'"),
    PARSE_XML_ERROR(20010, "parse xml error, original source is '%s'"),
    LOAD_CONFIG_FAILURE(20011, "load config failure, config source is '%s'"),
    COMPONENT_NOT_READY(20012, "component '%s' is not ready, please check property"),

    UNKNOWN_ERROR(29999, "unknown error，please contact developer");

    private final int code;

    private final String description;

    private CoreErrorCodes(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}
