package com.edgar.core.auth.stateless;

import com.edgar.core.cache.CacheWrapper;
import com.edgar.core.cache.EhCacheWrapper;
import net.sf.ehcache.CacheManager;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2014/8/17.
 */
public class ReplayAttackFilter extends AccessControlFilter {

    private CacheWrapper<String, String> cacheWrapper;

    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
        cacheWrapper = new EhCacheWrapper<String, String>("ReplayAttackCache", cacheManager);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        String nonce = request.getParameter("nonce");
        long timestamp = NumberUtils.toLong(request.getParameter("timestamp"));
        long currentTime = System.currentTimeMillis();
        if (timestamp + 5 * 60 * 1000 < currentTime) {
            onTimeout(response);
            return false;
        }
        String replayKey = nonce + "-" + timestamp;
        if (cacheWrapper.get(replayKey) != null) {
            onTimeout(response);
            return false;
        }
        cacheWrapper.put(replayKey, replayKey);
        return true;
    }

    // 登录失败时默认返回401状态码
    private void onTimeout(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
        httpResponse.getWriter().write("Request timed out!");
    }
}
