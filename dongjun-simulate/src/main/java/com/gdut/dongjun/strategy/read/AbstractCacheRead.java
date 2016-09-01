package com.gdut.dongjun.strategy.read;

import java.util.List;

/**
 * Created by AcceptedBoy on 2016/9/1.
 */
public abstract class AbstractCacheRead implements Read {

    protected List<String> cache;

    protected Integer cacheSize;

    public AbstractCacheRead(List<String> cache) {
        this.cache = cache;
        cacheSize = cache.size();
    }
}
