package org.squirrelframework.foundation.fsm;

/**
 * desc：匿名条件
 */
public abstract class AnonymousCondition<C> implements Condition<C> {
    
    @Override
    public String name() {
        return getClass().getSimpleName();
    }
    
    @Override
    final public String toString() {
        return "instance#"+getClass().getName();
    }
}