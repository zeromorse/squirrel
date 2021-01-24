package io.github.zeromorse.fsm.util;

import com.google.common.io.ByteStreams;
import com.google.common.io.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public final class IOUtil {
    /**
     * 获取资源内容
     */
    public static String getContent(String resourceName) {
        try {
            return Resources.toString(Resources.getResource(resourceName), Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取资源标准路径
     */
    public static String getCanonicalPath(String resourceName) {
        return Resources.getResource(resourceName).getFile();
    }

    /**
     * 输入流到字符串
     */
    public static String is2String(InputStream is) {
        try {
            return new String(ByteStreams.toByteArray(is));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
