package com.gdut.dongjun.core.handler.msg_decoder;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.core.HitchConst;
import com.gdut.dongjun.util.CharUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 识别报文种类
 * @author Gordan_Deng
 * @date 2017年5月5日
 */
@Component
public class ModuleCheckDataReceiver extends ChannelInboundHandlerAdapter {
	
	private static final char[] CODE_68 = new char[] { '6', '8' };
	private static final char[] CODE_00 = new char[] { '0', '0' };
	private static final int BYTE = 2;
	private static final int ADDRESS_LENGTH_TEMPERATURE = BYTE * 4;
	private static final int ADDRESS_LENGTH_ELECTRONIC = BYTE * 3;
	
	private static final Logger logger = Logger.getLogger(ModuleCheckDataReceiver.class);
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String data = (String) msg;
		Integer module = checkModule(data);
		if (0 == module) {
			logger.info("无法判断类别的报文：" + data);
			return ;
		}
		List<Object> array = new ArrayList<Object>();
		array.add(module);
		array.add(msg);
		ctx.fireChannelRead(array);
	}

	private Integer checkModule(String msg) {
		char[] data = CharUtils.removeSpace(msg.toCharArray());
		if (CharUtils.startWith(data, CODE_00)) {
			return HitchConst.MODULE_GPRS;
		}
		if (CharUtils.equals(data, BYTE + ADDRESS_LENGTH_TEMPERATURE, BYTE, CODE_68)) {
			return HitchConst.MODULE_TEMPERATURE;
		}
		else if (CharUtils.equals(data, BYTE + ADDRESS_LENGTH_ELECTRONIC, BYTE, CODE_68)) {
			return HitchConst.MODULE_ELECTRICITY;
		}
		else {
			return 0;
		}
	}

}
