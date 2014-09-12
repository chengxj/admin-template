package com.edgar.core.mvc;

import com.edgar.core.shiro.AuthHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Administrator on 14-9-12.
 */
public class LoggerInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            Logger logger = LoggerFactory.getLogger(method.getBeanType());
            AuthHelper authHelper = method.getMethodAnnotation(AuthHelper.class);
            Map<String, String[]> parameterMap = request.getParameterMap();
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
            for (String key : parameterMap.keySet()) {
                if (key.equalsIgnoreCase("password")) {
                    continue;
                }
                String[] v = parameterMap.get(key);
                for (String string : v) {
                    map.add(key, string);
                }
            }
            if (authHelper != null) {
                logger.info("{},url:{}, httpMethod:{},httpParam:{},ip:{}", authHelper.value(), request.getRequestURL(), request.getMethod(), map,
                        getIpAddr(request));
            } else {
                logger.info("url:{}, httpMethod:{},httpParam:{},ip:{}", request.getRequestURL(), request.getMethod(), map,
                        getIpAddr(request));
            }
        }

        return super.preHandle(request, response, handler);
    }

    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
