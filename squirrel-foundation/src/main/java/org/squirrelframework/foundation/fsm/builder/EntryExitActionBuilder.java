package org.squirrelframework.foundation.fsm.builder;

import org.squirrelframework.foundation.fsm.StateMachine;

/**
 * State action builder including entry action and exit action
 * 
 * @author Henry.He
 *
 * @param <T> type of State Machine
 * @param <S> type of State
 * @param <E> type of Event
 * @param <C> type of Context
 *
 * desc：出入状态动作构建器
 */
public interface EntryExitActionBuilder<T extends StateMachine<T, S, E, C>, S, E, C> extends When<T, S, E, C> {
}
