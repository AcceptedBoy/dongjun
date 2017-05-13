package com.gdut.dongjun.core.server.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.core.ElectronicCtxStore;
import com.gdut.dongjun.core.handler.ChannelInfo;
import com.gdut.dongjun.core.initializer.ServerInitializer;
import com.gdut.dongjun.core.message.impl.ElectronicModuleMessageCreator;
import com.gdut.dongjun.core.server.NetServer;
import com.gdut.dongjun.util.TemperatureDeviceCommandUtil;

@Service("TemperatureServer")
public class TemperatureServer extends NetServer {
	
	private static final int BYTE = 2;

	@Autowired
	private ElectronicCtxStore elecStore;
	@Autowired
	private ElectronicModuleMessageCreator elecMessageCreator;
	@Resource(name = "TemperatureServerInitializer")
	private ServerInitializer initializer;
	@Resource(name = "TemperatureServerInitializer")
	public void setInitializer(ServerInitializer initializer) {
		super.initializer = initializer;
		super.hitchEventBreak = 30 * 60 * 1000;
		// super.cvReadBreak = 30 * 1000;//设置较短的读取间隔
	}

	private static final Logger logger = Logger.getLogger(TemperatureServer.class);
	
	/**
	 * 设置报警事件监听，每30分钟发送一次总召，因为新的协约不要求主动发总召报文，故删去
	 */
	@Override
	protected void hitchEventSpy() {
		
		List<ChannelInfo> infoList = elecStore.getInstance();
		String address;
		for (ChannelInfo info : infoList) {
			if (info.getDecimalAddress().length() != BYTE * 6) {
				int numOf0 = BYTE * 6 - info.getDecimalAddress().length();
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < numOf0; i++) {
					sb.append("a");
				}
				sb.append(TemperatureDeviceCommandUtil.reverseString(info.getDecimalAddress()));
				address = sb.toString();
			} else {
				address = TemperatureDeviceCommandUtil.reverseString(info.getDecimalAddress());
			}
			List<String> msgList = elecMessageCreator.generateTotalCall(address);
			for (String order : msgList) {
				logger.info("电能表总召命令：" + order);
				info.getCtx().writeAndFlush(order);
			}
		}
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
