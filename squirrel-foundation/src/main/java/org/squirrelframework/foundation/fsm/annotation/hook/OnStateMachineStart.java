package org.squirrelframework.foundation.fsm.annotation.hook;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * desc：当状态机开始时的行为
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface OnStateMachineStart {
    String when() default "";
}
