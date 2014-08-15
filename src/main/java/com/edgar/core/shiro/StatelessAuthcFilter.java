package com.edgar.core.shiro;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;

public class StatelessAuthcFilter implements Filter {

    private static final Set<String> MULTI_READ_HTTP_METHODS = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER) {{
        // Enable Multi-Read for PUT and POST requests
        add("PUT");
        add("POST");
    }};

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String url = httpServletRequest.getRequestURL().toString();
        String method = httpServletRequest.getMethod();
        if (StringUtils.endsWith(url, "login")) {
            filterChain.doFilter(httpServletRequest, servletResponse);
        } else if (MULTI_READ_HTTP_METHODS.contains(method)) {
            AuthenticationRequestWrapper authenticationRequestWrapper;
            try {
                authenticationRequestWrapper = new AuthenticationRequestWrapper(httpServletRequest);
                System.out.println(new String(authenticationRequestWrapper.body.getBytes()));
            } catch (IOException ex) {
                throw new ServletException("Unable to wrap the request", ex);
            }

            // continue the filter chain
            filterChain.doFilter(authenticationRequestWrapper, servletResponse);
        }  else {
            filterChain.doFilter(httpServletRequest, servletResponse);
        }

    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }
}