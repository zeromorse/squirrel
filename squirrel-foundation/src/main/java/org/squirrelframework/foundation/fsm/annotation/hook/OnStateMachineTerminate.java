package org.squirrelframework.foundation.fsm.annotation.hook;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * desc：当状态机终止时做什么
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface OnStateMachineTerminate {
    String when() default "";
}
