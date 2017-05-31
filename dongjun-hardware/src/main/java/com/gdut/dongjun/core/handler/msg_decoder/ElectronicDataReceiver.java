package com.gdut.dongjun.core.handler.msg_decoder;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.core.ElectronicCtxStore;
import com.gdut.dongjun.core.HitchConst;
import com.gdut.dongjun.core.handler.ChannelInfo;
import com.gdut.dongjun.core.handler.DLT645_97ParseStrategy;
import com.gdut.dongjun.core.server.impl.TemperatureServer;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;

/**
 * <p>
 * 按照协议分类 68/3100310068C90000000A1C026000000400551618041502010000 请求帧的一种情况 68 地址
 * 68 控制码 数据域长度 数据标识 校验和 16 1 6 1 1 1 4 1 1 读数据的一种情况 68 地址 68 控制码 数据域长度 数据标识
 * N1...Nm 校验和 16 1 6 1 1 1 4 N 1 1 地址6个字节，每个字节两个BCD码，总共12个十进制数
 * </p>
 * 
 * @author Gordan_Deng
 * @date 2017年4月12日
 */
@Service
@Sharable
public class ElectronicDataReceiver extends AbstractDataReceiver implements InitializingBean {

	@Autowired
	private DLT645_97ParseStrategy strategy;
	@Autowired
	private ElectronicCtxStore ctxStore;
	
	// TODO 考虑下要不要将ElectronicModule直接塞到Attribute里面
	public ElectronicDataReceiver() {
		super(HitchConst.MODULE_ELECTRICITY, Logger.getLogger(ElectronicDataReceiver.class));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.setParseStrategy(strategy);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ChannelInfo info = ctxStore.get(ctx);
		if (null != info && null != info.getAddress()) {
			//清除报文缓存
			TemperatureServer.deviceOffline(info.getAddress());
		}
		strategy.clearCache(ctx);
		ctxStore.remove(ctx);
		super.channelInactive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error(cause.getMessage());
		cause.printStackTrace();
		super.exceptionCaught(ctx, cause);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
//		strategy.clearCache(ctx);
//		ctxStore.remove(ctx);
//		//清除报文缓存
//		TemperatureServer.deviceOffline(ctxStore.get(ctx).getAddress());
		super.handlerRemoved(ctx);
	}
	
}
