package org.squirrelframework.foundation.fsm;

/**
 * The type of transition. According to the UML specification (2.5 b1 pag. 377), state machine transition 
 * can be divided into three type.
 * 
 * @author Henry.He
 *
 * desc：迁移类型，按作用范围有小到大排序
 */
public enum TransitionType {
    /**
     * Implies that the Transition, if triggered, occurs without exiting or entering the source State 
     * (i.e., it does not cause a state change). This means that the entry or exit condition of the source 
     * State will not be invoked. An internal Transition can be taken even if the SateMachine is in one or 
     * more Regions nested within the associated State.
     *
     * desc：作用于状态内，且不会脱离当前状态
     */
    INTERNAL, 
    /**
     * Implies that the Transition, if triggered, will not exit the composite (source) State, but it 
     * will exit and re-enter any state within the composite State that is in the current state configuration.
     *
     * desc：作用于合成状态，合成状态内部迁移
     */
    LOCAL,
    /**
     * Implies that the Transition, if triggered, will exit the composite (source) State.
     *
     * desc：作用与合成状态间，状态/合成状态间的迁移
     */
    EXTERNAL
}
