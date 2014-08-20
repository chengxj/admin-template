package com.edgar.core.mvc;

import com.edgar.core.auth.AccessToken;
import com.edgar.core.auth.LoginCommand;
import com.edgar.core.auth.LogoutCommand;
import com.edgar.core.auth.stateless.StatelessUser;
import com.edgar.core.auth.stateless.StatelessUserService;
import com.edgar.core.command.CommandBus;
import com.edgar.core.command.CommandResult;
import com.edgar.core.shiro.*;
import com.edgar.core.util.Constants;
import com.edgar.core.util.ExceptionFactory;
import com.edgar.core.view.ResponseMessage;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;

/**
 * 用户登录、注销的rest类
 *
 * @author Edgar Zhang
 * @version 1.0
 */
@Controller
@RequestMapping("/auth")
public class AuthenticatedResource {

    @Autowired
    private CommandBus commandBus;

    /**
     * 未登录用户
     *
     * @return 未登录的视图
     */
    @AuthHelper(value = "UnLogin User", type = AuthType.ANON)
    @RequestMapping(method = RequestMethod.GET, value = "/unlogin")
    public ModelAndView unLogin() {
        throw ExceptionFactory.unLogin();
    }

    /**
     * 未授权用户
     *
     * @return 未授权的视图
     */
    @AuthHelper(value = "UnAuthorized User", type = AuthType.ANON)
    @RequestMapping(method = RequestMethod.GET, value = "/unauthorized")
    public ModelAndView unAuthorized() {
        throw ExceptionFactory.unAuthorized();
    }

    /**
     * 用户登录
     *
     * @param loginCommand 用户名 密码对象
     * @return 登录成功的视图
     */
    @AuthHelper(value = "Login", type = AuthType.SSL)
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ModelAndView login(@RequestBody LoginCommand loginCommand) {
        CommandResult<AccessToken> result = commandBus.executeCommand(loginCommand);
        return ResponseMessage.asModelAndView(result.getResult());
    }


    /**
     * 用户注销
     *
     * @return 注销成功的视图
     */
    @AuthHelper(value = "Logout", type = AuthType.AUTHC)
    @RequestMapping(method = RequestMethod.POST, value = "/logout")
    public ModelAndView logout() {
        StatelessUser user = (StatelessUser) RequestContextHolder.currentRequestAttributes().getAttribute(Constants.USER_KEY, RequestAttributes.SCOPE_REQUEST);
        LogoutCommand command = new LogoutCommand();
        command.setAccessToken(user.getAccessToken());
        commandBus.executeCommand(command);
        return ResponseMessage.asModelAndView("Login Success");
    }
}
