package org.squirrelframework.foundation.fsm.annotation.hook;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Transition end listener annotation
 * @author Henry.He
 *
 * desc：当迁移结束时做什么
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface OnTransitionEnd {
    String when() default "";
}
