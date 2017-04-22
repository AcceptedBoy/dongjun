package com.gdut.dongjun.core.server.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.core.initializer.ServerInitializer;
import com.gdut.dongjun.core.server.NetServer;
import com.gdut.dongjun.domain.DataZone;
import com.gdut.dongjun.domain.po.LowVoltageSwitch;
import com.gdut.dongjun.enums.EventVariable;
import com.gdut.dongjun.service.LowVoltageSwitchService;
import com.gdut.dongjun.util.LowVoltageDeviceCommandUtil;
import com.sun.xml.bind.v2.TODO;

/**
 * @author Sherlock-lee
 * @date 2015年11月17日 下午12:01:03
 * @see TODO
 * @since 1.0
 */
@Service("LowVoltageServer")
public class LowVoltageServer extends NetServer {

	@Resource(name = "LowVoltageServerInitializer")
	private ServerInitializer initializer;
	@Autowired
	private LowVoltageSwitchService lowVoltageSwitchService;

	@Resource(name = "LowVoltageServerInitializer")
	public void setInitializer(ServerInitializer initializer) {

		super.initializer = initializer;
	}

	/**
	 * 
	 * @Title: hitchEventSpy
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	@Override
	protected void hitchEventSpy() {

		List<LowVoltageSwitch> switchs = lowVoltageSwitchService
				.selectByParameters(null);

		if (switchs != null) {
			for (LowVoltageSwitch s : switchs) {

				if (s.getId() != null && CtxStore.isReady(s.getId())) {

					SwitchGPRS gprs = CtxStore.get(s.getId());
					String msg = LowVoltageDeviceCommandUtil.read(gprs
							.getAddress(), new DataZone(
							EventVariable.HITCH_EVENT.toString()));

					gprs.getCtx().writeAndFlush(msg);
				}
			}
		}

	}

	/**
	 * 
	 * @Title: timedCVRead
	 * @Description: 定时读取电流电压
	 * @param
	 * @return void
	 * @throws
	 */
	@Override
	protected void timedCVReadTask() {

		String msg = null;
		List<LowVoltageSwitch> switchs = lowVoltageSwitchService
				.selectByParameters(null);

		if (switchs != null) {
			for (LowVoltageSwitch s : switchs) {

				if (s.getId() != null && CtxStore.isReady(s.getId())) {

					SwitchGPRS gprs = CtxStore.get(s.getId());

					msg = LowVoltageDeviceCommandUtil.readAllPhaseVoltage(gprs
							.getAddress());

					gprs.getCtx().writeAndFlush(msg);// 读取电压

				}
			}

			for (LowVoltageSwitch s : switchs) {

				if (s.getId() != null && CtxStore.isReady(s.getId())) {

					SwitchGPRS gprs = CtxStore.get(s.getId());

					msg = LowVoltageDeviceCommandUtil.readAllPhaseCurrent(gprs
							.getAddress());

					gprs.getCtx().writeAndFlush(msg);// 读取电流
				}
			}
		}
	}
}
