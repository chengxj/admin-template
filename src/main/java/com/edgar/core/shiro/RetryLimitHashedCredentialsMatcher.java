package com.edgar.core.shiro;

import java.util.concurrent.atomic.AtomicInteger;

import net.sf.ehcache.CacheManager;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;

import com.edgar.core.cache.EhCacheWrapper;

/**
 * 用户登录的校验类 如果密码错误的次数超过maxRetryCount定义的值，则抛出CustomExcessiveAttemptsException异常
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Deprecated
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

        private EhCacheWrapper<String, AtomicInteger> passwordRetryCache;

        private int maxRetryCount;

        @Autowired
        public void setCacheManager(CacheManager cacheManager) {
                this.passwordRetryCache = new EhCacheWrapper<String, AtomicInteger>(
                                "passwordRetryCache", cacheManager);
        }

        @Override
        public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

                String username = (String) token.getPrincipal();
                AtomicInteger retryCount = passwordRetryCache.get(username);
                if (retryCount == null) {
                        retryCount = new AtomicInteger(0);
                        passwordRetryCache.put(username, retryCount);
                }
                if (retryCount.incrementAndGet() > maxRetryCount) {
                        throw new CustomExcessiveAttemptsException(retryCount.get());
                }
                AuthContextHolder.setRetryCount(retryCount.get());

                boolean matches = super.doCredentialsMatch(token, info);
                if (matches) {
                        passwordRetryCache.remove(username);
                        AuthContextHolder.setRetryCount(0);
                }
                return matches;
        }

        public void setMaxRetryCount(int maxRetryCount) {
                this.maxRetryCount = maxRetryCount;
        }

}
