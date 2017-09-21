package com.zlikun.learning;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * EhCache-v2 基本用法示例
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017/9/21 9:01
 */
@Slf4j
public class CacheManagerTest {

    private CacheManager cacheManager ;

    @Before
    public void init() {
        // 使用默认配置
        cacheManager  = CacheManager.getInstance() ;
    }

    /**
     * 测试几种创建方法是否都是单例
     */
    @Test
    public void compare() {
        Assert.assertEquals(CacheManager.newInstance() ,CacheManager.create());
        Assert.assertEquals(CacheManager.getInstance() ,CacheManager.create());

        // 使用 new CacheManager() 会报错：
        // The source of the existing CacheManager is: DefaultConfigurationSource [ ehcache.xml or ehcache-failsafe.xml ]
        // Assert.assertNotEquals(new CacheManager() ,new CacheManager());
    }

    @Test
    public void cacheManager() {
        // cacheNames = simple
        log.info("cacheNames = {}" ,cacheManager.getCacheNames());
    }

    @Test
    public void cache() {

        // 添加一个缓存(名称，使用默认配置)，`IfAbsent`表示当原来不存在时才创建，否则使用原来的
        Ehcache helloCache = cacheManager.addCacheIfAbsent("hello") ;

        // 获取一个缓存实例
        Cache cache = cacheManager.getCache("hello") ;

        // 基于名称取得缓存对象是单例的
        Assert.assertEquals(helloCache ,cache);

        // 添加缓存键值对
        cache.put(new Element("name" ,"zlikun"));

        Element element = cache.get("name") ;
        Assert.assertNotNull(element);
        Assert.assertEquals("zlikun" ,element.getObjectValue());

        // 当查询的键不存在时，返回的Element整个都为空，而非仅值为空
        Assert.assertNull(cache.get("age"));

        // 存入缓存的值允许为空
        cache.put(new Element("ctime" ,null));
        // 此时取得的Element不为空
        Assert.assertNotNull(cache.get("ctime"));
        // Element中键非空
        Assert.assertEquals("ctime" ,cache.get("ctime").getObjectKey());
        // Element中值为空
        Assert.assertNull(cache.get("ctime").getObjectValue());

        // 存入时键值皆可为空，但实际无法取出(空指针异常)
        cache.put(new Element(null ,"zlikun"));

    }

}
