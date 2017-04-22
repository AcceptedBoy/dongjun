package com.gdut.dongjun.service.cache.manager;

import com.gdut.dongjun.service.cache.CacheService;
import com.gdut.dongjun.util.SpringApplicationContextHolder;

public abstract class EhCacheManager {

	protected static CacheService cacheService;
	
	protected EhCacheManager() {
		if (null == cacheService) {
			synchronized(EhCacheManager.class) {
				if (null == cacheService) {
					cacheService = (CacheService)SpringApplicationContextHolder.getSpringBean("cacheService");
				}
			}
		}
	}
	
	public Object get(String key) {
		return cacheService.get(key); 
	}
	
    public void put(String key, Object value) {
    	cacheService.put(key, value);
    }
    
    public void remove(String key) {
    	cacheService.remove(key);
    }
}
