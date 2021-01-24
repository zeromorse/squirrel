package org.squirrelframework.foundation.fsm;

import java.util.List;

/**
 * Constants conditions
 *
 * @author Henry.He
 *
 * desc：条件相关的工具
 */
public class Conditions {

    public static <T extends StateMachine<T, S, E, C>, S, E, C> boolean isSatified(Condition<T, S, E, C> condition, S from, S to, E event, C context, T stateMachine) {
        return condition != null && context != null && condition.isSatisfied(from, to, event, context, stateMachine);
    }

    public static <T extends StateMachine<T, S, E, C>, S, E, C> boolean isNotSatified(Condition<T, S, E, C> condition, S from, S to, E event, C context, T stateMachine) {
        return condition == null || context == null || !condition.isSatisfied(from, to, event, context, stateMachine);
    }

    public static <T extends StateMachine<T, S, E, C>, S, E, C> Condition<T, S, E, C> always() {
        return new Always<T, S, E, C>();
    }

    public static <T extends StateMachine<T, S, E, C>, S, E, C> Condition<T, S, E, C> never() {
        return new Never<T, S, E, C>();
    }

    public static <T extends StateMachine<T, S, E, C>, S, E, C> Condition<T, S, E, C> and(final Condition<T, S, E, C> first, final Condition<T, S, E, C> second) {
        return new AnonymousCondition<T, S, E, C>() {
            @Override
            public boolean isSatisfied(S from, S to, E event, C context, T stateMachine) {
                return first.isSatisfied(from, to, event, context, stateMachine) && second.isSatisfied(from, to, event, context, stateMachine);
            }

            @Override
            public String name() {
                return first.name() + "And" + second.name();
            }
        };
    }

    public static <T extends StateMachine<T, S, E, C>, S, E, C> Condition<T, S, E, C> and(final List<Condition<T, S, E, C>> conditions) {
        return new AnonymousCondition<T, S, E, C>() {
            @Override
            public boolean isSatisfied(S from, S to, E event, C context, T stateMachine) {
                for (Condition<T, S, E, C> condition : conditions) {
                    if (!condition.isSatisfied(from, to, event, context, stateMachine)) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public String name() {
                String name = null;
                for (Condition<T, S, E, C> c : conditions) {
                    if (name == null)
                        name = c.name();
                    else
                        name = name + "And" + c.name();
                }
                return name;
            }
        };
    }

    public static <T extends StateMachine<T, S, E, C>, S, E, C> Condition<T, S, E, C> or(final Condition<T, S, E, C> first, final Condition<T, S, E, C> second) {
        return new AnonymousCondition<T, S, E, C>() {
            @Override
            public boolean isSatisfied(S from, S to, E event, C context, T stateMachine) {
                return first.isSatisfied(from, to, event, context, stateMachine) || second.isSatisfied(from, to, event, context, stateMachine);
            }

            @Override
            public String name() {
                return first.name() + "Or" + second.name();
            }
        };
    }

    public static <T extends StateMachine<T, S, E, C>, S, E, C> Condition<T, S, E, C> or(final List<Condition<T, S, E, C>> conditions) {
        return new AnonymousCondition<T, S, E, C>() {
            @Override
            public boolean isSatisfied(S from, S to, E event, C context, T stateMachine) {
                for (Condition<T, S, E, C> condition : conditions) {
                    if (condition.isSatisfied(from, to, event, context, stateMachine)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String name() {
                String name = null;
                for (Condition<T, S, E, C> c : conditions) {
                    if (name == null)
                        name = c.name();
                    else
                        name = name + "Or" + c.name();
                }
                return name;
            }
        };
    }

    public static <T extends StateMachine<T, S, E, C>, S, E, C> Condition<T, S, E, C> not(final Condition<T, S, E, C> condition) {
        return new AnonymousCondition<T, S, E, C>() {
            @Override
            public boolean isSatisfied(S from, S to, E event, C context, T stateMachine) {
                return !condition.isSatisfied(from, to, event, context, stateMachine);
            }

            @Override
            public String name() {
                return "Not" + condition.name();
            }
        };
    }

    public static <T extends StateMachine<T, S, E, C>, S, E, C> Condition<T, S, E, C> xor(final Condition<T, S, E, C> first, final Condition<T, S, E, C> second) {
        return new AnonymousCondition<T, S, E, C>() {
            @Override
            public boolean isSatisfied(S from, S to, E event, C context, T stateMachine) {
                return first.isSatisfied(from, to, event, context, stateMachine) ^ second.isSatisfied(from, to, event, context, stateMachine);
            }

            @Override
            public String name() {
                return first.name() + "Xor" + second.name();
            }
        };
    }

    public static class Always<T extends StateMachine<T, S, E, C>, S, E, C> extends AnonymousCondition<T, S, E, C> {
        @Override
        public boolean isSatisfied(S from, S to, E event, C context, T stateMachine) {
            return true;
        }
    }

    public static class Never<T extends StateMachine<T, S, E, C>, S, E, C> extends AnonymousCondition<T, S, E, C> {
        @Override
        public boolean isSatisfied(S from, S to, E event, C context, T stateMachine) {
            return false;
        }
    }
}