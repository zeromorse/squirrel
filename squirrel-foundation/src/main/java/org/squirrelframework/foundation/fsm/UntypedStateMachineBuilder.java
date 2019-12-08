package org.squirrelframework.foundation.fsm;

/**
 * desc：默认的状态机构建器，实现在静态代理中 {@link StateMachineBuilderFactory}
 */
public interface UntypedStateMachineBuilder extends StateMachineBuilder<UntypedStateMachine, Object, Object, Object> {
    
    <T extends UntypedStateMachine> T newUntypedStateMachine(Object initialStateId);
    
    <T extends UntypedStateMachine> T newUntypedStateMachine(Object initialStateId, Object... extraParams);
    
    <T extends UntypedStateMachine> T newUntypedStateMachine(
            Object initialStateId, StateMachineConfiguration configuration, Object... extraParams);
    
    <T> T newAnyStateMachine(Object initialStateId);
            
    <T> T newAnyStateMachine(Object initialStateId, Object... extraParams);
    
    <T> T newAnyStateMachine(Object initialStateId, StateMachineConfiguration configuration, Object... extraParams);
}
