package com.gdut.dongjun.handler;

import com.gdut.dongjun.util.HexString_BytesUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * 以十六进制的字符和二进制流的编码器
 */
public class HexStringEncoder extends ChannelOutboundHandlerAdapter {

	@Override
	public void write(ChannelHandlerContext ctx, Object msg,
			ChannelPromise promise) {
		
		ByteBuf encoded = ctx.alloc().buffer(2);
		byte[] bytes = HexString_BytesUtil.hexStringToBytes((String) msg);
		encoded.writeBytes(bytes);
		ctx.write(encoded);
	}
}
