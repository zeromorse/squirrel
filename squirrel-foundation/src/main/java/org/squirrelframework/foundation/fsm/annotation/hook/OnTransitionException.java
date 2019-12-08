package org.squirrelframework.foundation.fsm.annotation.hook;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * desc：当迁移过程中有异常做什么
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface OnTransitionException {
    String when() default "";
}
