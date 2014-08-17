package com.edgar.core.shiro;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2014/8/17.
 */
public class ReplayAttackFilter extends AccessControlFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        String nonce = request.getParameter("nonce");
        long timestamp = NumberUtils.toLong(request.getParameter("timestamp"));
        long currentTime = System.currentTimeMillis();
        if (timestamp + 0.5 * 60 * 1000 < currentTime) {
            onTimeout(response);
            return false;
        }
        String replayKey = nonce + "-" + timestamp;
        System.out.println(replayKey);
//
//        // 4、生成无状态Token
//        StatelessToken token = new StatelessToken(accessToken, baseString,
//                clientDigest);
//
//        try {
//            // 5、委托给Realm进行登录
//            getSubject(request, response).login(token);
//        } catch (Exception e) {
//            e.printStackTrace();
//            onTimeout(response); // 6、登录失败
//            return false;
//        }
        return true;
    }

    // 登录失败时默认返回401状态码
    private void onTimeout(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
        httpResponse.getWriter().write("Request timed out!");
    }
}
