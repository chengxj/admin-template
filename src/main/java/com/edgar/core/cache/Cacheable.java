package com.edgar.core.cache;

import org.springframework.context.ApplicationEvent;

public @interface Cacheable {
    // cache entry expires in 1 hour by default
    int expiresInSec() default 3600;

    Class<? extends ApplicationEvent> invalidatingEvent() default VoidEvent.class;

    Class<? extends CacheInvalidator> invalidationStrategy() default DefaultCacheInvalidator.class;
}
