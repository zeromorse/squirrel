package org.squirrelframework.foundation.fsm;

/**
 * desc：迁移的优先级
 */
public interface TransitionPriority {

    public static final int NORMAL = 1;
    
    public static final int MIDDLE = 100;
    
    public static final int HIGH = 1000;
    
    public static final int HIGHEST = 10000;
}
