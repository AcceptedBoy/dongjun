package com.gdut.dongjun.core.cache.config;

import net.sf.ehcache.config.CacheConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * name:缓存名称。
 *        maxElementsInMemory：缓存最大个数。
 *        eternal:对象是否永久有效，一但设置了，timeout将不起作用。
 *        timeToIdleSeconds：设置对象在失效前的允许闲置时间（单位：秒）。仅当eternal=false对象不是永久有效时使用，可选属性，默认值是0，也就是可闲置时间无穷大。
 *        timeToLiveSeconds：设置对象在失效前允许存活时间（单位：秒）。最大时间介于创建时间和失效时间之间。仅当eternal=false对象不是永久有效时使用，默认是0.，也就是对象存活时间无穷大。
 *        overflowToDisk：当内存中对象数量达到maxElementsInMemory时，Ehcache将会对象写到磁盘中。
 *        diskSpoolBufferSizeMB：这个参数设置DiskStore（磁盘缓存）的缓存区大小。默认是30MB。每个Cache都应该有自己的一个缓冲区。
 *        maxElementsOnDisk：硬盘最大缓存个数。
 *        diskPersistent：是否缓存虚拟机重启期数据 Whether the disk store persists between restarts of the Virtual Machine. The default value is false.
 *        diskExpiryThreadIntervalSeconds：磁盘失效线程运行时间间隔，默认是120秒。
 *        memoryStoreEvictionPolicy：当达到maxElementsInMemory限制时，Ehcache将会根据指定的策略去清理内存。默认策略是LRU（最近最少使用）。你可以设置为FIFO（先进先出）或是LFU（较少使用）。
 *        clearOnFlush：内存数量最大时是否清除。
 */
@Configuration
@EnableCaching
@ConfigurationProperties(prefix = "ehcache")
public class EhCacheConfig implements CachingConfigurer {

    private volatile static net.sf.ehcache.CacheManager instance;

    //private static final Logger LOG = LoggerFactory.getLogger(EhCacheConfig.class);

    private String policy;

    private int ttl;

    private String name;

    @Bean(name = "cacheManager")
    public net.sf.ehcache.CacheManager getInstance(){
        if (instance == null){
            synchronized (EhCacheConfig.class){
                if (instance == null){
                    instance = ehCacheManager();
                }
            }
        }
        return instance;
    }

    @Bean(destroyMethod = "shutdown")
    public net.sf.ehcache.CacheManager ehCacheManager() {
        CacheConfiguration cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setName(name);
        cacheConfiguration.setEternal(false);
        cacheConfiguration.setMaxEntriesLocalHeap(1000);
        cacheConfiguration.setMemoryStoreEvictionPolicy(policy);
        cacheConfiguration.setTimeToLiveSeconds(ttl);

        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
        config.addCache(cacheConfiguration);
        return net.sf.ehcache.CacheManager.newInstance(config);
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }

    @Override
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheManager()); //for @Cachenable, call by spring
    }

    @Override
    public CacheResolver cacheResolver() {
        return null;
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return null;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getPolicy() {
        return policy;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public int getTtl() {
        return ttl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
