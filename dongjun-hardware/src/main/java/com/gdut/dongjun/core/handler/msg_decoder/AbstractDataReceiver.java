package com.gdut.dongjun.core.handler.msg_decoder;

import java.util.List;

import org.apache.log4j.Logger;

import com.gdut.dongjun.core.handler.ParseStrategy;
import com.gdut.dongjun.util.CharUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public abstract class AbstractDataReceiver extends ChannelInboundHandlerAdapter {
	
	private Integer moduleTpye;
	protected Logger logger;
	protected ParseStrategy strategy;

	public AbstractDataReceiver(Integer moduleTpye, Logger logger) {
		this.moduleTpye = moduleTpye;
		this.logger = logger;
	}
	
	public void setParseStrategy(ParseStrategy strategy) {
		this.strategy = strategy;
	}

	/**
	 * 读取数据的入口
	 * 先验证是不是属于自己的报文，如果不是就抛给下一个Handler，如果是就先验证报文合法性，再读取报文信息。
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		List<Object> list = (List<Object>) msg;
		int type = (int) list.get(0);
		if (!(this.moduleTpye == type)) {
			ctx.fireChannelRead(msg);
			return;
		}
		String m = (String) list.get(1);
		char[] data = CharUtils.removeSpace(m.toCharArray());
		//进入解析流程
		strategy.parse(data, ctx);
	}
	
}
