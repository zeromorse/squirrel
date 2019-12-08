package org.squirrelframework.foundation.component;

/**
 * Post process object created by {@link SquirrelProvider}
 * 
 * @author Henry.He
 *
 * @param <T> type of object to be processed
 *
 * name：SQRL后处理标准
 */
public interface SquirrelPostProcessor<T> {

    /**
     * Post process created component
     * @param component created component
     */
    void postProcess(T component);
}
