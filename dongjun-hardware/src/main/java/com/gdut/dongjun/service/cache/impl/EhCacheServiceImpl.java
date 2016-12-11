package com.gdut.dongjun.service.cache.impl;

import com.gdut.dongjun.service.cache.CacheService;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

/**
 * Created by symon on 16-11-29.
 */
@ConfigurationProperties(prefix = "ehcache")
@Service
@Lazy(false)
public class EhCacheServiceImpl implements CacheService {

    @Autowired
    @Qualifier("cacheManager")
    private CacheManager cacheManager;

    @NotNull
    private String cacheName;

    public void setName(String name) {
        this.cacheName = name;
    }

    @Override
    public void put(String key, Object value) {
        cacheManager.getCache(cacheName).put(new Element(key, value));
    }

    @Override
    public Object get(String key) {
        if (cacheManager.getCache(cacheName).isKeyInCache(key) &&
                cacheManager.getCache(cacheName).get(key) != null) {
            return cacheManager.getCache(cacheName).get(key).getObjectValue();
        }else {
            return null;
        }
    }

    @Override
    public void removeAll() {
        cacheManager.getCache(cacheName).removeAll();
    }

    @Override
    public void remove(String key) {
        cacheManager.getCache(cacheName).remove(key);
    }
}
