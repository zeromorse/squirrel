package org.squirrelframework.foundation.fsm.annotation.hook;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * desc；当动作执行之前的行为
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface OnBeforeActionExecuted {
    String when() default "";
}
