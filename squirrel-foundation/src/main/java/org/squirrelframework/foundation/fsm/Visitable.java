package org.squirrelframework.foundation.fsm;

/**
 * @author Henry.He
 *
 * note：访问者模式，用来标记被访问对象，这里主要用来完成实例的序列化
 */
public interface Visitable {
    /**
     * Accepts a {@link org.squirrelframework.foundation.fsm.Visitor}.
     *
     * @param visitor the visitor.
     */
    void accept(final Visitor visitor);
}
