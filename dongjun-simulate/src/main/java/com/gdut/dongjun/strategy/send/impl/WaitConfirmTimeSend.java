package com.gdut.dongjun.strategy.send.impl;

import com.gdut.dongjun.strategy.send.AbstractNettySend;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by AcceptedBoy on 2016/9/1.
 */
public class WaitConfirmTimeSend extends AbstractNettySend {

    private long waitTime;

    public WaitConfirmTimeSend(ChannelHandlerContext ctx, long waitTime) {
        super(ctx);
        this.waitTime = waitTime;
    }

    @Override
    public void send(Object msg) {
        ctx.writeAndFlush(msg);
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
