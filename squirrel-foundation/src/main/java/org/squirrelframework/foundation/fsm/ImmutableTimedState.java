package org.squirrelframework.foundation.fsm;

/**
 * desc：定时状态
 * note：没有过时，只是这些参数目前没有外界使用
 */
public interface ImmutableTimedState<T extends StateMachine<T, S, E, C>, S, E, C> extends ImmutableState<T, S, E, C> {
    
    long getTimeInterval();
    
    long getInitialDelay();
    
    E getAutoFireEvent();
    
    C getAutoFireContext();

}
