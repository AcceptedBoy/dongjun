package com.gdut.dongjun.strategy;

import com.gdut.dongjun.strategy.read.impl.CacheRandomRead;
import com.gdut.dongjun.strategy.send.impl.WaitConfirmTimeSend;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * 随机产生报文，并定时发送
 * Created by AcceptedBoy on 2016/9/1.
 */
public class RandomReadAndConfirmSend extends SimulateStrategy {

    private ChannelHandlerContext ctx;

    public RandomReadAndConfirmSend(List<String> cache, ChannelHandlerContext ctx, long waitTime) {
        setRead(new CacheRandomRead(cache));
        setSend(new WaitConfirmTimeSend(ctx, waitTime));
    }

    @Override
    public void readAndSend() {
        while(true) {
            send(read());
        }
    }
}