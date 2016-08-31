package com.gdut.dongjun.main;

import com.gdut.dongjun.constant.Constant;
import com.gdut.dongjun.handler.HexStringDecoder;
import com.gdut.dongjun.handler.HexStringEncoder;
import com.gdut.dongjun.handler.SimulateDataHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 开始发送模拟数据的类，有main方法
 * Created by AcceptedBoy on 2016/8/28.
 */
public class SimulateClient {

    public static void main(String[] args) {
        new SimulateClient().connect(Constant.SEND_IP, Constant.SEND_PORT);
    }

    public void connect(String address, int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new HexStringEncoder());
                            socketChannel.pipeline().addLast(new HexStringDecoder());
                            socketChannel.pipeline().addLast(new SimulateDataHandler());
                        }
                    });
            ChannelFuture f = b.connect(address, port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    /*public static void main(String[] args) throws IOException, InterruptedException {
        AbstractSimulateSend send = new DefaultSimulateSend();
        send.simulateSend();
    }*/
}
