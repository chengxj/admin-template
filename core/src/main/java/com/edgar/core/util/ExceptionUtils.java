package com.edgar.core.util;

import com.edgar.core.exception.IgnoreExceptionHandler;
import com.edgar.core.exception.ThrowAppException;

/**
 * 异常处理的工具类
 */
public abstract class ExceptionUtils {

    public static void ignore(String context, String code,
                              String message, Throwable t) {
        new IgnoreExceptionHandler().handle(context, code, message, t);
    }

    public static void thrwoAppException(String context, String code,
                                         String message, Throwable t) {
        new ThrowAppException().handle(context, code, message, t);
    }
}
