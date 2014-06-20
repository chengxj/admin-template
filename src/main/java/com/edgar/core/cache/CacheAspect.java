package com.edgar.core.cache;

import java.util.List;
import java.util.Map;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edgar.core.repository.AbstractCrudRepositoryTemplate;
import com.edgar.core.repository.QueryExample;

/**
 * EhCache的AOP类，拦截持久层的get,update,delete方法并建立缓存 其中<code>update(QueryExample)</code>和
 * <code>delete(QueryExample)</code>方法会删除所有缓存
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Aspect
@Service
public class CacheAspect {

        @Autowired
        private CacheManager cacheManager;

        /**
         * get的切面.
         * 
         * @param pk
         *                主键
         */
        @Pointcut("execution(* com.edgar.core.repository.CrudRepository+.get(Object)) && args(pk)")
        public void getPointCut(Object pk) {
        }

        /**
         * 插入记录的切面.
         * 
         * @param domain
         *                实体对象
         */
        @Pointcut("execution(* com.edgar.core.repository.CrudRepository+.insert(Object)) && args(domain)")
        public void insertPointCut(Object domain) {

        }

        /**
         * 插入记录的切面.
         * 
         * @param domain
         *                实体对象
         */
        @SuppressWarnings("rawtypes")
        @Pointcut("execution(* com.edgar.core.repository.CrudRepository+.insert(java.util.List)) && args(domains)")
        public void insertListPointCut(List domains) {

        }

        /**
         * 根据主键修改记录的切面.
         * 
         * @param domain
         *                实体对象
         */
        @Pointcut("execution(* com.edgar.core.repository.CrudRepository+.update(Object)) && args(domain)")
        public void updatePointCut(Object domain) {

        }

        /**
         * 根据主键和时间戳修改记录的切面.
         * 
         * @param domain
         *                实体对象
         */
        @Pointcut("execution(* com.edgar.core.repository.CrudRepository+.updateByVersion(Object)) && args(domain)")
        public void updateByVersionPointCut(Object domain) {

        }

        /**
         * 根据条件修改记录的切面.
         * 
         * @param domain
         *                实体对象
         * @param example
         *                查询条件
         */
        @Pointcut("execution(* com.edgar.core.repository.CrudRepository+.update(Object, com.edgar.core.repository.QueryExample)) && args(domain, example)")
        public void updateByExamplePointCut(Object domain, QueryExample example) {

        }

        /**
         * 根据主键删除记录的切面.
         * 
         * @param pk
         *                主键
         */
        @Pointcut("execution(* com.edgar.core.repository.CrudRepository+.deleteByPk(Object)) && args(pk)")
        public void deletePointCut(Object pk) {

        }

        /**
         * 根据主键和时间戳删除记录的切面.
         * 
         * @param pk
         *                主键
         * @param updatedTime
         *                时间戳
         */
        @Pointcut("execution(* com.edgar.core.repository.CrudRepository+.deleteByPkAndVersion(Object, long)) && args(pk, updatedTime)")
        public void deleteByVersionPointCut(Object pk, long updatedTime) {

        }

        /**
         * 根据条件删除记录的切面.
         * 
         * @param 查询条件
         */
        @Pointcut("execution(* com.edgar.core.repository.CrudRepository+.delete(com.edgar.core.repository.QueryExample)) && args(example)")
        public void deleteByExamplePointCut(QueryExample example) {

        }

        /**
         * get方法的拦截
         * 
         * @param jp
         *                拦截点
         * @param pk
         *                主键
         * @return 实体类
         * @throws Throwable
         *                 异常
         */
        @SuppressWarnings("rawtypes")
        @Around(value = "getPointCut(pk)")
        public Object aroundGet(ProceedingJoinPoint jp, Object pk) throws Throwable {
                Logger logger = LoggerFactory.getLogger(jp.getTarget().getClass());
                AbstractCrudRepositoryTemplate t = (AbstractCrudRepositoryTemplate) jp.getTarget();
                if (!t.cacheEnabled()) {
                        return jp.proceed(jp.getArgs());
                }
                String cacheName = t.getEntityBeanType().getSimpleName() + "Cache";
                Ehcache cache = cacheManager.getEhcache(cacheName);
                if (cache != null) {
                        Object key = getCacheKey(pk);
                        Element element = cache.get(key);
                        if (element != null) {
                                Object value = element.getObjectValue();
                                logger.debug("get value from cache[{}],key:{},value:{}", cacheName,
                                                key, ToStringBuilder.reflectionToString(value,
                                                                ToStringStyle.SHORT_PREFIX_STYLE));
                                return value;
                        } else {
                                Object value = jp.proceed(jp.getArgs());
                                logger.debug("get value from db,key:{},value:{}", key,
                                                ToStringBuilder.reflectionToString(value,
                                                                ToStringStyle.SHORT_PREFIX_STYLE));
                                cache.put(new Element(key, value));
                                return value;
                        }
                }
                logger.debug("cache[{}]undefined", cacheName);
                return jp.proceed(jp.getArgs());
        }

        /**
         * 插入记录的拦截
         * 
         * @param jp
         *                拦截点
         * @param domain
         *                实体对象
         * @throws Throwable
         *                 异常
         */
        @SuppressWarnings({ "rawtypes", "unchecked" })
        @After(value = "insertPointCut(domain)")
        public void afterInsert(JoinPoint jp, Object domain) throws Throwable {
                Logger logger = LoggerFactory.getLogger(jp.getTarget().getClass());
                AbstractCrudRepositoryTemplate t = (AbstractCrudRepositoryTemplate) jp.getTarget();
                if (!t.cacheEnabled()) {
                        return;
                }
                String cacheName = t.getEntityBeanType().getSimpleName() + "Cache";
                Ehcache cache = cacheManager.getEhcache(cacheName);

                if (cache != null) {
                        Object pk = t.getPrimaryKeyValue(domain);
                        Object key = getCacheKey(pk);
                        logger.debug("put value into cache[{}],key:{},value:{}", cacheName, key,
                                        ToStringBuilder.reflectionToString(domain,
                                                        ToStringStyle.SHORT_PREFIX_STYLE));
                        if (BeanUtils.isSimpleValueType(pk.getClass())) {
                                cache.put(new Element(key, t.get(pk)));
                        }

                } else {
                        logger.debug("cache[{}]undefined", cacheName);
                }
        }

        /**
         * 插入记录的拦截
         * 
         * @param jp
         *                拦截点
         * @param domains
         *                实体对象的集合
         * @throws Throwable
         *                 异常
         */
        @SuppressWarnings({ "rawtypes", "unchecked" })
        @After(value = "insertListPointCut(domains)")
        public void afterInsertList(JoinPoint jp, List domains) throws Throwable {
                Logger logger = LoggerFactory.getLogger(jp.getTarget().getClass());
                AbstractCrudRepositoryTemplate t = (AbstractCrudRepositoryTemplate) jp.getTarget();
                if (!t.cacheEnabled()) {
                        return;
                }
                String cacheName = t.getEntityBeanType().getSimpleName() + "Cache";
                Ehcache cache = cacheManager.getEhcache(cacheName);

                if (cache != null) {
                        for (Object obj : domains) {
                                Object pk = t.getPrimaryKeyValue(obj);
                                Object key = getCacheKey(pk);
                                logger.debug("向缓存[{}]中放入值,键:{},值:{}", cacheName, key,
                                                ToStringBuilder.reflectionToString(obj,
                                                                ToStringStyle.SHORT_PREFIX_STYLE));
                                if (BeanUtils.isSimpleValueType(pk.getClass())) {
                                        cache.put(new Element(key, t.get(pk)));
                                }

                        }

                } else {
                        logger.debug("cache[{}]undefined", cacheName);
                }
        }

        /**
         * 根据主键修改记录的拦截
         * 
         * @param jp
         *                拦截点
         * @param domain
         *                实体对象
         * @throws Throwable
         *                 异常
         */
        @After(value = "updatePointCut(domain)")
        public void afterUpdate(JoinPoint jp, Object domain) throws Throwable {
                updateCache(jp, domain);
        }

        /**
         * 根据查询条件修改记录后的拦截
         * 
         * @param jp
         *                拦截点
         * @param domain
         *                实体对象
         * @param example
         *                查询条件
         * @throws Throwable
         *                 异常
         */
        @After(value = "updateByExamplePointCut(domain, example)")
        public void afterUpdateByExample(JoinPoint jp, Object domain, QueryExample example)
                        throws Throwable {
                removeAllCache(jp);
        }

        /**
         * 根据主键和时间戳修改记录的拦截
         * 
         * @param jp
         *                拦截点
         * @param domain
         *                实体对象
         * @throws Throwable
         *                 异常
         */
        @After(value = "updateByVersionPointCut(domain)")
        public void afterUpdateByVersion(JoinPoint jp, Object domain) throws Throwable {
                updateCache(jp, domain);
        }

        /**
         * 根据主键删除记录后的拦截
         * 
         * @param jp
         *                拦截点
         * @param pk
         *                主键
         * @throws Throwable
         *                 异常
         */
        @After(value = "deletePointCut(pk)")
        public void afterDeletePointCut(JoinPoint jp, Object pk) throws Throwable {
                deleteCache(jp, pk);
        }

        /**
         * 根据主键和时间戳删除记录后的拦截
         * 
         * @param jp
         *                拦截点
         * @param pk
         *                主键
         * @param updatedTime
         *                时间戳
         * @throws Throwable
         *                 异常
         */
        @After(value = "deleteByVersionPointCut(pk, updatedTime)")
        public void afterDeleteByVersionPointCut(JoinPoint jp, Object pk, long updatedTime)
                        throws Throwable {
                deleteCache(jp, pk);
        }

        /**
         * 根据查询条件删除记录后的拦截
         * 
         * @param jp
         *                拦截点
         * @param example
         *                查询条件
         * @throws Throwable
         *                 异常
         */
        @After(value = "deleteByExamplePointCut(example)")
        public void afterDeleteByExamplePointCut(JoinPoint jp, QueryExample example)
                        throws Throwable {
                removeAllCache(jp);
        }

        /**
         * 更新cache
         * 
         * @param jp
         *                拦截点
         * @param domain
         *                实体对象
         */
        @SuppressWarnings({ "rawtypes", "unchecked" })
        private void updateCache(JoinPoint jp, Object domain) {
                Logger logger = LoggerFactory.getLogger(jp.getTarget().getClass());
                AbstractCrudRepositoryTemplate t = (AbstractCrudRepositoryTemplate) jp.getTarget();
                if (!t.cacheEnabled()) {
                        return;
                }
                String cacheName = t.getEntityBeanType().getSimpleName() + "Cache";
                Ehcache cache = cacheManager.getEhcache(cacheName);

                if (cache != null) {
                        Object pk = t.getPrimaryKeyValue(domain);
                        Object key = getCacheKey(pk);
                        cache.remove(key);
                        Object value = t.get(pk);
                        logger.debug("update value in cache[{}],key:{},value:{}", cacheName, key,
                                        ToStringBuilder.reflectionToString(value,
                                                        ToStringStyle.SHORT_PREFIX_STYLE));
                        cache.put(new Element(key, value));

                } else {
                        logger.debug("cache[{}]undefined", cacheName);
                }
        }

        /**
         * 根据主键删除cache
         * 
         * @param jp
         *                拦截点
         * @param pk
         *                主键
         */
        @SuppressWarnings("rawtypes")
        private void deleteCache(JoinPoint jp, Object pk) {
                Logger logger = LoggerFactory.getLogger(jp.getTarget().getClass());
                AbstractCrudRepositoryTemplate t = (AbstractCrudRepositoryTemplate) jp.getTarget();
                if (!t.cacheEnabled()) {
                        return;
                }
                String cacheName = t.getEntityBeanType().getSimpleName() + "Cache";
                Ehcache cache = cacheManager.getEhcache(cacheName);

                if (cache != null) {
                        Object key = getCacheKey(pk);
                        logger.debug("remove from cache[{}],key:{}", cacheName, key);
                        cache.remove(key);

                } else {
                        logger.debug("cache[{}]undefined", cacheName);
                }
        }

        /**
         * 删除所有的cache
         * 
         * @param jp
         *                拦截点
         */
        @SuppressWarnings("rawtypes")
        private void removeAllCache(JoinPoint jp) {
                Logger logger = LoggerFactory.getLogger(jp.getTarget().getClass());
                AbstractCrudRepositoryTemplate t = (AbstractCrudRepositoryTemplate) jp.getTarget();
                if (!t.cacheEnabled()) {
                        return;
                }
                String cacheName = t.getEntityBeanType().getSimpleName() + "Cache";
                Ehcache cache = cacheManager.getEhcache(cacheName);

                if (cache != null) {
                        cache.removeAll();
                        logger.debug("clear cache[{}]", cacheName);

                } else {
                        logger.debug("cache[{}]undefined", cacheName);
                }
        }

        /**
         * 根据主键生成ehcache的键值，如果是primitive,String ,CharSequence,Number,Date,URI,URL,
         * Locale,Class，则使用主键，其他类型的主键使用格式化后的字符串
         * 
         * @param pk
         *                主键
         * @return cache的键值
         */
        private Object getCacheKey(Object pk) {
                if (BeanUtils.isSimpleValueType(pk.getClass())) {
                        return pk;
                }
                if (pk instanceof Map) {
                        return pk;
                }
                return ToStringBuilder.reflectionToString(pk, ToStringStyle.SHORT_PREFIX_STYLE);
        }
}
