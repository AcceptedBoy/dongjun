package com.gdut.dongjun.handler;

import com.gdut.dongjun.AbstractSimulateSend;
import com.gdut.dongjun.Constant.Constant;
import com.gdut.dongjun.Constant.SendStrategy;
import com.gdut.dongjun.DefaultSimulateSend;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.List;

/**
 * Created by AcceptedBoy on 2016/8/30.
 */
public class SimulateDataHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        AbstractSimulateSend send = new DefaultSimulateSend();
        send.createSourceCache();
        List<String> cache = send.getCache();
        for(int i = 0, length = cache.size();; ++i) {
            if(i == length) {
                i = 0;
                continue;
            }
            ctx.writeAndFlush(cache.get(i));

            Thread.sleep(Constant.SEND_STRATEGY == SendStrategy.RANDOM ?
                    (int)Math.random() : Constant.AVERAGE_SEND_SECOND);
        }
    }

    /**
     * 因为在这个数据模拟客户端中，没有接收数据的功能，所以不对其进行重写
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
