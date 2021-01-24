package io.github.zeromorse.fsm.context.config.marker;

import java.io.InputStream;

public interface ScxmlMarker {
    /**
     * 装饰标准 SCXML，加入特定的 sqrl 标记
     */
    String decorate(String scxml);

    /**
     * 装饰标准 SCXML，加入特定的 sqrl 标记
     */
    String decorate(InputStream source);
}
