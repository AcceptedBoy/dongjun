package com.gdut.dongjun.core.server.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.gdut.dongjun.core.ElectronicCtxStore;
import com.gdut.dongjun.core.handler.ChannelInfo;
import com.gdut.dongjun.core.initializer.ServerInitializer;
import com.gdut.dongjun.core.message.impl.ElectronicModuleMessageCreator;
import com.gdut.dongjun.core.server.NetServer;

//@Service("ElectronicServer")
public class ElectronicServer extends NetServer {

	private ServerInitializer initializer;
	@Autowired
	private ElectronicCtxStore ctxStore;
	@Autowired
	private ElectronicModuleMessageCreator messageCreator;

	@Resource(name = "ElectronicServerInitializer")
	public void setInitializer(ServerInitializer initializer) {

		super.initializer = initializer;
		super.hitchEventBreak = 30 * 60 * 1000;
		// super.cvReadBreak = 30 * 1000;//设置较短的读取间隔
	}

	/**
	 * 设置报警事件监听，每30分钟发送一次总召
	 */
	@Override
	protected void hitchEventSpy() {
		List<ChannelInfo> infoList = ctxStore.getInstance();
		for (ChannelInfo info : infoList) {
			List<String> msgList = messageCreator.generateTotalCall(info.getAddress());
			for (String order : msgList) {
				info.getCtx().writeAndFlush(order);
			}
		}
	}

	@Override
	protected void timedCVReadTask() { }

}
