package org.squirrelframework.foundation.fsm.builder;

import org.squirrelframework.foundation.fsm.StateMachine;

/**
 * desc：延迟绑定从句 from/fromAny...to/toAny...on/onAny...when...perform 中节点
 */
public interface DeferBoundActionFrom<T extends StateMachine<T, S, E, C>, S, E, C> {
    
    /**
     * Build transition target state and return to clause builder
     * @param stateId id of state
     * @return To clause builder
     */
    DeferBoundActionTo<T, S, E, C> to(S stateId);
    
    DeferBoundActionTo<T, S, E, C> toAny();
    
}
