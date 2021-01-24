package io.github.zeromorse.fsm.util;

import javax.annotation.Nullable;

public final class ArrayUtil {
    public static boolean isEmpty(@Nullable Object[] objs) {
        return objs == null || objs.length == 0;
    }
}
