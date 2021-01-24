package io.github.zeromorse.fsm.util;

import com.google.common.collect.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Service Provider Interface 机制
 */
public final class SPIUtil {
    private static final Logger logger = LoggerFactory.getLogger(SPIUtil.class);

    /**
     * 加载必须组件，优先使用SPI的实现，若不存在则使用默认实现
     *
     * @param componentIface 组件接口
     * @param defaultClazz   默认实现
     */
    public static <T> T load(Class<T> componentIface, Class<? extends T> defaultClazz) {
        ServiceLoader<T> impls = ServiceLoader.load(componentIface);
        if (!Iterables.isEmpty(impls)) {
            T spiInst = impls.iterator().next();
            logger.info("load spi class {} as a implement of {}, will replace {}", spiInst.getClass(), componentIface, defaultClazz);
            return spiInst;
        }

        try {
            return defaultClazz.newInstance();
        } catch (Exception e) {
            // 按理说不会有问题
            logger.error("create default component failure", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取所有实现组件接口的实例
     *
     * @param componentIface 组件接口
     */
    public static <T> Iterator<T> load(Class<T> componentIface) {
        ServiceLoader<T> impls = ServiceLoader.load(componentIface);
        return impls.iterator();
    }
}
