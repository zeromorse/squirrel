package org.squirrelframework.foundation.fsm;

/**
 * Defines the history behavior of a state (on re-entrance of a super state).
 * 
 * @author Henry.He
 *
 * desc：历史行为类型
 */
public enum HistoryType {
    /**
     * The state enters into its initial sub-state. The sub-state itself enters its initial sub-state and so on until the innermost nested
     * state is reached. This is the default.
     *
     * desc：无，状态的常规迁移，进入初始的子状态
     */
    NONE,

    /**
     * The state enters into its last active sub-state. The sub-state itself enters its initial sub-state and so on until the innermost
     * nested state is reached.
     *
     * desc：浅，状态回到前一个状态的初始状态
     */
    SHALLOW,

    /**
     * The state enters into its last active sub-state. The sub-state itself enters into-its last active state and so on until the innermost
     * nested state is reached.
     *
     * desc：深，状态回到前一个激活状态（前一个状态的最后激活状态）
     */
    DEEP
}
