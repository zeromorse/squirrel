package org.squirrelframework.foundation.self;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试Log4j2作为日志实现的能力
 */
public class LogTest {
    private static Logger logger = LoggerFactory.getLogger(LogTest.class);

    public static void main(String[] args) {
        logger.info("Hello World!");
    }
}
