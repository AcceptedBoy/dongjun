package com.gdut.dongjun.core.server.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.core.ElectronicCtxStore;
import com.gdut.dongjun.core.handler.ChannelInfo;
import com.gdut.dongjun.core.initializer.ServerInitializer;
import com.gdut.dongjun.core.message.ChannelResendMessage;
import com.gdut.dongjun.core.message.ChannelSendMessage;
import com.gdut.dongjun.core.message.impl.DLT645_97MessageCreator;
import com.gdut.dongjun.core.server.NetServer;
import com.gdut.dongjun.util.TemperatureDeviceCommandUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * 这里有发送报文的逻辑，暂时发送报文只考虑到给电能表使用，以后有其他设备就改改逻辑吧。
 * @author Gordan_Deng
 * @date 2017年5月31日
 */
@Service("TemperatureServer")
public class TemperatureServer extends NetServer {
	
	private static final int BYTE = 2;
	private static final Map<String, ChannelResendMessage> msgMap = 
			new HashMap<String, ChannelResendMessage>();
	private static Date currentMsgDate;

	@Autowired
	private ElectronicCtxStore elecStore;
	@Autowired
	private DLT645_97MessageCreator elecMessageCreator;
	@Resource(name = "TemperatureServerInitializer")
	private ServerInitializer initializer;
	@Resource(name = "TemperatureServerInitializer")
	public void setInitializer(ServerInitializer initializer) {
		super.initializer = initializer;
		super.hitchEventBreak = 210 * 1000;
//		super.hitchEventBreak = 30 * 60 * 1000;
		// super.cvReadBreak = 30 * 1000;//设置较短的读取间隔
	}

	private static final Logger logger = Logger.getLogger(TemperatureServer.class);
	
	/**
	 * 设置报警事件监听，每30分钟发送一次总召，因为新的协约不要求主动发总召报文，故删去
	 * @throws  
	 */
	@Override
	protected void hitchEventSpy() {
		//更新报文时间
		currentMsgDate = new Date();
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
			for (ChannelHandlerContext c : info.getCtxList()) {
				send.getChannel().add(c.channel());
			}
			Stack<String> stack = new Stack<String>();
			stack.addAll(msgList);
			send.setAllMessage(stack);
			senderList.add(send);
			textNum = (textNum < msgList.size()) ? msgList.size() : textNum;
			//添加报文缓存，用于重发
			addMsgCache(address, send);
		}
		//发送报文
		sendMessageInternal(senderList, textNum);
		//睡眠1分30秒后进行未确认报文重发
		try {
			Thread.sleep(90 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("开始重发未确认报文");
		//复用senderList
		senderList.clear();
		textNum = 0;
		for (Entry<String, ChannelResendMessage> entry : msgMap.entrySet()) {
			ChannelSendMessage send = new ChannelSendMessage();
			Stack<String> stack = new Stack<String>();
			for (Entry<String, String> msgEntry : entry.getValue().getMsgs().entrySet()) {
				stack.add(msgEntry.getValue());
			}
			send.setAllMessage(stack);
			send.setChannel(entry.getValue().getChannels());
			senderList.add(send);
			textNum = (textNum < stack.size()) ? stack.size() : textNum;
		}
		//发送报文
		sendMessageInternal(senderList, textNum);
		//接下来该线程会睡眠3分30秒，时间设置在hitchEventBreak
	}

	@Override
	protected void timedCVReadTask() {

	}
	
	/**
	 * 测试方法
	 * @param m
	 */
	public void sendMessage(String m) {
		List<ChannelInfo> infoList = elecStore.getInstance();
		for (ChannelInfo info : infoList) {
			logger.info("发送测试报文：" + m);
			for (ChannelHandlerContext c : info.getCtxList()) {
				c.channel().writeAndFlush(m);
			}
		}
	}
	
	/**
	 * 添加报文缓存，用于发未收到回复的报文
	 * @param address
	 * @param msg
	 */
	private void addMsgCache(String address, ChannelSendMessage sendMsg) {
		ChannelResendMessage resendMessageFrame;
		if (!msgMap.containsKey(address)) {
			resendMessageFrame = new ChannelResendMessage();
			msgMap.put(address, resendMessageFrame);
		} else {
			resendMessageFrame = msgMap.get(address);
		}
		resendMessageFrame.getChannels().clear();
		resendMessageFrame.getChannels().addAll(sendMsg.getChannel());
		Map<String, String> map = resendMessageFrame.getMsgs();
		map.clear();
		for (String m : sendMsg.getAllMessage()) {
			String register = m.substring(m.length() - 8, m.length() - 4);
			map.put(register, m);
		}
	}
	
	/**
	 * 发送报文
	 * @param senderList
	 * @param textNum
	 */
	private void sendMessageInternal(List<ChannelSendMessage> senderList, int textNum) {
		//发送报文
		for (int i = 0; i < textNum; i++) {
//			long sendNano = System.nanoTime();
			for (ChannelSendMessage csm : senderList) {
				Stack<String> stack = csm.getAllMessage();
				if (0 == stack.size()) {
					continue;
				}
				String msg = stack.pop();
				for (Channel c : csm.getChannel()) {
					logger.info("发送读数据报文：" + msg);
					c.writeAndFlush(msg);
				}
			}
			//对于一个Channel来说，相邻两个报文发送的间隔近乎2s
			try {
//				Thread.sleep(2 * 1000 - (System.nanoTime() - sendNano) / 1000);
				Thread.sleep(2 * 1000);
			} catch (InterruptedException e) {
				logger.info("发报文线程被中断!!！");
			}
		}
	}
	
	/**
	 * 确认报文到达，并清除缓存
	 * @param address
	 * @param register
	 */
	public static void confirmMsgArrived(String address, String register) {
		msgMap.get(address).getMsgs().remove(register);
	}
	
	/**
	 * 设备下线清除缓存
	 * @param address
	 */
	public static void deviceOffline(String address) {
		msgMap.remove(address);
	}
	
	/**
	 * 获取报文时间
	 * @param address
	 * @return
	 */
	public static Date getMsgDate(String address) {
		return currentMsgDate;
	}
	
}
