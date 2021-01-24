package io.github.zeromorse.fsm.facade;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.sankuai.meituan.waimai.config.fsm.agent.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

/**
 * 状态触发的请求信息
 */
public class FsmReqInfo {
    private static final Logger logger = LoggerFactory.getLogger(FsmReqInfo.class);

    private static final FsmReqInfo EMPTY = new FsmReqInfo(null, null, null);

    private String curState; // 当前状态
    private String event; // 触发事件
    private Object ctx; // 上下文
    private String ctxJson; // JSON化的上下文

    private FsmReqInfo(String curState, String event, Object ctx) {
        this.curState = curState;
        this.event = event;
        this.ctx = ctx;
        this.ctxJson = JsonUtil.toJSONString(ctx);
    }

    public static FsmReqInfo empty() {
        return EMPTY;
    }

    public static FsmReqInfo forDefaultStart(@Nullable Object ctx) {
        return new FsmReqInfo(null, null, ctx);
    }

    public static FsmReqInfo forCustomStart(String curState, @Nullable Object ctx) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(curState), "curState must not empty");

        return new FsmReqInfo(curState, null, ctx);
    }

    public static FsmReqInfo forFire(String curState, String event, @Nullable Object ctx) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(curState), "curState must not empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(event), "event must not empty");

        return new FsmReqInfo(curState, event, ctx);
    }

    public String getCurState() {
        return curState;
    }

    public String getEvent() {
        return event;
    }

    public Object getCtx() {
        return ctx;
    }

    public String getCtxJson() {
        return ctxJson;
    }
}
