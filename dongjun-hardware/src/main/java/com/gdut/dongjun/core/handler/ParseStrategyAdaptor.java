package com.gdut.dongjun.core.handler;

import io.netty.channel.ChannelHandlerContext;

public abstract class ParseStrategyAdaptor {

	public abstract boolean applyParseStrategy(ParseStrategy strategy);
	
	public abstract Object doParse(char[] data, ChannelHandlerContext ctx);
}
