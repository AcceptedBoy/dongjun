package com.gdut.dongjun.service.cache;

/**
 * EhCache缓存
 * @author Gordan_Deng
 * @date 2017年2月18日
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
