package com.gdut.dongjun.core.handler;

import java.util.List;

import org.apache.log4j.Logger;

import com.gdut.dongjun.util.HexString_BytesUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * 解决报文拆包
 * 注意，未处理前的byte[]里面是一个字节代表一个16进制数字
 * 通过此{@code HexString_BytesUtil.bytesToHexString(byte[] src)}处理后的byte数组是2个byte代表一个16进制数字
 * @author Gordan_Deng
 * @date 2017年3月24日
 */
public class SeparatedTextDecoder extends ByteToMessageDecoder {

	//字节68转为10进制之后的数字
	private static final Integer CODE_68 = 68 * 16 + 8;
	//字节16转为10进制之后的数字
	private static final Integer CODE_16 = 1 * 16 + 6;
	private static final Integer CODE_00 = 0;
	//gprs模块登录包字节长度
	private static final Integer GPRSLoginPackageLength = 37;
	//gprs模块心跳包字节长度
	private static final Integer GPRSOnlinePackageLegnth = 10;
	
	private static final Logger logger = Logger.getLogger(SeparatedTextDecoder.class);
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		
		//storeIndex是上一次未完整的报文的终点
		AttributeKey<Integer> key = AttributeKey.valueOf("storeIndex");
		Attribute<Integer> attr = ctx.attr(key);
		if (null == attr.get()) {
			attr.set(in.readerIndex());
		}
		
		Integer storeIndex = attr.get();
		Integer readerIndex = in.readerIndex();
		//移到新来的报文的起点，并读取新报文到newText
		in.readerIndex(storeIndex);
		byte[] newText = new byte[in.writerIndex()];
		in.readBytes(newText);
		//把报文重新设置回原起点
		in.readerIndex(readerIndex);
		
		//是否是GPRS登录包和心跳包
		if (isStartWith_0003(newText)) {
			readAllByte(in, out);
			return ;
		}
		
		//新来的报文是不是68开头
		if (isStartWith_68(newText)) {
			
			in.readerIndex(storeIndex);
			in.discardReadBytes();
			attr.set(in.writerIndex());	//storeIndex设置为writerIndex
			if (isEndWith_16(newText)) {
				readAllByte(in, out);
				attr.set(0);
			}
		} else {
			
			//读整段报文开头字符
			int head = 0;
			in.readBytes(head);
			in.readerIndex(readerIndex);
			logger.info("整段报文开头" + Integer.toHexString(head));
			//整段报文开头是不是68开头
			if (head == CODE_68) {
				
				//新来的报文是不是16结尾
				if (isEndWith_16(newText)) {
					readAllByte(in, out);
					attr.set(0);
				} 
				else if (!isEndWith_16(newText)) {
					attr.set(in.writerIndex());
				}
			} else {
				//无用报文，丢弃
				in.clear();
				attr.set(0);
			}
		}
		
	}
	
	private boolean isStartWith_68(byte[] text) {
		int code = text[0] & 0xFF;
		logger.info("新来报文开头" + Integer.toHexString(code));
		if (code == CODE_68) {
			return true;
		}
		return false;
	}
	
	private boolean isEndWith_16(byte[] text) {
		int code = text[text.length - 1] & 0xFF;
		logger.info("新来报文结尾" + Integer.toHexString(code));
		if (code == CODE_16) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是不是GPRS模块发过来的报文
	 * @param text
	 * @return
	 */
	private boolean isStartWith_0003(byte[] text) {
		/*
		 * 登录包报文示范，共37个字节
		 * 002500010000333432314c513130303056312e383136303430313133303030303030303030
		 * 心跳包报文示范，共10个字节
		 * 000a0003000133343231
		 */
		int code = text[text.length - 1] & 0xFF;
		if (code == CODE_00 &&
				(text.length == GPRSLoginPackageLength || text.length == GPRSOnlinePackageLegnth)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 读取ByteBuf中所有字节，并清空
	 * @param in
	 * @param out
	 */
	private void readAllByte(ByteBuf in, List<Object> out) {
		byte[] allText = new byte[in.readableBytes()];
		in.readBytes(allText);
		out.add(allText);
		in.clear();
	}

}
