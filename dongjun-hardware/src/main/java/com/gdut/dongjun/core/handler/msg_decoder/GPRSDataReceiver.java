package com.gdut.dongjun.core.handler.msg_decoder;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.GPRSCtxStore;
import com.gdut.dongjun.core.HitchConst;
import com.gdut.dongjun.service.GPRSModuleService;
import com.gdut.dongjun.util.CharUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
@Component
public class GPRSDataReceiver extends ChannelInboundHandlerAdapter {

	private static final char[] CODE_00 = new char[] { '0', '0' };
	private static final char[] CODE_01 = new char[] { '0', '1' };
	private static final char[] CODE_03 = new char[] { '0', '3' };
	
	private static final String ATTRIBUTE_GPRS_ADDRESS = "GPRS_ADDRESS";
	
	private final Logger logger = Logger.getLogger(GPRSDataReceiver.class);
	
	
	@Autowired
	private GPRSModuleService gprsService;

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		String address = (String)CtxStore.removeCtxAttribute(ctx, ATTRIBUTE_GPRS_ADDRESS);
		logger.info("GPRS" + address + "设备失去联络");
		GPRSCtxStore.removeGPRS(address);
		super.channelInactive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		List<Object> list = (List<Object>) msg;
		Integer num = (Integer)list.get(0);
		if (!(HitchConst.MODULE_GPRS == num)) {
			//如果GPRS没有注册，不进行解析
			if (check(ctx)) {
				ctx.fireChannelRead(msg);
				return ;
			}
		}
		String message = (String)list.get(1);
		char[] data = message.toCharArray();
		handleIdenCode(ctx, data);
	}

	/**
	 * 若GPRS模块注册不成功，报文不通过
	 * @param ctx
	 * @return
	 */
	private boolean check(ChannelHandlerContext ctx) {
		String address = (String)CtxStore.getCtxAttribute(ctx, ATTRIBUTE_GPRS_ADDRESS);
		if ((null == address || "".equals(address))
				|| !GPRSCtxStore.isGPRSAlive(address)) {
			logger.info("该GPRS尚未注册");
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 注册GPRS模块
	 * @param ctx
	 * @param data
	 */
	private void handleIdenCode(ChannelHandlerContext ctx, char[] data) {
		String gprsAddress = null;
		// 登录和心跳包
		if (CharUtils.startWith(data, CODE_00)
				&& (CharUtils.equals(data, 6, 8, CODE_01) || CharUtils.equals(data, 6, 8, CODE_03))) {
			char[] gprsNumber = CharUtils.subChars(data, 12, 8);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i <= 6; i += 2) {
				//示范 30 30 30 31，表示0001地址
				if (sb.length() == 0 && gprsNumber[i + 1] == '0') {
					continue;
				}
				sb.append(gprsNumber[i + 1]);
			}
			//去除开头的0
			gprsAddress = sb.toString();
			
			//判断GPRS是否已在网站上注册
			String gprsId = gprsService.isGPRSAvailable(gprsAddress);
			if (null != gprsId) {
				CtxStore.setCtxAttribute(ctx, ATTRIBUTE_GPRS_ADDRESS, gprsAddress);
				//更新TemperatureCtxStore的GPRSList
				GPRSCtxStore.addGPRS(gprsAddress);
				if (CharUtils.equals(data, 6, 8, CODE_01)) {
					//GPRS模块登录
					logger.info(gprsAddress + " GPRS模块登录成功");
				} else if (CharUtils.equals(data, 6, 8, CODE_03)) {
					logger.info(gprsAddress + " GPRS模块在线");
				}
			} else {
				//如果网站上没注册gprs，否决此报文
				logger.info("未注册GPRS模块地址" + gprsAddress);
			}
		}
	}

}
