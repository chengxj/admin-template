package com.edgar.core.exception;

/**
 * Created by Administrator on 2014/11/17.
 */
public class ThrowAppException implements ExceptionHandler {
    @Override
    public void handle(String context, String code, String message, Throwable t) {
        if (t instanceof AppException) {
            AppException e = (AppException) t;
            e.addInfo(context, code, message);
            throw e;
        } else {
            throw new AppException(context, code, message, t);
        }
    }
}
