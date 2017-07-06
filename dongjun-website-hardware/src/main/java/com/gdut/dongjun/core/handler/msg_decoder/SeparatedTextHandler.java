package com.gdut.dongjun.core.handler.msg_decoder;

import org.springframework.stereotype.Service;

import com.gdut.dongjun.util.CharUtils;
import com.gdut.dongjun.util.StringCommonUtil;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 处理报文耦合情况
 * 
 * @author Gordan_Deng
 * @date 2017年6月27日
 */
@Service
@Sharable
public class SeparatedTextHandler extends ChannelInboundHandlerAdapter {
	
	private static final char[] CODE_68 = new char[] { '6', '8' }; // 68
	private static final char[] CODE_EB90 = new char[] { 'e', 'b', '9', '0', 'e', 'b', '9', '0', 'e', 'b', '9', '0' };
	private static final char[] CODE_16 = new char[] { '1', '6' }; // 16

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String message = (String) msg;
		char[] data = CharUtils.removeSpace(message.toCharArray());
		int begin = 0;
		while (true) {
			// 获取报文分割点
			int pos = getSeparatedPoint(data, begin);
			// pos为-1报文异常
			if (pos == -1) {
				break;
			}
			// 分割出独立报文段
			char[] dataInfo = CharUtils.subChars(data, begin, pos);
			super.channelRead(ctx, dataInfo);
			//如果分割点是整段报文的终点，结束
			if (pos == data.length) {
				break;
			}
			begin = pos;
		}
	}

	/**
	 * 得到报文分割点
	 * @param data
	 * @param begin
	 * @return
	 */
	private int getSeparatedPoint(char[] data, int begin) {
		int index = StringCommonUtil.getFirstIndexOfEndTag(data, begin, "16");
		if (index != -1) {
			if (isSeparatedPoint(data, index)) {
				return index;
			}
		}
		return -1;
	}

	/**
	 * 检查index是否是报文分割点
	 * @param data
	 * @param index
	 * @return
	 */
	private boolean isSeparatedPoint(char[] data, int index) {
		// 如果index是data的终点，返回true
		if (index == data.length) {
			return true;
		}
		// 如果16后面是68xx68，返回true
		else if (CharUtils.equals(CharUtils.subChars(data, index, 2), CODE_68)
				&& CharUtils.equals(CharUtils.subChars(data, index + 6, 2), CODE_68)) {
			return true;
		}
		// 如果16后面是eb90eb90eb90xxxx16，返回true
		else if (CharUtils.equals(CharUtils.subChars(data, index, 12), CODE_EB90)
				&& CharUtils.equals(CharUtils.subChars(data, index + 16, 2), CODE_16)) {
			return true;
		}
		return false;
	}
	
}
