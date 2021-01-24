package io.github.zeromorse.fsm.facade.impl;

import com.sankuai.meituan.waimai.config.fsm.agent.context.FsmAgentConfiguration;
import com.sankuai.meituan.waimai.config.fsm.agent.context.config.cache.FsmVersion;
import com.sankuai.meituan.waimai.config.fsm.agent.context.model.GenericContext;
import com.sankuai.meituan.waimai.config.fsm.agent.context.model.GenericStateMachine;
import com.sankuai.meituan.waimai.config.fsm.agent.context.model.GenericStateMachineFactory;
import com.sankuai.meituan.waimai.config.fsm.agent.facade.FsmFacade;
import com.sankuai.meituan.waimai.config.fsm.agent.facade.FsmMetaInfo;
import com.sankuai.meituan.waimai.config.fsm.agent.facade.FsmReqInfo;
import com.sankuai.meituan.waimai.config.fsm.agent.facade.FsmRespInfo;
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
