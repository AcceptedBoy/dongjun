package com.gdut.dongjun.core.server.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.core.ElectronicCtxStore;
import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.core.initializer.ServerInitializer;
import com.gdut.dongjun.core.message.impl.ElectronicModuleMessageCreator;
import com.gdut.dongjun.core.server.NetServer;

@Service("ElectronicServer")
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
		List<SwitchGPRS> gprsList = ctxStore.getInstance();
		for (SwitchGPRS device : gprsList) {
			List<String> msgList = messageCreator.generateTotalCall(device.getAddress());
			for (String msg : msgList) {
				device.getCtx().writeAndFlush(msg);
			}
		}
	}

	@Override
	protected void timedCVReadTask() { }

}
