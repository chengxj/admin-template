package com.edgar.core.shiro;

import java.util.concurrent.atomic.AtomicInteger;

import net.sf.ehcache.CacheManager;

import org.springframework.beans.factory.annotation.Autowired;

import com.edgar.core.cache.EhCacheWrapper;

public class RetryLimitService {
	private EhCacheWrapper<String, AtomicInteger> passwordRetryCache;

	private int maxRetryCount;

	@Autowired
	public void setCacheManager(CacheManager cacheManager) {
		this.passwordRetryCache = new EhCacheWrapper<String, AtomicInteger>(
				"passwordRetryCache", cacheManager);
	}

	public void addRetry(String username) {

		AtomicInteger retryCount = passwordRetryCache.get(username);
		if (retryCount == null) {
			retryCount = new AtomicInteger(0);
			passwordRetryCache.put(username, retryCount);
		}
		AuthContextHolder.setRetryCount(retryCount.get());
		if (retryCount.incrementAndGet() > maxRetryCount) {
			throw new CustomExcessiveAttemptsException(retryCount.get());
		}
	}
	
	public void removeRetry(String username) {
		passwordRetryCache.remove(username);
		AuthContextHolder.setRetryCount(0);
	}

	public void setMaxRetryCount(int maxRetryCount) {
		this.maxRetryCount = maxRetryCount;
	}
}
