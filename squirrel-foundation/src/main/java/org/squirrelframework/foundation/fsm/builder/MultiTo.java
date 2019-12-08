package org.squirrelframework.foundation.fsm.builder;

import org.squirrelframework.foundation.fsm.StateMachine;

/**
 * Created by kailianghe on 7/12/14.
 *
 * desc：添加一个状态向多个状态的迁移，from...toAmong...onEach...when...perform 子句的组成
 */
public interface MultiTo<T extends StateMachine<T, S, E, C>, S, E, C> {
    /**
     * Build transition event
     * @param events transition event
     * @return On clause builder
     */
    On<T, S, E, C> onEach(E... events);
}
