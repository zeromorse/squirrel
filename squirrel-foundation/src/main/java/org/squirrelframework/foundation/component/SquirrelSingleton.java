package org.squirrelframework.foundation.component;

/**
 * Interface to flag a class which has a static setInstance/getInstance method which can be used by an external software
 * to set a specific implementation of a component, or interface which has a static field named INSTANCE.
 * 
 * @author Henry.He
 *
 * name：SQRL单例标志接口
 * note：使用 kotlin 的 object 改写的话，可以避免这种口头约定
 */
public interface SquirrelSingleton {
}
