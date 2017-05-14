package com.gdut.dongjun.core.handler.msg_decoder;

import java.util.List;

import org.apache.log4j.Logger;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.handler.ChannelHandlerManager;
import com.gdut.dongjun.core.handler.ChannelInfo;
import com.gdut.dongjun.util.CharUtils;
import com.gdut.dongjun.util.TemperatureDeviceCommandUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public abstract class AbstractDataReceiver extends ChannelInboundHandlerAdapter {
	
	private Integer moduleTpye;
	protected Logger logger;
	protected CtxStore ctxStore;

	public AbstractDataReceiver(Integer moduleTpye, Logger logger) {
		this.moduleTpye = moduleTpye;
		this.logger = logger;
	}

	/**
	 * 读取数据的入口
	 * 先验证是不是属于自己的报文，如果不是就抛给下一个Handler，如果是就先验证报文合法性，再读取报文信息。
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		List<Object> list = (List<Object>) msg;
		int type = (int) list.get(0);
		if (!(this.moduleTpye == type)) {
			ctx.fireChannelRead(msg);
			return;
		}
		String m = (String) list.get(1);
		char[] data = CharUtils.removeSpace(m.toCharArray());
		if (check(ctx, data)) {
			channelReadInternal(ctx, data);
		} else {
			logger.info("验证失败：" + m);
		}
	}

	/**
	 * 抽取设备地址，转换成十进制地址，与数据库中的地址进行匹配，如果相同就给ChannelInfo设置Ctx和地址
	 * @param ctx
	 * @param data
	 * @return
	 */
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
		ChannelHandlerManager.addCtx(info.getMonitorId(), ctx);
		return info.getModuleId();
	}

	/**
	 * 读取数据
	 * 
	 * @param ctx
	 * @param data
	 */
	protected abstract void channelReadInternal(ChannelHandlerContext ctx, char[] data);

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
}
