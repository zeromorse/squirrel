package org.squirrelframework.foundation.fsm.annotation.hook;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * desc：当动作执行异常了，应当如何处理
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface OnActionExecException {
    String when() default "";
}
