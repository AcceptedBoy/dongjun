package com.gdut.dongjun.core.handler;

import io.netty.channel.ChannelHandlerContext;

public class DefaultParseStrategyAdaptor extends ParseStrategyAdaptor {

	private ParseStrategy parseStrategy;
	
	@Override
	public boolean applyParseStrategy(ParseStrategy strategy) {
		this.parseStrategy = strategy;
		return true;
	}

	@Override
	public Object doParse(char[] data, ChannelHandlerContext ctx) {
		return parseStrategy.parse(data, ctx);
	}

}
