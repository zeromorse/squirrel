package org.squirrelframework.foundation.event;

import java.lang.reflect.Method;

import org.squirrelframework.foundation.component.SquirrelConfiguration;
import org.squirrelframework.foundation.exception.ErrorCodes;
import org.squirrelframework.foundation.exception.SquirrelRuntimeException;
import org.squirrelframework.foundation.util.ReflectUtils;

import com.google.common.base.Preconditions;

/**
 * name：监听器方法
 * desc：保存有监听方法，并负责同步或异步地执行
 */
public class ListenerMethod {
    
    private final Class<?> eventType;

    private final Object target;

    private final Method method;
    
    private boolean hasParameter = false;
    
    private final boolean isAsync;
    
    @SuppressWarnings("unused")
    private final long timeout;
    
    public ListenerMethod(Class<?> eventType, Object listener, Method method) {
        Preconditions.checkArgument(eventType!=null && listener!=null && method!=null, "Parameters cannot be null.");
        // Checks that the object is of correct type
        Preconditions.checkArgument(method.getDeclaringClass().isAssignableFrom(listener.getClass()),
            "The method "+ method.getName() + " cannot be used for the given target: " + listener.getClass().getName()
        );
        
        this.eventType = eventType;
        this.target = listener;
        this.method = method;
        this.isAsync = AsyncEventListener.class.isAssignableFrom(listener.getClass());
        this.timeout = isAsync ? AsyncEventListener.class.cast(listener).timeout() : -1;
        
        final Class<?>[] params = method.getParameterTypes();
        // check parameter type
        if (params.length > 0) {
            if (params.length == 1 && params[0].isAssignableFrom(eventType)) {
                hasParameter = true;
            } else {
                throw new SquirrelRuntimeException(ErrorCodes.METHOD_UNEXPECTED_PARAMETERS);
            }
        } 
    }
    
    public void invokeMethod(final Object event) {
        // Only send events supported by the method
        Preconditions.checkArgument(eventType.isAssignableFrom(event.getClass()));
        if(isAsync) {
            SquirrelConfiguration.getExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    ReflectUtils.invoke(method, target, hasParameter ? new Object[] {event} : new Object[0]);
                }
            });
        } else {
            synchronized (target) {
                ReflectUtils.invoke(method, target, hasParameter ? new Object[] {event} : new Object[0]);
            }
        }
    }
    
    public boolean matches(Class<?> eventType, Object target) {
        return (this.target == target) && (eventType.equals(this.eventType));
    }
    
    public boolean matches(Class<?> eventType, Object target, Method method) {
        return (this.target == target) && (eventType.equals(this.eventType) && method.equals(this.method));
    }
    
    @Override
    public int hashCode() {
        int hash = eventType.hashCode();
        hash = 17 * hash + target.hashCode();
        hash = 17 * hash + method.hashCode();
        return hash;
    }
    
    public Class<?> getEventType() {
        return eventType;
    }
    
    public Object getTarget() {
        return target;
    }
    
    public Method getMethod() {
        return method;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        // return false if obj is a subclass (do not use instanceof check)
        if ((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        
        // obj is of same class, test it further
        ListenerMethod t = (ListenerMethod) obj;
        return (eventType == t.eventType || (eventType != null && eventType.equals(t.eventType)))
                && (target == t.target || (target != null && target.equals(t.target)))
                && (method == t.method || (method != null && method.equals(t.method)));
    }
    
    @Override
    public String toString() {
        return new StringBuilder(target.getClass().getSimpleName()).
                append('.').append(method.getName()).append('(').
                append(eventType.getSimpleName()).append(')').toString();
    }
}
