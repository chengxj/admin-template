package com.edgar.core.command;

import org.springframework.stereotype.Repository;

import com.edgar.core.command.CommandHandler;
import com.edgar.core.command.CommandResult;
import com.edgar.module.sys.repository.domain.SysUser;

@Repository
public class ChainGetSysUserHandler implements CommandHandler<ChainGetSysUser> {

        @Override
        public CommandResult<SysUser> execute(ChainGetSysUser command) {
                System.out.println(command.getClass());
                return CommandResult.newInstance(new SysUser());
        }

}
