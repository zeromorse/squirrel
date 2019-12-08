package org.squirrelframework.foundation.fsm;

import org.squirrelframework.foundation.component.SquirrelComponent;

import java.util.List;

/**
 * Action collection which insert new action in predefined order. 
 * 
 * @author Henry.He
 *
 * @param <T> type of State Machine
 * @param <S> type of State
 * @param <E> type of Event
 * @param <C> type of Context
 *
 * desc：动作集合
 */
public interface Actions<T extends StateMachine<T, S, E, C>, S, E, C> extends SquirrelComponent {

    /**
     * Add a new action
     * @param newAction new action
     */
    void add(Action<T, S, E, C> newAction);
    
    /**
     * Add a list of new actions
     * @param newActions new actions
     */
    void addAll(List<? extends Action<T, S, E, C>> newActions);
    
    /**
     * @return all sorted actions
     */
    List<Action<T, S, E, C>> getAll();
    
    /**
     * Remove all actions
     */
    void clear();
}
