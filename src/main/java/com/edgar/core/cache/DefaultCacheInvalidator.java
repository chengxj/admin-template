package com.edgar.core.cache;

import net.sf.ehcache.Cache;
import org.springframework.context.ApplicationEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 14-9-9
 * Time: 上午10:43
 * To change this template use File | Settings | File Templates.
 */
public class DefaultCacheInvalidator implements CacheInvalidator<ApplicationEvent> {

    public boolean invalidate(Cache cache, String key, ApplicationEvent invalidatingEvent) {
        cache.remove(key);
        return true;
    }
}
