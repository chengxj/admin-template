package com.edgar.core.log;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.edgar.core.shiro.AuthHelper;

@Aspect
@Service
public class BusinessLogAspect {

        /**
         * 建立日志的切面方法.
         */
        @Pointcut("execution(@com.edgar.core.shiro.AuthHelper" + " * com.edgar..*Resource.*(..))")
        public void logInfoPointCut() {
        }

        /**
         * 创建日志.
         * 
         * @param jp
         *                拦截点对象
         * @param helper
         *                日志的annotation类
         */
        @After(value = "logInfoPointCut() && @annotation(helper)", argNames = "helper")
        public void afterUpdate(JoinPoint jp, AuthHelper helper) {
                Logger logger = LoggerFactory.getLogger(jp.getTarget().getClass());
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                                .currentRequestAttributes()).getRequest();
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
                logger.info("{},url:{}, httpMethod:{},httpParam:{},ip:{}", helper.value(), request.getRequestURL(), request.getMethod(), map,
                                getIpAddr(request));
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