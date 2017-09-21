package com.zlikun.learning;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.statistics.StatisticsGateway;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017/9/21 10:03
 */
@Slf4j
public class CacheUsageTest {

    private CacheManager cacheManager = CacheManager.getInstance() ;

    @Test
    public void usage() {

        // 构造一个缓存实例
        Cache cache = new Cache("hello" ,3 , false ,false ,60 ,30) ;

        // 使用Cache实例前，必须将Cache实例加入到CacheManager中
        cacheManager.addCache(cache);

        // 添加一个缓存，Element实例非空，但内部键、值皆可为空(键空不建议)
        // 键值类型都必须实现序列化接口
        cache.put(new Element("name" ,"zlikun"));

        // 更新一个键的值(重新put一次即可)
        cache.put(new Element("name" ,"kafka"));

        // 获取一个键值对
        Element element = cache.get("name") ;
        Assert.assertNotNull(element);
        Assert.assertEquals("name" ,element.getObjectKey());
        Assert.assertEquals("kafka" ,element.getObjectValue());
        // 佐证键值类型为 Serializable 类型
        Assert.assertTrue(element.getObjectKey() instanceof Serializable);
        Assert.assertTrue(element.getObjectValue() instanceof Serializable);

        // 删除一个键，返回删除状态
        Assert.assertTrue(cache.remove("name")) ;
        Assert.assertFalse(cache.remove("name")) ;

        // 获取缓存使用量
        Assert.assertEquals(0 ,cache.getSize());
        // 获取缓存统计信息
        StatisticsGateway sg = cache.getStatistics() ;
        log.info("LocalDiskSize : {}" ,sg.getLocalDiskSize());
        log.info("LocalOffHeapSize(MemoryStoreSize) : {}" ,sg.getLocalOffHeapSize());

        // 关闭CacheManager
        cacheManager.shutdown();

    }

}
