package com.edgar.core.cache;

import net.sf.ehcache.Cache;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 14-9-9
 * Time: 上午10:43
 * To change this template use File | Settings | File Templates.
 */
public interface CacheInvalidator<T extends ApplicationEvent> extends Serializable {

    boolean invalidate(Cache cache, String key, T invalidatingEvent);
}
