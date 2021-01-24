package io.github.zeromorse.fsm.context.config.marker.impl;

import com.google.common.collect.Lists;
import io.github.zeromorse.fsm.context.model.GenericContext;
import io.github.zeromorse.fsm.context.model.GenericStateMachine;
import io.github.zeromorse.fsm.context.model.impl.GenericEntryAction;
import io.github.zeromorse.fsm.context.model.impl.GenericExitAction;
import io.github.zeromorse.fsm.context.model.impl.GenericTransitAction;
import io.github.zeromorse.fsm.context.model.impl.GenericTransitCondition;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.squirrelframework.foundation.fsm.AnonymousAction;
import org.squirrelframework.foundation.fsm.AnonymousCondition;
import org.squirrelframework.foundation.fsm.StateMachine;

import java.util.List;

/**
 * SQRL标记
 */
public class SqrlMark {
    public static final SqrlMark DEFAULT = new SqrlMark();

    private Class<? extends StateMachine> stateMachineClass = GenericStateMachine.class;
    private Class<?> stateClass = String.class;
    private Class<?> eventClass = String.class;
    private Class<?> contextClass = GenericContext.class;

    private AnonymousCondition transitCondition = GenericTransitCondition.INSTANCE;
    private AnonymousAction entryAction = GenericEntryAction.INSTANCE;
    private AnonymousAction exitAction = GenericExitAction.INSTANCE;
    private AnonymousAction transitAction = GenericTransitAction.INSTANCE;

    Namespace sqrlNamespace = Namespace.getNamespace("sqrl", "http://squirrelframework.org/squirrel");

    private SqrlMark() {
    }

    Element sqrlFsmElement() {
        Element fsm = new Element("fsm", sqrlNamespace);
        fsm.setAttribute("fsm-type", stateMachineClass.getCanonicalName());
        fsm.setAttribute("context-type", contextClass.getCanonicalName());
        fsm.setAttribute("state-type", stateClass.getCanonicalName());
        fsm.setAttribute("event-type", eventClass.getCanonicalName());
        return fsm;
    }

    private Element sqrlEntryActionElement() {
        Element action = new Element("action", sqrlNamespace);
        action.setAttribute("content", entryAction.toString());
        return action;
    }

    Element onentryElement() {
        Element onentry = new Element("onentry");
        onentry.addContent(sqrlEntryActionElement());
        return onentry;
    }

    private Element sqrlExitActionElement() {
        Element action = new Element("action", sqrlNamespace);
        action.setAttribute("content", exitAction.toString());
        return action;
    }

    Element onexitElement() {
        Element onexit = new Element("onexit");
        onexit.addContent(sqrlExitActionElement());
        return onexit;
    }

    List<Attribute> transitionAttrs() {
        return Lists.newArrayList(
                new Attribute("cond", transitCondition.toString()),
                new Attribute("priority", "1", sqrlNamespace),
                new Attribute("type", "EXTERNAL", sqrlNamespace));
    }

    Element transitionActionElement() {
        Element action = new Element("action", sqrlNamespace);
        action.setAttribute("content", transitAction.toString());
        return action;
    }

    /**
     * 定制化参数
     */
    public static class Builder {
        private Class<? extends StateMachine> stateMachineClass;
        private Class<?> stateClass;
        private Class<?> eventClass;
        private Class<?> contextClass;

        private AnonymousCondition transitCondition;
        private AnonymousAction entryAction;
        private AnonymousAction exitAction;
        private AnonymousAction transitAction;

        public Builder() {
        }

        public Builder types(Class<? extends StateMachine> stateMachineClass, Class<?> stateClass, Class<?> eventClass, Class<?> contextClass) {
            this.stateMachineClass = stateMachineClass;
            this.stateClass = stateClass;
            this.eventClass = eventClass;
            this.contextClass = contextClass;

            return this;
        }

        public Builder instances(AnonymousCondition transitCondition, AnonymousAction entryAction, AnonymousAction exitAction, AnonymousAction transitAction) {
            this.transitCondition = transitCondition;
            this.entryAction = entryAction;
            this.exitAction = exitAction;
            this.transitAction = transitAction;

            return this;
        }

        public SqrlMark build() {
            SqrlMark sqrlMark = new SqrlMark();

            sqrlMark.stateMachineClass = stateMachineClass;
            sqrlMark.stateClass = stateClass;
            sqrlMark.eventClass = eventClass;
            sqrlMark.contextClass = contextClass;
            sqrlMark.transitCondition = transitCondition;
            sqrlMark.entryAction = entryAction;
            sqrlMark.exitAction = exitAction;
            sqrlMark.transitAction = transitAction;

            return sqrlMark;
        }
    }
}
