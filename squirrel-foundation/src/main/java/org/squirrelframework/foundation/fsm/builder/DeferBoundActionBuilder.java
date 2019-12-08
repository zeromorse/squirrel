package org.squirrelframework.foundation.fsm.builder;

import org.squirrelframework.foundation.fsm.StateMachine;

/**
 * desc：
 * 延迟绑定，相当于transit版的守护进程，为迁移切入附加操作，若迁移不存在则不触发
 * 从句为 from/fromAny...to/toAny...on/onAny...when...perform
 */
public interface DeferBoundActionBuilder<T extends StateMachine<T, S, E, C>, S, E, C> {
    
    public DeferBoundActionFrom<T, S, E, C> fromAny();
    
    public DeferBoundActionFrom<T, S, E, C> from(S from);

}
