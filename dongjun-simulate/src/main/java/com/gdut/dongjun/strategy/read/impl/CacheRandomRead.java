package com.gdut.dongjun.strategy.read.impl;

import com.gdut.dongjun.strategy.read.AbstractCacheRead;

import java.util.List;
import java.util.Random;

public class CacheRandomRead extends AbstractCacheRead {

    private Random random;

    public CacheRandomRead(List<String> cache) {
        super(cache);
        random = new Random();
    }

    @Override
    public Object read() {
        return cache.get(random.nextInt(cacheSize));
    }
}
