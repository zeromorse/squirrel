package io.github.zeromorse.fsm.facade.impl;

import io.github.zeromorse.fsm.context.FsmAgentConfiguration;
import io.github.zeromorse.fsm.context.config.cache.FsmVersion;
import io.github.zeromorse.fsm.context.model.GenericContext;
import io.github.zeromorse.fsm.context.model.GenericStateMachine;
import io.github.zeromorse.fsm.context.model.GenericStateMachineFactory;
import io.github.zeromorse.fsm.facade.FsmFacade;
import io.github.zeromorse.fsm.facade.FsmMetaInfo;
import io.github.zeromorse.fsm.facade.FsmReqInfo;
import io.github.zeromorse.fsm.facade.FsmRespInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FsmFacadeImpl implements FsmFacade {
    private static final Logger logger = LoggerFactory.getLogger(FsmFacadeImpl.class);

    private FsmAgentConfiguration configuration;

    public FsmFacadeImpl(FsmAgentConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public FsmRespInfo start(FsmMetaInfo meta, FsmReqInfo req) {
        GenericStateMachine fsm = startGenericStateMachine(meta, req, "");
        String currentState = fsm.getCurrentState();
        return new FsmRespInfo(currentState, fsm.getVersion().getId());
    }

    @Override
    public FsmRespInfo fire(FsmMetaInfo meta, FsmReqInfo req) {
        GenericStateMachine fsm = startGenericStateMachine(meta, req, req.getCurState());

        // 触发事件
        fsm.fire(req.getEvent(), new GenericContext(configuration, meta, req.getCtxJson(), req.getCurState()));
        String currentState = fsm.getCurrentState();
        return new FsmRespInfo(currentState, fsm.getVersion().getId());
    }

    private GenericStateMachine startGenericStateMachine(FsmMetaInfo meta, FsmReqInfo req, String formState) {
        logger.debug("start statemachine, meta = {}, req = {}", meta, req);
        String ctxJson = req.getCtxJson();
        FsmVersion targetVersion = configuration.getConfigFinder().searchFsmVersion(meta, ctxJson);

        // 创建状态机并启动
        GenericStateMachine fsm = GenericStateMachineFactory.create(targetVersion.getSqrlScxml(), meta.getBizUniqId(), req.getCurState());
        fsm.setVersion(targetVersion);
        fsm.start(new GenericContext(configuration, meta, ctxJson, formState));
        return fsm;
    }
}
