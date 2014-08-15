package com.edgar.core.shiro;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.AccessControlFilter;

public class StatelessAuthcFilter extends AccessControlFilter {

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
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String accessToken = request.getParameter("accessToken");
        String clientDigest = request.getParameter("digest");
        String baseString = null;
        if (StringUtils.endsWith(url, "/login")) {
            return true;
        }

        if (MULTI_READ_HTTP_METHODS.contains(method)) {
            AuthenticationRequestWrapper authenticationRequestWrapper;
            try {
                authenticationRequestWrapper = new AuthenticationRequestWrapper(request);
                String body = new String(authenticationRequestWrapper.getBody().getBytes());
                String bodyString = null;
                if (StringUtils.isNotBlank(body)) {
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> map = mapper.readValue(body, new TypeReference<HashMap<String, Object>>() {
                    });
                    List<String> keys = new ArrayList<String>(map.keySet());
                    Collections.sort(keys);

                    List<String> dataStringList = new ArrayList<String>(keys.size());

                    for (String key : keys) {
                        Object value = map.get(key);
                        if (value instanceof String[]) {
                            List<String> valueList = new ArrayList<String>();
                            Collections.addAll(valueList, (String[]) value);
                            Collections.sort(valueList);
                            dataStringList.add(key + "=" + StringUtils.join(valueList, ","));
                        } else if (value instanceof List) {
                            List<String> valueList = (List<String>) value;
                            dataStringList.add(key + "=" + StringUtils.join(valueList, ","));
                        } else {
                            dataStringList.add(key + "=" + value.toString());
                        }
                    }
                    bodyString = StringUtils.join(dataStringList, "&");
                }
                String queryString = getQueryString(request);
                if (StringUtils.isNotBlank(queryString)) {
                    if (StringUtils.isNotBlank(bodyString)) {
                        queryString = queryString + "&" + bodyString;
                    }
                } else {
                    queryString = bodyString;
                }
                baseString = getBaseString(request, queryString);
            } catch (IOException ex) {
                throw new ServletException("Unable to wrap the request", ex);
            }

        } else {
            String queryString = getQueryString(request);
            baseString = getBaseString(request, queryString);
        }

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
        return true;
    }

    private String getBaseString(HttpServletRequest request, String queryString) {
        System.out.println(queryString);
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String contextPah = request.getContextPath();
        StringBuilder baseString = new StringBuilder(method).append(StringUtils.substringAfter(url, contextPah + "/"));
        if (StringUtils.isNotBlank(queryString)) {
            baseString.append("?").append(queryString);
        }
        return baseString.toString();
    }

    public String getQueryString(HttpServletRequest request) {
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
        httpResponse.getWriter().write("login error");
    }
}