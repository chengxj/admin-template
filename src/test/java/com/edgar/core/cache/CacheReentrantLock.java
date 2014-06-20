package com.edgar.core.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

public class CacheReentrantLock extends ReentrantLock {

        private static final ConcurrentMap<String, ReentrantLock> map = new ConcurrentHashMap<String, ReentrantLock>();
        private static final Object lock = new Object();
        public static ReentrantLock getLock(String key) {
                synchronized (lock) {
                        if (map.containsKey(key)) {
                                return map.get(key);
                        }
                        ReentrantLock lock = new ReentrantLock();
                         map.putIfAbsent(key, lock);
                        return lock;
                }
                
        }

}
