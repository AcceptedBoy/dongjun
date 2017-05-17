package com.gdut.dongjun.core.handler;

import org.apache.log4j.Logger;

import com.gdut.dongjun.core.CtxStore;

import io.netty.channel.ChannelHandlerContext;

/**
 * 协议解析策略
 * parserId为ParseStrategy的唯一标识
 * 201 为DLT645_07
 * 202 为DLT645_97
 * 301 为温度
 * @author Gordan_Deng
 * @date 2017年5月11日
 */
public abstract class ParseStrategy implements MessageParser {
	
	protected String parserId;
	protected Logger logger;
	protected CtxStore ctxStore;
	
	public ParseStrategy(String parserId, Logger logger) {
		this.logger = logger;
	}
	
	protected String getOnlineAddress(ChannelHandlerContext ctx, char[] data) {
		ChannelInfo info = ctxStore.get(ctx);

		if (null != info && null != info.getAddress()) {
			return info.getModuleId();
		}
		// 如果十进制地址和数据库中的十进制地址一样，认为是同一个设备
		String address = getAddress(data);
		String decimalAddress = getDecimalAddress(data);
		ChannelInfo channelInfo = ctxStore.getChannelInfoByDecimalAddress(decimalAddress);
		if (null != channelInfo) {
			channelInfo.setCtx(ctx);
			channelInfo.setAddress(address);
		} else {
			//TODO 此处应有报警
			logger.info("暂未初始化ChannelInfo，接收到的子模块报文地址为" + decimalAddress);
			return null;
		}
		// TODO
//		ChannelHandlerManager.addCtx(info.getMonitorId(), ctx);
		return info.getModuleId();
	}
	
	/**
	 * 验证数据合法性
	 * 
	 * @param ctx
	 * @param data
	 * @return
	 */
	protected abstract boolean check(ChannelHandlerContext ctx, char[] data);
	
	/**
	 * 抽取地址
	 * @param data
	 * @return
	 */
	protected abstract String getAddress(char[] data);
	
	/**
	 * 转换成十进制地址
	 * @param data
	 * @return
	 */
	protected abstract String getDecimalAddress(char[] data);
	
	protected abstract Object parseInternal(ChannelHandlerContext ctx, char[] data);

	public String getParserId() {
		return parserId;
	}

	public void setParserId(String parserId) {
		this.parserId = parserId;
	}

	@Override
	public Object parse(char[] data, ChannelHandlerContext ctx) {
		if (check(ctx, data)) {
			return parseInternal(ctx, data);
		}
		return null;
	}
	
}
