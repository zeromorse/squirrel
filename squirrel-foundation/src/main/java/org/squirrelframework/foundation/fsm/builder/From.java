package org.squirrelframework.foundation.fsm.builder;

import org.squirrelframework.foundation.fsm.StateMachine;

/**
 * From clause builder which is used to build transition target state.
 * 
 * @author Henry.He
 *
 * @param <T> type of State Machine
 * @param <S> type of State
 * @param <E> type of Event
 * @param <C> type of Context
 *
 * desc：添加一个状态迁移，From...To...On...When...Perform 子句中的一部分
 */
public interface From<T extends StateMachine<T, S, E, C>, S, E, C> {
    /**
     * Build transition target state and return to clause builder
     * @param stateId id of state
     * @return To clause builder
     */
    To<T, S, E, C> to(S stateId);
    
    /**
     * Builder transition target state as final state and return to clause builder
     * @param stateId id of state
     * @return To clause builder
     */
    To<T, S, E, C> toFinal(S stateId);
}
