package org.squirrelframework.foundation.fsm.builder;

import org.squirrelframework.foundation.fsm.StateMachine;

/**
 * Created by kailianghe on 7/12/14.
 *
 * desc：添加一个状态向多个状态的迁移，from...toAmong...onEach...when...perform 子句的组成
 */
public interface MultiFrom<T extends StateMachine<T, S, E, C>, S, E, C> {

    /**
     * Build transition target states and return to clause builder
     * @param stateIds id of states
     * @return To clause builder
     */
    MultiTo<T, S, E, C> toAmong(S... stateIds);
}
