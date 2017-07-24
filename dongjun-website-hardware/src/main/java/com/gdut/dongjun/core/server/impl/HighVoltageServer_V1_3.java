package com.gdut.dongjun.core.server.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.HighVoltageCtxStore;
import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.core.initializer.ServerInitializer;
import com.gdut.dongjun.core.server.NetServer;
import com.gdut.dongjun.domain.HighVoltageStatus;
import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.enums.HighCommandControlCode;
import com.gdut.dongjun.service.HighVoltageSwitchService;
import com.gdut.dongjun.util.HighVoltageDeviceCommandUtil;


/**
 * @author Sherlock-lee
 * @date 2015年11月17日 下午12:01:03
 * @see
 * @since 1.0
 */
@Service("HighVoltageServer_V1_3")
public class HighVoltageServer_V1_3 extends NetServer implements InitializingBean {

	@Resource(name = "HighVoltageServerInitializer_V1_3")
	private ServerInitializer initializer;
	@Autowired
	private HighVoltageSwitchService lowVoltageSwitchService;
	@Autowired
	private HighVoltageCtxStore ctxStore;

	private static final Logger logger = Logger
			.getLogger(HighVoltageServer_V1_3.class);

	@Resource(name = "HighVoltageServerInitializer_V1_3")
	public void setInitializer(ServerInitializer initializer) {

		super.initializer = initializer;
		super.hitchEventBreak = 30 * 60 * 1000;
		// super.cvReadBreak = 30 * 1000;//设置较短的读取间隔
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		final HighVoltageDeviceCommandUtil ut = new HighVoltageDeviceCommandUtil();
		/*
		 * 心跳线程，每分钟向所有设备发心跳报文，对已连接未获取状态的设备进行总召
		 */
		new Thread() {
			@Override
			public void run() {
				while (true) {
					logger.info("主站发送心跳报文");
					for (SwitchGPRS gprs : CtxStore.getInstance()) {
						if (null != gprs.getAddress()) {
							String msg = new HighVoltageDeviceCommandUtil().confirmHeart(gprs.getAddress());
							gprs.getCtx().writeAndFlush(msg);
							if (null != gprs.getId()) {
								HighVoltageStatus s = ctxStore.getStatusbyId(gprs.getId());
								if (null == s || null == s.getStatus()) {
									//	设备已连接但未获取状态，进行全域总召
									gprs.getCtx().writeAndFlush(ut.anonTotalCall());
								}
							}
						}
					}
					try {
						Thread.sleep(1000 * 60 * 5);
					} catch (InterruptedException e) {
						logger.info("心跳线程终止");
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	@Override
	protected void hitchEventSpy() {

		List<HighVoltageSwitch> switchs = lowVoltageSwitchService
				.selectByParameters(null);

		if (switchs != null) {
			for (HighVoltageSwitch s : switchs) {
				if (s.getId() != null) {
					if (CtxStore.isReady(s.getId())) {
						totalCall(s);
					}
				}
			}
		}

	}
	
	/**
	 * 这个方法可以用于当有新的高压开关在线时，进行一次总召.
	 * @return 返回发送总召命令的报文
	 */
	public static String totalCall(HighVoltageSwitch s) {
		
		return totalCall(s.getId());
	}
	
	public static String totalCall(String id) {
		
		SwitchGPRS gprs = CtxStore.get(id);
		String msg = new HighVoltageDeviceCommandUtil()
				.readVoltageAndCurrent(gprs.getAddress(),
						HighCommandControlCode.READ_VOLTAGE_CURRENT
								.toString());
		logger.info("总召激活地址：" + gprs.getAddress() + "---" + msg);
		gprs.getCtx().writeAndFlush(msg);// 读取电压
		return msg;
	}

	/*public static void main(String[] args) {
		String msg = new HighVoltageDeviceCommandUtil()
				.readVoltageAndCurrent("0400",
						HighCommandControlCode.READ_VOLTAGE_CURRENT
								.toString());
		System.out.println(msg);
	}*/

	@Override
	protected void timedCVReadTask() {
		
	}

	
}
