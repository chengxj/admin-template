package com.edgar.core.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * AppException，系统所有的异常在顶层均以AppException抛出
 * @author Edgar
 * @version 1.0
 */
public class AppException extends RuntimeException {

    private List<ExceptionItem> items = new ArrayList<ExceptionItem>();

    private static class ExceptionItem {
        public final String context;
        public final String code;
        public final String message;

        public ExceptionItem(String context, String code,
                             String message) {

            this.context = context;
            this.code = code;
            this.message = message;
        }
    }

    public AppException(String context, String code,
                        String message) {
        addInfo(context, code, message);
    }

    public AppException(String context, String code,
                        String message, Throwable cause) {
        super(cause);
        addInfo(context, code, message);
    }

    public AppException addInfo(
            String cotext, String code, String message) {
        this.items.add(
                new ExceptionItem(cotext, code, message));
        return this;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("Exception\n");
        //append additional context information.
        itemsToString(builder);
        //append root causes and text from this exception first.
        if (getMessage() != null) {
            builder.append('\n');
            if (getCause() == null) {
                builder.append(getMessage());
            } else if (!getMessage().equals(getCause().toString())) {
                builder.append(getMessage());
            }
        }
        appendException(builder, getCause());
        return builder.toString();
    }

    private void itemsToString(StringBuilder builder) {
        int itemSize = this.items.size();
        for (int i = itemSize - 1; i >= 0; i--) {
            ExceptionItem info =
                    this.items.get(i);
            for (int j = 0; j < itemSize -i; j ++) {
                builder.append(" ");
            }
            builder.append("└── ");
            builder.append('[');
            builder.append(info.context);
            builder.append(':');
            builder.append(info.code);
            builder.append(']');
            builder.append(info.message);
            if (i > 0) {
                builder.append('\n');
            }
        }
    }

    private void appendException(
            StringBuilder builder, Throwable throwable) {
        if (throwable == null) {
            return;
        }
        appendException(builder, throwable.getCause());
        builder.append(throwable.toString());
        builder.append('\n');
    }
}