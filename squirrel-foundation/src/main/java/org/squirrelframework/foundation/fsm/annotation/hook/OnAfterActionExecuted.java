package org.squirrelframework.foundation.fsm.annotation.hook;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * desc：标记动作执行之后的行为
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface OnAfterActionExecuted {
    String when() default "";
}
