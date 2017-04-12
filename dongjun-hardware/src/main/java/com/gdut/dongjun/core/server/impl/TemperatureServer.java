package com.gdut.dongjun.core.server.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.core.initializer.ServerInitializer;
import com.gdut.dongjun.core.server.NetServer;

@Service("TemperatureServer")
public class TemperatureServer extends NetServer {

	private static final Logger logger = Logger.getLogger(TemperatureServer.class);
	
	@Resource(name = "TemperatureServerInitializer")
	private ServerInitializer initializer;

	@Resource(name = "TemperatureServerInitializer")
	public void setInitializer(ServerInitializer initializer) {

		super.initializer = initializer;
		super.hitchEventBreak = 30 * 60 * 1000;
		// super.cvReadBreak = 30 * 1000;//设置较短的读取间隔
	}

	/**
	 * 设置报警事件监听，每30分钟发送一次总召，因为新的协约不要求主动发总召报文，故删去
	 */
	@Override
	protected void hitchEventSpy() {
//		List<TemperatureDevice> devices = temperatureDeviceService.selectByParameters(null);
//		if (devices != null) {
//			for (TemperatureDevice device : devices) {
//				if (device.getId() != null && CtxStore.isReady(device.getId())) {
//					totalCall(device);
//				}
//			}
//		}
	}

//	public static String totalCall(TemperatureDevice device) {
//		return totalCall(device.getId());
//	}
//
//	public static String totalCall(String id) {
//		SwitchGPRS gprs = CtxStore.get(id);
//		String msg = new TemperatureDeviceCommandUtil(gprs.getAddress()).getTotalCall();
//		logger.info("总召激活地址：" + gprs.getAddress() + "---" + msg);
//		gprs.getCtx().writeAndFlush(msg);
//		return msg;
//	}

	@Override
	protected void timedCVReadTask() {

	}

}
