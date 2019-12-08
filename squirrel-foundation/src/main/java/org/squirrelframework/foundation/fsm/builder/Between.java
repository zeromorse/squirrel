package org.squirrelframework.foundation.fsm.builder;

import org.squirrelframework.foundation.fsm.StateMachine;

/**
 * Created by kailianghe on 7/12/14.
 *
 * desc：添加两个状态互相转化的事件，Between...And...OnMutual 子句中的一部分
 */
public interface Between<T extends StateMachine<T, S, E, C>, S, E, C> {
    /**
     * Build mutual transitions between state
     * @param toStateId to state id
     * @return and clause builder
     */
    And<T, S, E, C> and(S toStateId);
}
