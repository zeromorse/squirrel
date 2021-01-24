package io.github.zeromorse.fsm.facade;

/**
 * 触发状态机的响应信息
 */
public class FsmRespInfo {
    private String curState; // 当前状态
    private int version; // 当前采用的状态机版本

    public FsmRespInfo(String curState, int version) {
        this.curState = curState;
        this.version = version;
    }

    public String getCurState() {
        return curState;
    }

    public int getVersion() {
        return version;
    }
}
