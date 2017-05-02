package com.gdut.dongjun;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.core.server.impl.ElectronicServer;
import com.gdut.dongjun.core.server.impl.TemperatureServer;
import com.gdut.dongjun.domain.dao.ProtocolPortMapper;
import com.gdut.dongjun.domain.po.ProtocolPort;
import com.gdut.dongjun.service.webservice.client.WebsiteServiceClient;


/**
 * 这个是应用程序在初始化的时候启动，所以因此可以开启对端口的监听；
 * <p>编写InitializingBean的实现类,项目在加载完毕后立刻执行{@code afterPropertiesSet()} 方法 ,
 * 并且可以使用spring 注入好的bean
 * @author link xiaoMian <972192420@qq.com>
 */
@Component
public class MonitorStartup implements InitializingBean {
	
	@Autowired
	private TemperatureServer temperatureServer;
    @Autowired
    private ElectronicServer electronicServer;
	@Autowired
	private ProtocolPortMapper protocolPortDAOImpl;
//	@Autowired
//	private CacheService cacheService;
	@Autowired
	private WebsiteServiceClient websiteClient;

	private static Logger logger = Logger.getLogger(MonitorStartup.class);

	@Override
	public void afterPropertiesSet() throws Exception {
		//warmCache();
		monitorStartup();
	}

	/**
	 * 缓存预热
	 * TODO 只考虑了高压设备
     */
//	private void warmCache() {
//
//		List<HighVoltageSwitch> hvSwitchList = hvSwitchService.selectByParameters(null);
//		List<HighVoltageVoltage> hvVoltList = null;
//		List<HighVoltageCurrent> hvCurrList = null;
//		String hvSwitchId = null;
//
//		//所有高压设备的电压电流缓存
//		for(HighVoltageSwitch hvSwitch : hvSwitchList) {
//			Integer deStrings[] = new Integer[6];
//			hvSwitchId = hvSwitch.getId();
//			//电压a,b,c相
//			hvVoltList = hvVoltageService
//					.getRecentlyVoltage(hvSwitchId, "A");
//			if (hvVoltList != null && hvVoltList.size() != 0) {
//				deStrings[0] = hvVoltList.get(0).getValue();
//			}
//			hvVoltList = hvVoltageService
//					.getRecentlyVoltage(hvSwitchId, "B");
//			if (hvVoltList != null && hvVoltList.size() != 0) {
//				deStrings[1] = hvVoltList.get(0).getValue();
//			}
//			hvVoltList = hvVoltageService
//					.getRecentlyVoltage(hvSwitchId, "C");
//			if (hvVoltList != null && hvVoltList.size() != 0) {
//				deStrings[2] = hvVoltList.get(0).getValue();
//			}
//			//电流a,b,c相
//			hvCurrList = hvCurrentService
//					.getRecentlyCurrent(hvSwitchId, "A");
//			if (hvCurrList != null && hvCurrList.size() != 0) {
//				deStrings[3] = hvCurrList.get(0).getValue();
//			}
//			hvCurrList = hvCurrentService
//					.getRecentlyCurrent(hvSwitchId, "B");
//			if (hvCurrList != null && hvCurrList.size() != 0) {
//				deStrings[4] = hvCurrList.get(0).getValue();
//			}
//			hvCurrList = hvCurrentService
//					.getRecentlyCurrent(hvSwitchId, "C");
//			if (hvCurrList != null && hvCurrList.size() != 0) {
//				deStrings[5] = hvCurrList.get(0).getValue();
//			}
//			//最后一步
//			cacheService.put(hvSwitch.getId(), deStrings);
//		}
//		logger.info("缓存预热完成");
//	}

	/**
	 * 在项目启动的时候开启监听端口
	 * @throws Exception
	 */
	private void monitorStartup() throws Exception {
		List<ProtocolPort> ports = protocolPortDAOImpl.selectByParameters(null);
		for (ProtocolPort port : ports) {
			if (port.getRemark().equals("temperature_device")) {
				temperatureServer.setPort(port.getPort());
			} else if (port.getRemark().equals("electronic_module")) {
				electronicServer.setPort(port.getPort());
			}
			else {
				logger.warn("服务器启动端" + port.getRemark() + ":" + port.getPort() + "口有误，请查看服务器启动代码是否有误");
			}
		}

		if (null != temperatureServer.getPort()) {
			logger.info("温度设备端口号：" + temperatureServer.getPort());
			temperatureServer.start();
		}
		if (null != electronicServer.getPort()) {
			logger.info("电能表端口号：" + electronicServer.getPort());
			electronicServer.start();
		}
	}	
}