package com.edgar.core.auth;

import com.edgar.core.auth.stateless.StatelessUserService;
import com.edgar.core.command.CommandHandler;
import com.edgar.core.command.CommandResult;
import com.edgar.core.util.ExceptionFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginCommandHandler implements CommandHandler<LoginCommand> {

    @Autowired
    private StatelessUserService statelessUserService;

    @Override
    public CommandResult<AccessToken> execute(LoginCommand command) {
        String username = command.getUsername();
        String password = command.getPassword();

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username,
                password);
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            throw ExceptionFactory.userOrPasswordError();
        } catch (IncorrectCredentialsException e) {
            throw ExceptionFactory.userOrPasswordError();
        } catch (AuthenticationException e) {
            throw ExceptionFactory.userOrPasswordError();
        }
        AccessToken restToken = statelessUserService.newToken(username);
        return CommandResult.newInstance(restToken);
    }
}
