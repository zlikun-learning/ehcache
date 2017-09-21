package com.zlikun.learning;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.junit.Assert;
import org.junit.Test;

/**
 * Explicit Locking Test
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017/9/21 13:12
 */
public class ExplicitLockingTest {

    private CacheManager cacheManager = CacheManager.getInstance() ;

    @Test
    public void test() {

        Cache cache = cacheManager.getCache("simple") ;

        String key = "name" ;

        // 写锁
        cache.acquireWriteLockOnKey(key);
        try {
            cache.put(new Element(key ,"zlikun"));
        } finally {
            cache.releaseWriteLockOnKey(key);
        }

        // 读锁
        cache.acquireReadLockOnKey(key);
        try {
            Element element = cache.get(key) ;
            Assert.assertNotNull(element);
            Assert.assertNotNull(element.getObjectValue());
        } finally {
            cache.releaseReadLockOnKey(key);
        }

    }

}
