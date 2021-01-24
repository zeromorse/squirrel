package io.github.zeromorse.fsm.context.config.cache;


import javax.annotation.Nullable;
import java.util.List;

/**
 * 状态机挂载行为
 */
public interface FsmBehavior {
    @Nullable
    String getTransitCondition(FteTriple fteTriple);

    List<String> getTransitAction(FteTriple fteTriple);

    List<String> getEntryAction(String state);

    List<String> getExitAction(String state);
}
