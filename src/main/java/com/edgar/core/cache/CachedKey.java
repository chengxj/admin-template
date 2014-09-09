package com.edgar.core.cache;

import net.sf.ehcache.Cache;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

public class CachedKey implements Serializable {

    private final String key;
    private final Cacheable cacheable;

    private CacheInvalidator invalidator;

    public CachedKey(String key, Cacheable cacheable) {
        this.key = key;
        this.cacheable = cacheable;
    }

    public <T extends ApplicationEvent> boolean invalidate(Cache cache, T invalidatingEvent) {
        return getInvalidator().invalidate(cache, key, invalidatingEvent);
    }

    private CacheInvalidator getInvalidator() {
        if (invalidator == null) {
            try {
                invalidator = cacheable.invalidationStrategy().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return invalidator;
    }

    public String getKey() {
        return key;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CachedKey ? key.equals(((CachedKey)obj).key) : false;
    }
}