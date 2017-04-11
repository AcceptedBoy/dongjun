package com.gdut.dongjun.service.webservice.server.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.core.TemperatureCtxStore;
import com.gdut.dongjun.service.webservice.server.HardwareService;

@Component
public class HardwareServiceImpl implements HardwareService {
	
	/**
	 * 总召池，下面方法得到当前可用处理器个数，数量x2-1，就是线程池固有线程数
	 */
//	private static ExecutorService callerPool = Executors.newFixedThreadPool(
//			Runtime.getRuntime().availableProcessors() << 1 - 1);

	private final Logger logger = Logger.getLogger(HardwareServiceImpl.class);

	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.rmi.HardwareService#getOnlineAddressById(java.lang.String)
	 */
	@Override
	public String getOnlineAddressById(String id) {
		SwitchGPRS gprs = CtxStore.get(id);
		if(gprs != null) {
			return gprs.getAddress();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.rmi.HardwareService#getCtxInstance()
	 */
	@Override
	public List<SwitchGPRS> getCtxInstance() {
		return CtxStore.getInstance();
	}
	
	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.rmi.HardwareService#getSwitchGPRS(java.lang.String)
	 */
	@Override
	public SwitchGPRS getSwitchGPRS(String id) {
		return CtxStore.get(id);
	}

	@Override
	public void changeTemperatureDevice(String id) {
		TemperatureCtxStore.setBound(id);
	}

	@Override
	public List<Integer> getGPRSModuleStatus(List<String> deviceNumbers) {
		return TemperatureCtxStore.isGPRSAlive(deviceNumbers);
	}
	
}
