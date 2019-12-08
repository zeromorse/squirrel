package org.squirrelframework.foundation.component;

import com.google.common.base.Predicate;
import org.squirrelframework.foundation.event.ListenerMethod;
import org.squirrelframework.foundation.event.SquirrelEvent;

import java.lang.reflect.Method;

/**
 * This interface represents an observable object, or "data" in the subject-observer paradigm. 
 * It can be implemented to represent an object that the observer wants to have listened.
 * 
 * @author Henry.He
 *
 * name：被观察者接口
 */
public interface Observable {

    /**
     * @return whether the subject is notifiable
     */
    boolean isNotifiable();

    /**
     * Set notifiable of subject
     * @param notifiable
     */
    void setNotifiable(boolean notifiable);

    /**
     * Add listener to observable subject.
     * @param eventType type of event
     * @param listener listener object
     * @param method listener method
     */
    void addListener(Class<?> eventType, Object listener, Method method);

    /**
     * Add listener to observable subject.
     * @param eventType type of event
     * @param listener listener object
     * @param methodName name of listener method
     */
    void addListener(Class<?> eventType, Object listener, String methodName);

    void removeListener(Predicate<ListenerMethod> predicate);

    /**
     * Remove listener from observable subject.
     * @param eventType type of event
     * @param listener listener object
     * @param method listener method
     */
    void removeListener(Class<?> eventType, Object listener, Method method);

    /**
     * Remove listener to observable subject.
     * @param eventType type of event
     * @param listener listener object
     * @param methodName name of listener method
     */
    void removeListener(Class<?> eventType, Object listener, String methodName);

    /**
     * Add listener to observable subject.
     * @param eventType type of event
     * @param listener listener object
     */
    void removeListener(Class<?> eventType, Object listener);

    /**
     * Remove all listeners
     */
    void removeAllListeners();

    /**
     * Fire event to notify all observers
     * @param event based event
     */
    void fireEvent(SquirrelEvent event);

    int getListenerSize();
}
