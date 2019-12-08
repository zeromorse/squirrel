package org.squirrelframework.foundation.fsm.annotation.structure;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * desc：一系列迁移定义
 */
@Target({TYPE})
@Retention(RUNTIME)
public @interface Transitions {
    
    /** (Required) An array of <code>Transit</code> annotations. */
    Transit[] value();
}
