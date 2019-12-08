package org.squirrelframework.foundation.fsm;

/**
 * @author Henry.He
 *
 * note：访问者接口，用来标记访问者的行为，这里主要用来完成实例的序列化
 */
public interface Visitor {
    
    /**
     * @param visitable the element to be visited.
     */
    void visitOnEntry(StateMachine<?, ?, ?, ?> visitable);
    
    /**
     * @param visitable the element to be visited.
     */
    void visitOnExit(StateMachine<?, ?, ?, ?> visitable);
    
    /**
     * @param visitable the element to be visited.
     */
    void visitOnEntry(ImmutableState<?, ?, ?, ?> visitable);
    
    /**
     * @param visitable the element to be visited.
     */
    void visitOnExit(ImmutableState<?, ?, ?, ?> visitable);
    
    /**
     * @param visitable the element to be visited.
     */
    void visitOnEntry(ImmutableTransition<?, ?, ?, ?> visitable);
    
    /**
     * @param visitable the element to be visited.
     */
    void visitOnExit(ImmutableTransition<?, ?, ?, ?> visitable);
}
