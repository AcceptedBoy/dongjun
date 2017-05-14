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
			//有address说明设备已经和后台连接了，直接取address。否则通过十进制的地址转换得到address
			if (null != info.getAddress()) {
				address = info.getAddress();
			} else {
				if (info.getDecimalAddress().length() != BYTE * 6) {
					//如果地址不足偶数位，首位补0
					String a = info.getDecimalAddress();
					if (!(info.getDecimalAddress().length() % 2 == 0)) {
						a = "0" + info.getDecimalAddress();
					}					
					StringBuilder sb = new StringBuilder();
					sb.append(TemperatureDeviceCommandUtil.reverseString(a));
					int numOf0 = BYTE * 6 - a.length();
					for (int i = 0; i < numOf0; i++) {
						sb.append("a");
					}
					address = sb.toString();
				} else {
					address = TemperatureDeviceCommandUtil.reverseString(info.getDecimalAddress());
				}
			}
			List<String> msgList = elecMessageCreator.generateTotalCall(address);
			for (String order : msgList) {
				logger.info("发送电能表总召命令：" + order);
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
