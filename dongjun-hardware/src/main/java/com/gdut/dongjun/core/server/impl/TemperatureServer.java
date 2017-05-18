package com.gdut.dongjun.core.server.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.core.ElectronicCtxStore;
import com.gdut.dongjun.core.handler.ChannelInfo;
import com.gdut.dongjun.core.initializer.ServerInitializer;
import com.gdut.dongjun.core.message.ChannelSendMessage;
import com.gdut.dongjun.core.message.impl.DLT645_97MessageCreator;
import com.gdut.dongjun.core.server.NetServer;
import com.gdut.dongjun.util.TemperatureDeviceCommandUtil;

@Service("TemperatureServer")
public class TemperatureServer extends NetServer {
	
	private static final int BYTE = 2;

	@Autowired
	private ElectronicCtxStore elecStore;
	@Autowired
	private DLT645_97MessageCreator elecMessageCreator;
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
	 * @throws  
	 */
	@Override
	protected void hitchEventSpy() {
		List<ChannelInfo> infoList = elecStore.getInstance();
		List<ChannelSendMessage> senderList = new ArrayList<ChannelSendMessage>();
		int textNum = 0;
		//得到ChanelSendMessage
		for (ChannelInfo info : infoList) {
			String address = null;
			//得到地址
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
						sb.append("0");
					}
					address = sb.toString();
				} else {
					address = TemperatureDeviceCommandUtil.reverseString(info.getDecimalAddress());
				}
			}
			//得到报文
			List<String> msgList = elecMessageCreator.generateTotalCall(address);
			ChannelSendMessage send = new ChannelSendMessage();
			send.setChannel(info.getCtx().channel());
			Stack<String> stack = new Stack<String>();
			stack.addAll(msgList);
			send.setAllMessage(stack);
			senderList.add(send);
			textNum = (textNum < msgList.size()) ? msgList.size() : textNum;
		}
		//发送报文
		for (int i = 0; i < textNum; i++) {
			long sendNano = System.nanoTime();
			for (ChannelSendMessage csm : senderList) {
				Stack<String> stack = csm.getAllMessage();
				if (0 == stack.size()) {
					continue;
				}
				String msg = stack.pop();
				csm.getChannel().writeAndFlush(msg);
			}
			//对于一个Channel来说，相邻两个报文发送的间隔近乎2s
			try {
				Thread.sleep(2 * 1000 - (System.nanoTime() - sendNano) / 1000);
			} catch (InterruptedException e) {
				logger.info("发报文线程被中断!!！");
			}
		}
		
//		List<ChannelInfo> infoList = elecStore.getInstance();
//		String address;
//		for (ChannelInfo info : infoList) {
//			//有address说明设备已经和后台连接了，直接取address。否则通过十进制的地址转换得到address
//			if (null != info.getAddress()) {
//				address = info.getAddress();
//			} else {
//				if (info.getDecimalAddress().length() != BYTE * 6) {
//					//如果地址不足偶数位，首位补0
//					String a = info.getDecimalAddress();
//					if (!(info.getDecimalAddress().length() % 2 == 0)) {
//						a = "0" + info.getDecimalAddress();
//					}					
//					StringBuilder sb = new StringBuilder();
//					sb.append(TemperatureDeviceCommandUtil.reverseString(a));
//					int numOf0 = BYTE * 6 - a.length();
//					for (int i = 0; i < numOf0; i++) {
//						sb.append("0");
//					}
//					address = sb.toString();
//				} else {
//					address = TemperatureDeviceCommandUtil.reverseString(info.getDecimalAddress());
//				}
//			}
//			List<String> msgList = elecMessageCreator.generateTotalCall(address);
//			for (String order : msgList) {
//				logger.info("发送电能表总召命令：" + order);
//				info.getCtx().channel().writeAndFlush(order);
//				try {
//					Thread.sleep(1 * 1000);
//				} catch (InterruptedException e) {
//					logger.info("发报文线程出错！");
//					e.printStackTrace();
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
	
	public void sendMessage(String m) {
		List<ChannelInfo> infoList = elecStore.getInstance();
		for (ChannelInfo info : infoList) {
			logger.info("发送测试报文：" + m);
			info.getCtx().channel().writeAndFlush(m);
		}
	}
}
