package org.squirrelframework.foundation.fsm.builder;

import org.squirrelframework.foundation.fsm.StateMachine;

/**
 * desc：延迟绑定从句 from/fromAny...to/toAny...on/onAny...when...perform 中节点
 */
public interface DeferBoundActionTo<T extends StateMachine<T, S, E, C>, S, E, C> {

    /**
     * Build transition event
     * @param event transition event
     * @return On clause builder
     */
    On<T, S, E, C> on(E event);
    
    On<T, S, E, C> onAny();
    
}
