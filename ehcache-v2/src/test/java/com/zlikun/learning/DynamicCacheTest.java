package com.zlikun.learning;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 动态添加Cache实例
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017/9/21 9:48
 */
public class DynamicCacheTest {

    private CacheManager cacheManager ;

    @Before
    public void init () {
        cacheManager = CacheManager.getInstance() ;
    }

    @Test
    public void add() {

        // 通过一个配置实例来构造一个Cache实例
        Cache hello = new Cache(
                new CacheConfiguration("hello" ,3)
                    .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
                    .eternal(false)
                    .timeToLiveSeconds(60)
                    .timeToIdleSeconds(30)
                    .diskExpiryThreadIntervalSeconds(0)
                    .persistence(new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.LOCALTEMPSWAP))
        ) ;

        // 将Cache实例加入到CacheManager中
        cacheManager.addCache(hello);

        // 测试该Cache实例
        Cache cache = cacheManager.getCache("hello") ;

        Assert.assertNotNull(cache);

        cache.put(new Element("name" ,"zlikun"));
        Element element = cache.get("name") ;
        Assert.assertNotNull(element);
        Assert.assertEquals("zlikun" ,element.getObjectValue());
        // 证明过期时间为30秒
        Assert.assertTrue(System.currentTimeMillis() + 30000 >= element.getExpirationTime());

        // 删除指定缓存实例
        cacheManager.removeCache("hello");
        // 删除全部缓存实例
        cacheManager.removeAllCaches();
    }

}
