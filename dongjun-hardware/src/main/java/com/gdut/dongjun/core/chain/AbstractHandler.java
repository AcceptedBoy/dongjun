package com.gdut.dongjun.core.chain;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by AcceptedBoy on 2016/9/16.
 */
public abstract class AbstractHandler {

    public abstract boolean handleRequest(ChannelHandlerContext ctx, String data, String functionData);
}
