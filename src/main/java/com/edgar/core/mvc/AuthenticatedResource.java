package com.edgar.core.mvc;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.edgar.core.shiro.AuthHelper;
import com.edgar.core.shiro.AuthType;
import com.edgar.core.shiro.CustomExcessiveAttemptsException;
import com.edgar.core.shiro.RetryLimitService;
import com.edgar.core.util.ExceptionFactory;
import com.edgar.core.view.ResponseMessage;

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
	private RetryLimitService retryLimitService;

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
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return 登录成功的视图
	 */
	@AuthHelper(value = "Login", type = AuthType.ANON)
	@RequestMapping(method = RequestMethod.POST, value = "/login")
	public ModelAndView login(@RequestParam("username") String username,
			@RequestParam("password") String password) {
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username,
				password);
		// token.setRememberMe(true);
		try {
			retryLimitService.addRetry(username);
			subject.login(token);
		} catch (CustomExcessiveAttemptsException e) {
			throw ExceptionFactory.ExcessiveAttempts(e.getAttemptsNum());
		} catch (UnknownAccountException e) {
			throw ExceptionFactory.userOrPasswordError();
		} catch (IncorrectCredentialsException e) {
			throw ExceptionFactory.userOrPasswordError();
		} catch (AuthenticationException e) {
			throw ExceptionFactory.userOrPasswordError();
		}
		retryLimitService.removeRetry(username);
		return ResponseMessage.asModelAndView("Login Success");
	}

	/**
	 * 用户注销
	 * 
	 * @return 注销成功的视图
	 */
	@AuthHelper(value = "Logout", type = AuthType.ANON)
	@RequestMapping(method = RequestMethod.POST, value = "/logout")
	public ModelAndView logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return ResponseMessage.asModelAndView("Login Failed");
	}
}
