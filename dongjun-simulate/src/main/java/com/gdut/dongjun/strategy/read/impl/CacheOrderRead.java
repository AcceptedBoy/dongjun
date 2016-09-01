package com.gdut.dongjun.strategy.read.impl;

import com.gdut.dongjun.strategy.read.AbstractCacheRead;

import java.util.List;

/**
 * 报文缓存有序发送
 * Created by AcceptedBoy on 2016/9/1.
 */
public class CacheOrderRead extends AbstractCacheRead {

    private volatile static int index = 0;

    public CacheOrderRead(List<String> cache) {
        super(cache);
    }

    @Override
    public Object read() {
        if(index == cacheSize) {
            index = 0;
        }
        return cache.get(index++);
    }
}
