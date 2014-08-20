package com.edgar.core.auth.stateless;

import com.edgar.core.util.Constants;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StatelessAuthcFilter extends AccessControlFilter {

    @Autowired
    private StatelessUserService statelessUserService;

    @Override
    protected boolean isAccessAllowed(ServletRequest request,
                                      ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest,
                                     ServletResponse response) throws Exception {
        String accessToken = servletRequest.getParameter("accessToken");
        String clientDigest = servletRequest.getParameter("digest");
        String baseString = (String) servletRequest.getAttribute("baseString");

        // 4、生成无状态Token
        StatelessToken token = new StatelessToken(accessToken, baseString,
                clientDigest);

        try {
            // 5、委托给Realm进行登录
            getSubject(servletRequest, response).login(token);
        } catch (Exception e) {
            e.printStackTrace();
            onLoginFail(response); // 6、登录失败
            return false;
        }
        RequestContextHolder.currentRequestAttributes().setAttribute(Constants.USER_KEY, statelessUserService.getUser(accessToken), RequestAttributes.SCOPE_REQUEST);
        return true;
    }

    // 登录失败时默认返回401状态码
    private void onLoginFail(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("Username or Password is not correct!");
    }
}