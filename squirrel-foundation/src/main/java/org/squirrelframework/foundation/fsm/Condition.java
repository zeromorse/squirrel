package org.squirrelframework.foundation.fsm;

/**
 * A constraint which must evaluate to true after the trigger occurs in order for the transition to complete.
 * 
 * @author Henry.He
 *
 * @param <C> type of context
 *
 * desc：触发迁移需要满足的条件
 */
public interface Condition<T extends StateMachine<T, S, E, C>, S, E, C> {
    /**
     * @return whether the context satisfied current condition
     */
    boolean isSatisfied(S from, S to, E event, C context, T stateMachine);
    
    String name();
}
