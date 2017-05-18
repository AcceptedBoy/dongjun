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
 * 解决报文拆包 注意，未处理前的byte[]里面是一个字节代表一个16进制数字 通过此
 * {@code HexString_BytesUtil.bytesToHexString(byte[] src)}
 * 处理后的byte数组是2个byte代表一个16进制数字
 * 
 * 处理流程： ①如果接下来的报文是68开头，readerIndex移动到storeindex，然后清除已读数据
 * ②如果不是68开头，且16结尾，则从readerIndex读取到writerIndex，然后清零
 * ③如果不是68开头，且不是16结尾，则readerindex不动
 * 
 * @author Gordan_Deng
 * @date 2017年3月24日
 */
public class SeparatedTextDecoder extends ByteToMessageDecoder {

	// 字节68转为10进制之后的数字
	private static final int CODE_68 = 16 * 6 + 8;
	private static final int CODE_16 = 1 * 16 + 6;
	private static final int CODE_00 = 0;
	private static final int CODE_25 = 16 * 2 + 5;
	private static final int CODE_0A = 10;
	private static final int CODE_01 = 1;
	private static final int CODE_03 = 3;
	private static final int CODE_EB = 16 * 14 + 11;

	// gprs模块登录包字节长度
	private static final int GPRSLoginPackageLength = 37;
	// gprs模块心跳包字节长度
	private static final int GPRSOnlinePackageLegnth = 10;

	private static final Logger logger = Logger.getLogger(SeparatedTextDecoder.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		// storeIndex是上一次未完整的报文的终点
		AttributeKey<Integer> key = AttributeKey.valueOf("storeIndex");
		Attribute<Integer> attr = ctx.attr(key);
		if (null == attr.get()) {
			attr.set(in.readerIndex());
		}

		Integer storeIndex = attr.get();

		if (logger.isDebugEnabled()) {
			logger.debug("为处理前in的readerIndex" + in.readerIndex() + "    writerIndex" + in.writerIndex()
					+ "    storeIndex" + storeIndex);
		}

		Integer readerIndex = in.readerIndex();
		// 移到新来的报文的起点，并读取新报文到newText
		in.readerIndex(storeIndex);
		// 有一个bug，当报文发生拆包，在这个类合并成一条报文，顺利到达{@code
		// TemperatureReceiver}之后，这里会接收到一个decode调用，但是没有报文发过来
		// 暂时为了解决这个问题，设置如果writerIndex和storeIndex一样，就不执行下面流程。
		if (in.writerIndex() - storeIndex == 0) {
			return;
		}
		byte[] newText = new byte[in.writerIndex() - storeIndex];
		in.readBytes(newText);
		// 把报文重新设置回原起点
		in.readerIndex(readerIndex);

		// 是否是GPRS登录包和心跳包
		if (isGPRSMessage(newText)) {
			in.readerIndex(storeIndex);
			readAllByte(in, out);
			attr.set(0);

			if (logger.isDebugEnabled()) {
				logger.debug("为处理后in的readerIndex" + in.readerIndex() + "    writerIndex" + in.writerIndex()
						+ "    storeIndex" + storeIndex);
			}
			return;
		}

		// 是否是电能表登录报文 TODO
		// if (isStartWith_EB(newText)) {
		// in.readerIndex(storeIndex);
		// readAllByte(in, out);
		// attr.set(0);
		//
		// if (logger.isDebugEnabled()) {
		// logger.debug("为处理后in的readerIndex" + in.readerIndex() + " writerIndex"
		// + in.writerIndex() + " storeIndex" + storeIndex);
		// }
		// return ;
		// }

		// 新来的报文是不是68开头
		if (isStartWith_68(newText)) {
			byte[] discardText = new byte[storeIndex - in.readerIndex()];
			in.readBytes(discardText);
			if (0 != discardText.length) {
				String str = HexString_BytesUtil.bytesToHexString(discardText);
				logger.info("解码器丢弃无效报文：" + str);
			}
			
			in.readerIndex(storeIndex);
			in.discardReadBytes();
			attr.set(in.writerIndex()); // storeIndex设置为writerIndex
			if (isEndWith_16(newText)) {
				readAllByte(in, out);
				attr.set(0);
			}
		} else {

			// 读整段报文开头字符
			byte[] headByte = new byte[1];
			in.readBytes(headByte);
			int head = (int) headByte[0];
			in.readerIndex(readerIndex);
			if (logger.isDebugEnabled()) {
				logger.debug("整段报文开头" + Integer.toHexString(head));
			}
			// 整段报文开头是不是68开头
			if (head == CODE_68) {

				// 新来的报文是不是16结尾
				if (isEndWith_16(newText)) {
					readAllByte(in, out);
					attr.set(0);
				} else if (!isEndWith_16(newText)) {
					attr.set(in.writerIndex());
				}
			} else {
				// 无用报文，丢弃
				clear(in);
				// in.clear();
				attr.set(0);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("为处理后in的readerIndex" + in.readerIndex() + "    writerIndex" + in.writerIndex()
					+ "    storeIndex" + storeIndex);
		}
	}

	private void clear(ByteBuf in) {
		byte[] allText = new byte[in.readableBytes()];
		in.readBytes(allText);
		in.clear();
		if (0 == allText.length) {
			return;
		}
		String str = HexString_BytesUtil.bytesToHexString(allText);
		logger.info("解码器丢弃无效报文：" + str);
	}

	private boolean isStartWith_68(byte[] text) {
		int code = text[0] & 0xFF;
		if (logger.isDebugEnabled()) {
			logger.debug("新来报文开头" + Integer.toHexString(code));
		}
		if (code == CODE_68) {
			return true;
		}
		return false;
	}

	private boolean isStartWith_EB(byte[] text) {
		int code = text[0] & 0xFF;
		if (CODE_EB == code) {
			return true;
		}
		return false;
	}

	private boolean isEndWith_16(byte[] text) {
		int code = text[text.length - 1] & 0xFF;
		if (logger.isDebugEnabled()) {
			logger.debug("新来报文结尾" + Integer.toHexString(code));
		}
		if (code == CODE_16) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是不是GPRS模块发过来的报文
	 * 
	 * @param text
	 * @return
	 */
	private boolean isGPRSMessage(byte[] text) {
		/*
		 * 登录包报文示范，共37个字节
		 * 002500010000333432314c513130303056312e383136303430313133303030303030303030
		 * 心跳包报文示范，共10个字节 000a0003000133343231
		 */
		if (text.length != GPRSLoginPackageLength && text.length != GPRSOnlinePackageLegnth) {
			return false;
		}
		int code = text[0] & 0xFF;
		int code1 = text[1] & 0xFF;
		int code2 = text[3] & 0xFF;
		if (code == CODE_00) {
			if ((code1 == CODE_25 && code2 == CODE_01 && text.length == GPRSLoginPackageLength)
					|| (code1 == CODE_0A && code2 == CODE_03 && text.length == GPRSOnlinePackageLegnth)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 读取ByteBuf中所有字节，并清空
	 * 
	 * @param in
	 * @param out
	 */
	private void readAllByte(ByteBuf in, List<Object> out) {
		byte[] allText = new byte[in.readableBytes()];
		in.readBytes(allText);
		out.add(HexString_BytesUtil.bytesToHexString(allText));
		in.clear();
	}

}
