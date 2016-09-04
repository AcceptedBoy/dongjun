package com.gdut.dongjun.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Server Handler
 * Created by AcceptedBoy on 2016/8/28.
 */
public class SimulateServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println((String)msg);
    }
}
