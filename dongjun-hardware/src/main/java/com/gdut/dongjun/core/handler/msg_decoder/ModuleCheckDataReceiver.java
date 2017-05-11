package com.gdut.dongjun.core.handler.msg_decoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.core.HitchConst;
import com.gdut.dongjun.core.handler.ChannelHandlerManager;
import com.gdut.dongjun.util.CharUtils;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 识别报文种类
 * pipeline里面的Handler的名字都是TemperatureDataReceiver#0这样子的
 * #0表明这个是TemperatureDataReveiver第一个出现在pipeline里面
 * @author Gordan_Deng
 * @date 2017年5月5日
 */
@Sharable
@Component
public class ModuleCheckDataReceiver extends ChannelInboundHandlerAdapter {
	
	private static final char[] CODE_68 = new char[] { '6', '8' };
	private static final char[] CODE_00 = new char[] { '0', '0' };
	private static final char[] CODE_EB = new char[] { 'E', 'B' };
	private static final char[] CODE_eb = new char[] { 'e', 'b' };
	private static final int BYTE = 2;
	private static final int LENGTH_LENGTH_TEMPERATURE = BYTE * 2;
	private static final int ADDRESS_LENGTH_ELECTRONIC = BYTE * 3;
	private static int counter = 0;
	
	@Autowired
	private TestDataReceiver testDataReceiver;
	@Autowired
	private TemperatureDataReceiver temReceiver;
	
	private static final Logger logger = Logger.getLogger(ModuleCheckDataReceiver.class);
	
	private static Map<Class<?>, String> HANDLER_MAP = new HashMap<Class<?>, String>();
	
	static {
		HANDLER_MAP.put(TemperatureDataReceiver.class, "TemperatureDataReceiver#0");
		HANDLER_MAP.put(ModuleCheckDataReceiver.class, "ModuleCheckDataReceiver#0");
		HANDLER_MAP.put(ElectronicDataReceiver.class, "ElectronicDataReceiver#0");
		HANDLER_MAP.put(TestDataReceiver.class, "TestDataReceiver#0");
		HANDLER_MAP.put(GPRSDataReceiver.class, "GPRSDataReceiver#0");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (counter++ == 2) {
			logger.info("开始改变Handler");
			ctx.channel().pipeline().remove(HANDLER_MAP.get(TemperatureDataReceiver.class));
			ctx.channel().pipeline().remove(HANDLER_MAP.get(ElectronicDataReceiver.class));
			ctx.channel().pipeline().addLast(testDataReceiver);
		}
		if (counter == 4) {
			ctx.channel().pipeline().remove(HANDLER_MAP.get(TestDataReceiver.class));
			ctx.channel().pipeline().addLast(temReceiver);
		}
		String data = (String) msg;
		Integer module = checkModule(data);
		if (0 == module) {
			logger.info("无法判断类别的报文：" + data);
			return ;
		}
		List<Object> array = new ArrayList<Object>();
		array.add(module);
		array.add(msg);
		logger.info("接收到类别" + module + "的报文：" + data);
		ctx.fireChannelRead(array);
	}

	private Integer checkModule(String msg) {
		char[] data = CharUtils.removeSpace(msg.toCharArray());
		if (CharUtils.startWith(data, CODE_00)) {
			return HitchConst.MODULE_GPRS;
		}
//		//EB开头的报文现在进入电能表解析模块
//		if (CharUtils.equals(data, 0, 2, CODE_EB) || CharUtils.equals(data, 0, 2, CODE_eb)) {
//			return HitchConst.MODULE_ELECTRICITY;
//		}
		//温度模块两个68之间有两个字节的长度信息
		if (CharUtils.equals(data, BYTE + LENGTH_LENGTH_TEMPERATURE, BYTE + LENGTH_LENGTH_TEMPERATURE + BYTE, CODE_68)) {
			return HitchConst.MODULE_TEMPERATURE;
		}
		//电能表模块两个68之间有三个字节的地址信息
		else if (CharUtils.equals(data, BYTE + ADDRESS_LENGTH_ELECTRONIC, BYTE + ADDRESS_LENGTH_ELECTRONIC + BYTE, CODE_68)) {
			return HitchConst.MODULE_ELECTRICITY;
		}
		else {
			return 0;
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ChannelHandlerManager.removeCtx(ctx);
		ctx.fireChannelInactive();
	}

}
