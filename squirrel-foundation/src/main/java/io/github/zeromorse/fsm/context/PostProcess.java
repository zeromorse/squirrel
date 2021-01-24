package io.github.zeromorse.fsm.context;

/**
 * 后处理标记，用来确定需要后处理的组件是否准备就绪
 * <p>
 * 后处理：使用SPI时，serviceLoader载入的服务需要空构造函数，其属性需要setXxx来注入
 */
public interface PostProcess {
    /**
     * 属性是否填充完毕
     */
    boolean isReady();
}
