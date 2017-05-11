package com.gdut.dongjun.core.handler.msg_decoder;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Component
public class TestDataReceiver extends ChannelInboundHandlerAdapter {

	private Logger logger = Logger.getLogger(TestDataReceiver.class);
	
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		logger.info("解析报文Handler---TestDataReceiver正式启用");
		super.channelRegistered(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info("爆炸！！！");
		super.channelRead(ctx, msg);
	}

	
}
