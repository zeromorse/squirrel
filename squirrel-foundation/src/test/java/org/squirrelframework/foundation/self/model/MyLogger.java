package org.squirrelframework.foundation.self.model;

import lombok.extern.slf4j.Slf4j;

/**
 * 日志代理
 */
@Slf4j
public class MyLogger {
    private StringBuilder sb = new StringBuilder();

    public void append(String s) {
        log.info(s);
        sb.append(s);
    }

    public void append(String prepared, Object... values) {
        String s = String.format(prepared, values);
        log.info(s);
        sb.append(s);
    }

    public String toString() {
        return sb.toString();
    }

    public void clear() {
        sb = new StringBuilder();
    }
}