package org.squirrelframework.foundation.fsm;

/**
 * desc：匿名条件
 */
public abstract class AnonymousCondition<T extends StateMachine<T, S, E, C>, S, E, C>
        implements Condition<T, S, E, C> {
    
    @Override
    public String name() {
        return getClass().getSimpleName();
    }
    
    @Override
    final public String toString() {
        return "instance#"+getClass().getName();
    }
}