package com.edgar.core.shiro;

import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 14-8-13
 * Time: 上午10:54
 * To change this template use File | Settings | File Templates.
 */
public class DigestFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String url = request.getRequestURL().toString();
        if (StringUtils.endsWith(url, ".html")) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else if (StringUtils.endsWith(url, ".json")) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else if (StringUtils.endsWith(url, ".js")) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else if (StringUtils.endsWith(url, ".css")) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else if (StringUtils.endsWith(url, ".png")) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else if (StringUtils.endsWith(url, ".jpg")) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else if (StringUtils.endsWith(url, ".gif")) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else if (StringUtils.endsWith(url, ".eot")) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else if (StringUtils.endsWith(url, ".ico")) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else if (StringUtils.endsWith(url, ".woff?v=4.1.0")) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            String clientDigest = request.getParameter("digest");
            List<String> set = new ArrayList<String>();
            Enumeration<String> names = request.getParameterNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                if (!"digest".equals(name)) {
                    set.add(name);
                }
            }
            Collections.sort(set);
            List<String> queryString = new ArrayList<String>(set.size());

            for (String s : set) {
                String[] v = request.getParameterValues(s);
                List<String> value = new ArrayList<String>();
                Collections.addAll(value, v);
                Collections.sort(value);
                queryString.add(s + "=" + StringUtils.join(value, ","));
            }
            System.out.println(queryString);
            String contextPah = request.getContextPath();
            String method = request.getMethod();
            StringBuilder baseString = new StringBuilder(method).append(StringUtils.substringAfter(url, contextPah + "/"));
            baseString.append("?").append(StringUtils.join(queryString, "&"));
            System.out.println(clientDigest);
            System.out.println(baseString);
            String serverDigest = HmacSHA256Utils.digest("aaaaaaaaaaaaa", baseString.toString());
            System.out.println(serverDigest.equals(clientDigest));
            System.out.println(serverDigest);
            filterChain.doFilter(servletRequest, servletResponse);
        }


    }

    @Override
    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
