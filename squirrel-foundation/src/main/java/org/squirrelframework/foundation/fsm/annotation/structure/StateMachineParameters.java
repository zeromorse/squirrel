package org.squirrelframework.foundation.fsm.annotation.structure;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * desc：状态机参数配置
 */
@Retention(RUNTIME)
@Target({TYPE})
@Documented
public @interface StateMachineParameters {
    Class<?> stateType();
    Class<?> eventType();
    Class<?> contextType();
}
