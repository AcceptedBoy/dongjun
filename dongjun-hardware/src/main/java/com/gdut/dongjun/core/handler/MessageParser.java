package com.gdut.dongjun.core.handler;

import io.netty.channel.ChannelHandlerContext;

/**
 * 报文解析类
 * @author Gordan_Deng
 * @date 2017年5月14日
 */
public interface MessageParser {

	public Object parse(char[] data, ChannelHandlerContext ctx);
}
