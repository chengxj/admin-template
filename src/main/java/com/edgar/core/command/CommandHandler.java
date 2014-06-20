package com.edgar.core.command;

import net.bull.javamelody.MonitoredWithSpring;

/**
 * 命令的处理类.
 * 
 * @author Edgar
 * @version 1.0
 * 
 * @param <T> 命令对象
 */
@MonitoredWithSpring
public interface CommandHandler<T extends Command> {

        /**
         * 命令的处理方法.
         * 
         * @param command
         *                命令
         * @return {@link CommandResult}
         */
        CommandResult<?> execute(T command);
}