package org.squirrelframework.foundation.fsm.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * desc：执行当前行为的前置条件
 */
@Retention(RUNTIME)
@Target({METHOD, TYPE})
@Documented
public @interface ExecuteWhen {
    String value();
}
