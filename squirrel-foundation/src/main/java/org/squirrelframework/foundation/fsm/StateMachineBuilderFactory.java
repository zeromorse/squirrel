package org.squirrelframework.foundation.fsm;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.squirrelframework.foundation.component.SquirrelProvider;
import org.squirrelframework.foundation.util.TypeReference;

/**
 * State machine builder factory to create the state machine builder. Here we use {@link SquirrelProvider} to create the 
 * builder, so user can register different implementation class of {@link StateMachineBuilder} to instantiate different 
 * builder. And also user can register different type of post processor to post process created builder.
 * 
 * @author Henry.He
 *
 * desc：状态机构建器工厂
 * note：代理模式
 */
public class StateMachineBuilderFactory {

    // - 注解版的实现 -

    public static UntypedStateMachineBuilder create(Class<? extends UntypedStateMachine> stateMachineClazz) {
        return create(stateMachineClazz, new Class[0]);
    }
    
    public static UntypedStateMachineBuilder create(Class<? extends UntypedStateMachine> stateMachineClazz, 
            Class<?>... extraConstParamTypes) {
        final StateMachineBuilder<UntypedStateMachine, Object, Object, Object> builder = 
                create(stateMachineClazz, Object.class, Object.class, Object.class, extraConstParamTypes);
        return create(builder);
    }

    /**
     * 使用代理模式完成无类型状态机构建器的生成
     */
    public static UntypedStateMachineBuilder create(
            final StateMachineBuilder<UntypedStateMachine, Object, Object, Object> builder) {
        return (UntypedStateMachineBuilder) Proxy.newProxyInstance(
                UntypedStateMachineBuilder.class.getClassLoader(), 
                new Class[]{UntypedStateMachineBuilder.class}, 
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args)
                            throws Throwable {
                        try {
                            String methodName = method.getName();
                            if(methodName.equals("newUntypedStateMachine") || methodName.equals("newAnyStateMachine")) {
                                Object fsmInstance = null;
                                if(args.length==3 && StateMachineConfiguration.class.isAssignableFrom(args[1].getClass()) && 
                                        (args[2]==null || args[2].getClass().isArray())) {
                                    fsmInstance = builder.newStateMachine(args[0], 
                                            (StateMachineConfiguration)args[1], (Object[])args[2]);
                                } else if(args.length==2 && (args[1]==null || args[1].getClass().isArray())) {
                                    fsmInstance = builder.newStateMachine(args[0], (Object[])args[1]);
                                } else if(args.length==1) {
                                    fsmInstance = builder.newStateMachine(args[0]);
                                } else {
                                    throw new IllegalArgumentException("Illegal argument number.");
                                }
                                return fsmInstance;
                            }
                            return method.invoke(builder, args);
                        } catch(InvocationTargetException e) {
                            throw e.getTargetException();
                        }
                    }
                });
    }

    // - 泛型版的实现 -

    public static <T extends StateMachine<T, S, E, C>, S, E, C> StateMachineBuilder<T, S, E, C> create(
            Class<? extends T> stateMachineClazz, Class<S> stateClazz, Class<E> eventClazz, Class<C> contextClazz) {
        return create(stateMachineClazz, stateClazz, eventClazz, contextClazz, new Class<?>[0]);
    }
    
    public static <T extends StateMachine<T, S, E, C>, S, E, C> StateMachineBuilder<T, S, E, C> create(
            Class<? extends T> stateMachineClazz, Class<S> stateClazz, Class<E> eventClazz, Class<C> contextClazz, 
            Class<?>... extraConstParamTypes) {
        return SquirrelProvider.getInstance().newInstance(new TypeReference<StateMachineBuilder<T, S, E, C>>() {}, 
                new Class[] { Class.class, Class.class, Class.class, Class.class, Class[].class }, 
                new Object[] { stateMachineClazz, stateClazz, eventClazz, contextClazz, extraConstParamTypes });
    }
}
