package com.edgar.core.auth.stateless;

import com.edgar.core.auth.AuthService;
import com.edgar.core.util.Constants;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 根据token校验用户的filter
 */
public class StatelessAuthcFilter extends AccessControlFilter {

    private static final String ACCESS_TOKEN = "accessToken";
    private static final String DIGEST = "digest";
    private static final String BASE_STRING = "baseString";

    private static final Logger LOGGER = LoggerFactory
            .getLogger(StatelessAuthcFilter.class);

    @Autowired
    private AuthService authService;

    @Override
    protected boolean isAccessAllowed(ServletRequest request,
                                      ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest,
                                     ServletResponse response) throws Exception {
        String accessToken = servletRequest.getParameter(ACCESS_TOKEN);
        String clientDigest = servletRequest.getParameter(DIGEST);
        String baseString = (String) servletRequest.getAttribute(BASE_STRING);

        // 4、生成无状态Token
        StatelessToken token = new StatelessToken(accessToken, baseString,
                clientDigest);

        try {
            // 5、委托给Realm进行登录
            getSubject(servletRequest, response).login(token);
        } catch (Exception e) {
            LOGGER.error("Access Denied : {}", accessToken);
            onLoginFail(response);
            return false;
        }
        StatelessUser user = authService.getUser(accessToken);
        RequestContextHolder.currentRequestAttributes().setAttribute(Constants.USER_KEY, user, RequestAttributes.SCOPE_REQUEST);
        return true;
    }

    /**
     * 校验失败，返回401状态码
     *
     * @param response ServletResponse
     * @throws IOException IOException
     */
    private void onLoginFail(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("Invalid Token!");
    }
}