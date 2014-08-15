package com.edgar.core.shiro;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.TeeInputStream;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 14-8-13
 * Time: 下午2:27
 * To change this template use File | Settings | File Templates.
 */
public class DigestInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.println(request.getParameterMap());
//        StringWriter sb = new StringWriter();
//        Object requestBody = RequestContextHolder.currentRequestAttributes().getAttribute("edgarabcd", RequestAttributes.SCOPE_REQUEST);
//        Object requestBody = request.getAttribute(CustomJsonHttpMessageConverter.REQUEST_BODY_ATTRIBUTE_NAME);

//        final ByteArrayOutputStream writerStream = new ByteArrayOutputStream();
//        new TeeInputStream(request.getInputStream(), writerStream);
//
//        System.out.println(writerStream.toString());
//        System.out.println("interceptor");
//        if(requestBody != null) {
//            sb.append(requestBody.toString());
//            System.out.println(requestBody.getClass());
//        }



//        if (handler instanceof HandlerMethod) {
//            HandlerMethod method = (HandlerMethod) handler;
//            MethodParameter[] parameters = method.getMethodParameters();
//            for (MethodParameter p : parameters) {
//                System.out.println(p.getClass());
//            }
//            System.out.println(parameters.length);
//        }
//        String url = request.getRequestURL().toString();
//        String roleName = request.getParameter("roleName");
//        System.out.println(roleName);
//        String clientDigest = request.getParameter("digest");
//        List<String> set = new ArrayList<String>();
//        Enumeration<String> names = request.getParameterNames();
//        while (names.hasMoreElements()) {
//            String name = names.nextElement();
//            if (!"digest".equals(name)) {
//                set.add(name);
//            }
//        }
//        Collections.sort(set);
//        List<String> queryString = new ArrayList<String>(set.size());
//
//        for (String s : set) {
//            String[] v = request.getParameterValues(s);
//            List<String> value = new ArrayList<String>();
//            Collections.addAll(value, v);
//            Collections.sort(value);
//            queryString.add(s + "=" + StringUtils.join(value, ","));
//        }
//        String contextPah = request.getContextPath();
//        String method = request.getMethod();
//        StringBuilder baseString = new StringBuilder(method).append(StringUtils.substringAfter(url, contextPah + "/"));
//        baseString.append("?").append(StringUtils.join(queryString, "&"));
//        String serverDigest = HmacSHA256Utils.digest("aaaaaaaaaaaaa", baseString.toString());
//                System.out.println(IOUtils.toString(request.getInputStream(), "UTF-8"));
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        super.postHandle(request, response, handler, modelAndView);
    }
}
