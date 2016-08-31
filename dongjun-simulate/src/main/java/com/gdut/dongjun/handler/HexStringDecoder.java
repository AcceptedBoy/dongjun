package com.gdut.dongjun.handler;

import com.gdut.dongjun.util.HexString_BytesUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 以十六进制的字符和二进制流的译码器
 */
public class HexStringDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) {

		if (in.readableBytes() < 6) {
			return;
		}
		byte[] bytes = new byte[in.writerIndex()];
		in.readBytes(bytes);
		out.add(HexString_BytesUtil.bytesToHexString(bytes));
	}
}
