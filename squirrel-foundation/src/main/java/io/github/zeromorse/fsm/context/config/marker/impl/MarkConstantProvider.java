package io.github.zeromorse.fsm.context.config.marker.impl;

public class MarkConstantProvider {
    static final MarkConstantProvider DEFAULT = new MarkConstantProvider();

    private int stateMaxSize = 100; // 处理状态节点时，最大的处理数
    private int transitionMaxSize = 1000; // 处理迁移节点时，最大处理数

    int getStateMaxSize() {
        return stateMaxSize;
    }

    public void setStateMaxSize(int stateMaxSize) {
        this.stateMaxSize = stateMaxSize;
    }

    int getTransitionMaxSize() {
        return transitionMaxSize;
    }

    public void setTransitionMaxSize(int transitionMaxSize) {
        this.transitionMaxSize = transitionMaxSize;
    }
}
