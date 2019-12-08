package org.squirrelframework.foundation.fsm.builder;

import org.squirrelframework.foundation.fsm.StateMachine;

/**
 * Created by kailianghe on 7/12/14.
 *
 * desc：创建多个迁移时的快捷构建器
 */
public interface MultiTransitionBuilder<T extends StateMachine<T, S, E, C>, S, E, C> {

    /**
     * Build transition source state.
     * @param stateId id of state
     * @return multi from clause builder
     *
     * desc：添加一个状态向多个状态的迁移，from...toAmong...onEach...when...perform 子句中的开端
     */
    MultiFrom<T, S, E, C> from(S stateId);

    /**
     * Build transition source states.
     * @param stateIds id of states
     * @return single from clause builder
     *
     * desc：添加多个状态向一个状态的迁移，fromAmong...to...on...when...perform 子句
     */
    From<T, S, E, C> fromAmong(S... stateIds);

    /**
     * Build mutual transitions between two state
     * @param fromStateId from state id
     * @return between clause builder
     *
     * desc：添加两个状态间的互相迁移，between...and...onMutual 子句中的开端
     */
    Between<T, S, E, C> between(S fromStateId);
}
