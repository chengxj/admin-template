package com.edgar.core.shiro;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

public class StatelessAuthcFilter implements Filter {

    private static final Set<String> MULTI_READ_HTTP_METHODS = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER) {{
        // Enable Multi-Read for PUT and POST requests
        add("PUT");
        add("POST");
    }};

    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String url = request.getRequestURL().toString();
        String method = request.getMethod();

        if (StringUtils.endsWith(url, ".html")) {
            filterChain.doFilter(request, response);
        } else if (StringUtils.endsWith(url, ".js")) {
            filterChain.doFilter(request, response);
        } else if (StringUtils.endsWith(url, ".jpg")) {
            filterChain.doFilter(request, response);
        } else if (StringUtils.endsWith(url, ".png")) {
            filterChain.doFilter(request, response);
        } else if (StringUtils.endsWith(url, ".css")) {
            filterChain.doFilter(request, response);
        } else if (StringUtils.endsWith(url, ".font")) {
            filterChain.doFilter(request, response);
        } else if (StringUtils.endsWith(url, ".4.1.0")) {
            filterChain.doFilter(request, response);
        } else if (StringUtils.endsWith(url, ".woff")) {
            filterChain.doFilter(request, response);
        } else if (StringUtils.endsWith(url, ".eot")) {
            filterChain.doFilter(request, response);
        } else if (StringUtils.endsWith(url, ".ico")) {
            filterChain.doFilter(request, response);
        } else if (StringUtils.endsWith(url, ".gif")) {
            filterChain.doFilter(request, response);
        } else if (StringUtils.endsWith(url, ".json")) {
            filterChain.doFilter(request, response);
        } else if (MULTI_READ_HTTP_METHODS.contains(method) && !StringUtils.endsWith(url, "/login")) {
            AuthenticationRequestWrapper authenticationRequestWrapper;
            try {
                authenticationRequestWrapper = new AuthenticationRequestWrapper(request);
                String body = new String(authenticationRequestWrapper.getBody().getBytes());
                String bodyString = null;
                if (StringUtils.isNotBlank(body)) {
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String,Object> map = mapper.readValue(body, new TypeReference<HashMap<String,Object>>() {});
                    List<String> keys = new ArrayList<String>(map.keySet());
                    Collections.sort(keys);

                    List<String> dataStringList = new ArrayList<String>(keys.size());

                    for (String key : keys) {
                        Object value = map.get(key);
                        if(value instanceof String[]) {
                            List<String> valueList = new ArrayList<String>();
                            Collections.addAll(valueList, (String[])value);
                            Collections.sort(valueList);
                            dataStringList.add(key + "=" + StringUtils.join(valueList, ","));
                        } else if(value instanceof List) {
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
                String serverDigest = getServerDigest(request, queryString);
                System.out.println(url);
                String clientDigest = request.getParameter("digest");
                System.out.println(clientDigest);
                System.out.println(serverDigest);
                System.out.println(serverDigest.equals(clientDigest));
            } catch (IOException ex) {
                throw new ServletException("Unable to wrap the request", ex);
            }

            // continue the filter chain
            filterChain.doFilter(authenticationRequestWrapper, response);
        } else {
            System.out.println(url);
            String clientDigest = request.getParameter("digest");
            String queryString = getQueryString(request);
            String serverDigest = getServerDigest(request, queryString);
            System.out.println(clientDigest);
            System.out.println(serverDigest);
            System.out.println(serverDigest.equals(clientDigest));
            filterChain.doFilter(request, response);
        }

    }

    private String getServerDigest(HttpServletRequest request, String queryString) {
        System.out.println(queryString);
        String url =request.getRequestURL().toString() ;
        String method = request.getMethod();
        String contextPah = request.getContextPath();
        StringBuilder baseString = new StringBuilder(method).append(StringUtils.substringAfter(url, contextPah + "/"));
        if (StringUtils.isNotBlank(queryString))  {
            baseString.append("?").append(queryString);
        }
        return HmacSHA256Utils.digest("aaaaaaaaaaaaa", baseString.toString());
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

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }
}