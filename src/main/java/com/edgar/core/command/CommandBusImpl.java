package com.edgar.core.command;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 命令调度类的实现.
 * 
 * @author Edgar
 * @version 1.0
 */
@Service
public class CommandBusImpl implements CommandBus, ApplicationContextAware {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CommandBusImpl.class);

	/**
	 * Spring的上下文
	 */
	private static ApplicationContext APPLICATION_CONTEXT;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> CommandResult<T> executeCommand(Command command) {
		CommandHandler commandHandler;
		commandHandler = getCommandHandler(command);
		LOGGER.debug("request command is {}", command.getClass());
		if (command instanceof ChainCommand) {
			ChainCommand chainCommand = (ChainCommand) command;
			CommandResult<T> result = commandHandler.execute(command);
			Command nextCommand = chainCommand.nextCommand();
			if (nextCommand == null || nextCommand instanceof UnResolvedCommand) {
				return result;
			}
			LOGGER.debug("command in chain，next command is {}", command.getClass());
			return executeCommand(nextCommand);
		}
		return commandHandler.execute(command);
	}

	@Override
	public List<CommandResult> executeCommands(List<Command> commands) {
		LOGGER.debug("batch execute {} commands", commands.size());
        List<CommandResult> results = new ArrayList<CommandResult>(commands.size());
        for (Command command : commands) {
            results.add(executeCommand(command));
        }
		return results;
	}

	@Override
	public void setApplicationContext(ApplicationContext context) {
		APPLICATION_CONTEXT = context;
	}

	/**
	 * 根据命令对象获取处理类
	 * 
	 * @param command
	 *            命令对象
	 * @return 命令处理类
	 */
	@SuppressWarnings("rawtypes")
	private CommandHandler getCommandHandler(Command command) {
		Preconditions.checkNotNull(command, "command cannot be null");
		String handlerId = command.getClass().getSimpleName() + "Handler";
		handlerId = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, handlerId);
		return APPLICATION_CONTEXT.getBean(handlerId,
                CommandHandler.class);
	}

}
