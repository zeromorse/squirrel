package io.github.zeromorse.fsm.facade;

public interface FsmFacade {
    /**
     * 启动状态机
     *
     * @return 初始状态
     */
    FsmRespInfo start(FsmMetaInfo meta, FsmReqInfo req);

    /**
     * 触发状态迁移
     *
     * @return 目标状态，若迁移条件不满足，则返回原状态
     */
    FsmRespInfo fire(FsmMetaInfo meta, FsmReqInfo req);
}
