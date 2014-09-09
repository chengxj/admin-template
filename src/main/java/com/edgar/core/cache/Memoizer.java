package com.edgar.core.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Service
@Aspect
public class Memoizer implements ApplicationListener<ApplicationEvent> {

    private static final String CACHE_NAME = "Cache";
    private static final String KEY_CACHE_NAME = "KeyCache";

    private Cache cache;
    private Cache keyCache;

    @Autowired
    private CacheManager cacheManager;

    @PostConstruct
    public void init() {
        cache = cacheManager.getCache(CACHE_NAME);
        keyCache = cacheManager.getCache(KEY_CACHE_NAME);
    }

    @Around("@annotation(cacheable)")
    public Object aroundCacheableMethod(ProceedingJoinPoint pjp, Cacheable cacheable) throws Throwable {
        String objectKey = getObjectKey(pjp);
        Element result = cache.get(objectKey);

        if (result == null) {
            Object value = pjp.proceed();
            result = element(objectKey, value, cacheable.expiresInSec());
            cache.put(result);
            registerKeyCache(cacheable, objectKey);
        }

        return result.getObjectValue();
    }

    private String getObjectKey(ProceedingJoinPoint pjp) {
        String targetName = pjp.getTarget().getClass().getSimpleName();
        String methodName = pjp.getSignature().getName();
        Object[] args = pjp.getArgs();

        StringBuilder sb = new StringBuilder();
        sb.append(targetName).append(".").append(methodName);
        if (args != null) for (Object arg : args) sb.append(".").append(arg);
        return sb.toString();
    }

    private Element element(String key, Object value, int timeToLive) {
        Element element = new Element(key, value);
        element.setTimeToLive(timeToLive);
        return element;
    }

    private void registerKeyCache(Cacheable cacheable, String key) {
        Class<? extends ApplicationEvent> invalidatingEvent = cacheable.invalidatingEvent();
        if (invalidatingEvent == VoidEvent.class) {
            return;
        }

        Element result = keyCache.get(invalidatingEvent);
        Set<CachedKey> keys;

        if (result == null) {
            keys = new HashSet<CachedKey>();
            keyCache.put(new Element(invalidatingEvent, keys));
        } else {
            keys = (Set<CachedKey>)result.getObjectValue();
        }

        keys.add(new CachedKey(key, cacheable));
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        Element result = keyCache.get(event.getClass());
        if (result == null) return;

        Set<CachedKey> cachedKeys = (Set<CachedKey>)result.getObjectValue();
        Set<CachedKey> toBeRemoved = new HashSet<CachedKey>();
        for (CachedKey cachedKey : cachedKeys) {
            if (cachedKey.invalidate(cache, event)) {
                toBeRemoved.add(cachedKey);
            }
        }

        cachedKeys.removeAll(toBeRemoved);
    }
}