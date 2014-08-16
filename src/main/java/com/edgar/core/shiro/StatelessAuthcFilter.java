package com.edgar.core.shiro;

import com.edgar.core.cache.CacheWrapper;
import com.edgar.core.cache.EhCacheWrapper;
import com.edgar.core.util.Constants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.ehcache.CacheManager;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class StatelessAuthcFilter extends AccessControlFilter {

    @Autowired
    private StatelessUserService statelessUserService;

    private static final Set<String> MULTI_READ_HTTP_METHODS = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER) {{
        // Enable Multi-Read for PUT and POST requests
        add("PUT");
        add("POST");
    }};

    @Override
    protected boolean isAccessAllowed(ServletRequest request,
                                      ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest,
                                     ServletResponse response) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String method = request.getMethod();
        String accessToken = servletRequest.getParameter("accessToken");
        String clientDigest = servletRequest.getParameter("digest");
        String baseString = (String) request.getAttribute("baseString");

        // 4、生成无状态Token
        StatelessToken token = new StatelessToken(accessToken, baseString,
                clientDigest);

        try {
            // 5、委托给Realm进行登录
            getSubject(request, response).login(token);
        } catch (Exception e) {
            e.printStackTrace();
            onLoginFail(response); // 6、登录失败
            return false;
        }
        RequestContextHolder.currentRequestAttributes().setAttribute(Constants.USER_KEY, statelessUserService.getUser(accessToken), RequestAttributes.SCOPE_REQUEST);
        return true;
    }

    private String getBaseString(HttpServletRequest request, String queryString) {
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String contextPah = request.getContextPath();
        StringBuilder baseString = new StringBuilder(method).append(StringUtils.substringAfter(url, contextPah + "/"));
        if (StringUtils.isNotBlank(queryString)) {
            baseString.append("?").append(queryString);
        }
        return baseString.toString();
    }

    private String getQueryString(HttpServletRequest request) {
        List<String> paramNameList = new ArrayList<String>();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if (!"digest".equals(name)) {
                paramNameList.add(name);
            }
        }
        Collections.sort(paramNameList);
        List<String> paramStringList = new ArrayList<String>(paramNameList.size());

        for (String paramName : paramNameList) {
            String[] parameterValues = request.getParameterValues(paramName);
            List<String> parameterValueList = new ArrayList<String>();
            Collections.addAll(parameterValueList, parameterValues);
            Collections.sort(parameterValueList);
            paramStringList.add(paramName + "=" + StringUtils.join(parameterValueList, ","));
        }
        return StringUtils.join(paramStringList, "&");
    }

    // 登录失败时默认返回401状态码
    private void onLoginFail(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("Username or Password is not correct!");
    }
}