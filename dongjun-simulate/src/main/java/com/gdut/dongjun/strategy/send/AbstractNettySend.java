package com.gdut.dongjun.strategy.send;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by AcceptedBoy on 2016/9/1.
 */
public abstract class AbstractNettySend implements Send {

    protected ChannelHandlerContext ctx;

    protected AbstractNettySend(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }
}
