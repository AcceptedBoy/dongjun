package com.gdut.dongjun.core.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class OverlongTextDecoder extends ModifiedDecoder {
	/*
	 * 104(10) = 68(16)
	 * 22(10) = 16(16)
	 */
	
	/** 上一则报文的writerIndex所在的地方 */
	private int preIndex;
	
	private static final Logger logger = LoggerFactory.getLogger(OverlongTextDecoder.class);

	public OverlongTextDecoder() {
		super();
		this.preIndex = 0;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		Object decoded = decode(ctx, in);
		if (decoded != null) {
			out.add(decoded);
		}
	}
	
	protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {

		final int eol = findEndOfLine(buffer);
		/* 代表没有缓存过报文 */
		if (eol == buffer.writerIndex() && preIndex == 0) {
			final ByteBuf frame;
			frame = buffer.readSlice(buffer.writerIndex() - buffer.readerIndex());
			buffer.clear();
			return frame.retain();
		} else {
			/* 如果接下来的报文是68开头，从readerIndex读到preIndex，然后readerIndex到writerIndex的数据移到ByteBuf初始位 */
			if (buffer.getByte(preIndex) == 104) {
				final ByteBuf frame;
				frame = buffer.readSlice(preIndex - buffer.readerIndex());
				buffer.readerIndex(preIndex);
				if (eol == buffer.writerIndex()) {
					//如果真发生丢包，原残缺报文加上特殊结尾段“ffff16”，方便报文解析
					byte[] specialEnd = {(byte) 255, (byte) 255, 22};
					frame.writeBytes(specialEnd);
					frame.writeBytes(buffer.readSlice(buffer.writerIndex() - preIndex));
					buffer.clear();
					preIndex = 0;
				} else {
					buffer.discardReadBytes(); //去除已读取字节
					preIndex = buffer.writerIndex();
				}
				return frame.retain();
			}
			/* 如果是16结尾，则readerIndex读取到writerIndex */
			else if (eol == buffer.writerIndex()) {
				final ByteBuf frame;
				frame = buffer.readSlice(buffer.writerIndex() - buffer.readerIndex());
				preIndex = 0;
				buffer.clear();
				return frame.retain();
			}
			else if (eol == -1) {
				//TODO
				this.preIndex = buffer.writerIndex();
				return null;
			}
			else {
				logger.warn("unknown mistake occured in OverlongTextDecoder");
			}
		}
		return null;
	}

	private int findEndOfLine(ByteBuf buffer) {
		int i = buffer.readerIndex();
		int writerIndex = buffer.writerIndex();
		for (; i < writerIndex; i++) {
			byte b = buffer.getByte(i);
//			if (b == '1' && buffer.getByte(i + 1) == '6' && i == writerIndex - 2 ) {
//				return writerIndex;
//			}
			if (b == 22 && i == writerIndex - 1) {
				return writerIndex;
			}
			System.out.println(b);
		}
		return -1;
	}
	
	

}
