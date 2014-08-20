package com.edgar.core.auth;

import com.edgar.core.auth.stateless.StatelessUserService;
import com.edgar.core.command.CommandHandler;
import com.edgar.core.command.CommandResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2014/8/20.
 */
@Service
public class LogoutCommandHandler implements CommandHandler<LogoutCommand> {
    @Autowired
    private StatelessUserService statelessUserService;

    @Override
    public CommandResult<Boolean> execute(LogoutCommand command) {
        statelessUserService.removeToken(command.getAccessToken());
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return CommandResult.newInstance(true);
    }
}
