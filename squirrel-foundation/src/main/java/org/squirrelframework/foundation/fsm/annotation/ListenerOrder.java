package org.squirrelframework.foundation.fsm.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ListenerOrder can be used to insure listener invoked orderly
 * @author Henry.He
 *
 * desc：用于标记监听器的执行顺序
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ListenerOrder {
    int value();
}
