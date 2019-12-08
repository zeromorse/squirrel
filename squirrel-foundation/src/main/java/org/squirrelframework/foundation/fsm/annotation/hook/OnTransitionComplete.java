package org.squirrelframework.foundation.fsm.annotation.hook;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * desc：当迁移完成时做什么
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface OnTransitionComplete {
    String when() default "";
}
