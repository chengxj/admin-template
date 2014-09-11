package com.edgar.core.command;
import org.springframework.stereotype.Service;

import com.edgar.core.command.CommandHandler;
import com.edgar.core.command.CommandResult;
import com.edgar.module.sys.repository.domain.SysUser;

@Service
public class GetSysUserHandler implements CommandHandler<GetSysUser> {

	@Override
	public CommandResult<SysUser> execute(GetSysUser command) {
	        System.out.println(command.getClass());
		return CommandResult.newInstance(new SysUser());
	}

}