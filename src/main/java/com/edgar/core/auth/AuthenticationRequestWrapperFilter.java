package com.edgar.core.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * 重复读取request body的过滤器
 */
public class AuthenticationRequestWrapperFilter implements Filter {

    private static final Set<String> MULTI_READ_HTTP_METHODS = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER) {{
        add("PUT");
        add("POST");
    }};
    private static final Set<String> IGNORE_URL = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String ignore = filterConfig.getInitParameter("ignore");
        String[] ignoreArray = StringUtils.split(ignore, ",");
        Collections.addAll(IGNORE_URL, ignoreArray);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String method = request.getMethod();
        String baseString;
        String serveltPath = request.getServletPath();
        for (String ignore : IGNORE_URL) {
            String start = StringUtils.substringBeforeLast(StringUtils.trim(ignore), "/*");
            if (StringUtils.startsWith(serveltPath, start)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
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
                            List<Object> valueList = (List<Object>) value;
                            dataStringList.add(key + "=" + StringUtils.join(valueList, ","));
                        } else {
                            dataStringList.add(key + "=" + value.toString());
                        }
                    }
                    bodyString = StringUtils.join(dataStringList, "&");
                }
                String queryString = getQueryString(authenticationRequestWrapper);
                if (StringUtils.isNotBlank(queryString)) {
                    if (StringUtils.isNotBlank(bodyString)) {
                        queryString = queryString + "&" + bodyString;
                    }
                } else {
                    queryString = bodyString;
                }
                baseString = getBaseString(authenticationRequestWrapper, queryString);
                request = authenticationRequestWrapper;

            } catch (IOException ex) {
                throw new ServletException("Unable to wrap the request", ex);
            }

        } else {
            String queryString = getQueryString(request);
            baseString = getBaseString(request, queryString);
        }
        request.setAttribute("baseString", baseString);

        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {

    }

    private String getBaseString(HttpServletRequest request, String queryString) {
        String method = request.getMethod();
        String servletPath = request.getServletPath();
        if (StringUtils.startsWith(servletPath, "/")) {
            servletPath = StringUtils.substringAfter(servletPath, "/");
        }
        StringBuilder baseString = new StringBuilder(method).append(servletPath);
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
}
