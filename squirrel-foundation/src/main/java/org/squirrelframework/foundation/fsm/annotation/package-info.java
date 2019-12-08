/**
 * note：
 * 使用注解标记替代接口实现，辅助声明钩子方法
 * 许多带有 `String when() default "";` 实现的方法，可以采用 `extends` 特定接口辅助声明
 */
package org.squirrelframework.foundation.fsm.annotation;