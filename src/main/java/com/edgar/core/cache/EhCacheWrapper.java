package com.edgar.core.cache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * EhCache缓存的实现
 * 
 * @author Edgar Zhang
 * @version 1.0
 * @param <K>
 *                缓存的键值
 * @param <V>
 *                缓存的值
 */
public class EhCacheWrapper<K, V> implements CacheWrapper<K, V> {
        private final String cacheName;
        private final CacheManager cacheManager;

        public EhCacheWrapper(final String cacheName, final CacheManager cacheManager) {
                this.cacheName = cacheName;
                this.cacheManager = cacheManager;
        }

        public Ehcache getCache() {
                return cacheManager.getEhcache(cacheName);
        }

        @Override
        public void put(K key, V value) {
                getCache().put(new Element(key, value));
        }

        @SuppressWarnings("unchecked")
        @Override
        public V get(K key) {
                Element element = getCache().get(key);
                if (element != null) {
                        return (V) element.getObjectValue();
                }
                return null;
        }

        @Override
        public void remove(K key) {
                getCache().remove(key);
        }
        
        @Override
        public void removeAll() {
                getCache().removeAll();;
        }

}