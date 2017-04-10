package com.gdut.dongjun.service.cache;

/**
 * <strong>Cache interface </strong><br>
 */
public interface CacheService {
    /**
     * caching a object
     *
     */
    public void put(String key, Object value);

    /**
     * get cache
     *
     */
    public Object get(String key);

    /**
     * clear all
     */
    public void removeAll();

    /**
     * remove some
     */
    public void remove(String key);
}
