package com.gdut.dongjun.core.handler.msg_decoder;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.ElectronicCtxStore;
import com.gdut.dongjun.core.GPRSCtxStore;
import com.gdut.dongjun.core.HitchConst;
import com.gdut.dongjun.core.TemperatureCtxStore;
import com.gdut.dongjun.core.handler.ChannelInfo;
import com.gdut.dongjun.core.handler.GPRSParseStrategy;
import com.gdut.dongjun.domain.po.DataMonitorSubmodule;
import com.gdut.dongjun.domain.po.ElectronicModule;
import com.gdut.dongjun.domain.po.TemperatureModule;
import com.gdut.dongjun.service.DataMonitorSubmoduleService;
import com.gdut.dongjun.service.ElectronicModuleService;
import com.gdut.dongjun.service.GPRSModuleService;
import com.gdut.dongjun.service.TemperatureModuleService;
import com.gdut.dongjun.util.CharUtils;
import com.gdut.dongjun.util.MyBatisMapUtil;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;

/**
 * 负责GPRS登录以及初始化ChannelInfo
 * @author Gordan_Deng
 * @date 2017年5月11日
 */
@Sharable
@Component
public class GPRSDataReceiver extends AbstractDataReceiver implements InitializingBean {
	
	@Autowired
	private GPRSParseStrategy strategy;

	public GPRSDataReceiver() {
		super(HitchConst.MODULE_GPRS, Logger.getLogger(GPRSDataReceiver.class));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.setParseStrategy(strategy);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		String address = (String)strategy.clearCache(ctx);
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
			}
			return ;
		}
		String message = (String) list.get(1);
		char[] data = CharUtils.removeSpace(message.toCharArray());
		strategy.parse(data, ctx);
	}

	/**
	 * 若GPRS模块注册不成功，报文不通过
	 * @param ctx
	 * @return
	 */
	private boolean check(ChannelHandlerContext ctx) {
		return strategy.isEnabledPassed(ctx);
	}


	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		String address = (String)strategy.clearCache(ctx);
		logger.info("GPRS" + address + "设备去除解析模块");
		GPRSCtxStore.removeGPRS(address);
	}

}
