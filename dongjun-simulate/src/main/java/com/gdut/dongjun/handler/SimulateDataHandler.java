package com.gdut.dongjun.handler;

import com.gdut.dongjun.base.AbstractSimulateSend;
import com.gdut.dongjun.base.DefaultSimulateSend;
import com.gdut.dongjun.constant.Constant;
import com.gdut.dongjun.strategy.RandomReadAndConfirmSend;
import com.gdut.dongjun.strategy.SimulateStrategy;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.List;

/**
 * Created by AcceptedBoy on 2016/8/30.
 */
public class SimulateDataHandler extends ChannelInboundHandlerAdapter {

    private SimulateStrategy strategy;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        AbstractSimulateSend send = new DefaultSimulateSend();
        send.createSourceCache();
        List<String> cache = send.getCache();
        strategy = new RandomReadAndConfirmSend(cache, ctx, Constant.AVERAGE_SEND_SECOND);
        strategy.readAndSend();
    }

    /*private static class SendWorker extends Thread {

        private List<String> cache;

        private ChannelHandlerContext ctx;

        public SendWorker(List<String> cache, ChannelHandlerContext ctx) {
            this.cache = cache;
            this.ctx = ctx;
        }

        @Override
        public void run() {
            if(Constant.SEND_STRATEGY == SendStrategy.RANDOM) {
                for(int i = 0, length = cache.size(); i < length; ++i) {
                    ctx.writeAndFlush(cache.get(i));
                    try {
                        Thread.sleep((int)Math.random());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if(Constant.SEND_STRATEGY == SendStrategy.UNIFORMITY) {
                for(int i = 0, length = cache.size(); i < length; ++i) {
                    ctx.writeAndFlush(cache.get(i));
                    try {
                        Thread.sleep(Constant.AVERAGE_SEND_SECOND);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }*/

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
