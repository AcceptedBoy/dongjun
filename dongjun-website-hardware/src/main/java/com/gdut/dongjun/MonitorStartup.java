package com.gdut.dongjun;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.core.server.impl.ControlMeasureServer;
import com.gdut.dongjun.core.server.impl.HighVoltageServer;
import com.gdut.dongjun.core.server.impl.HighVoltageServer_V1_3;
import com.gdut.dongjun.core.server.impl.LowVoltageServer;
import com.gdut.dongjun.domain.dao.ProtocolPortMapper;
import com.gdut.dongjun.domain.po.ProtocolPort;
import com.gdut.dongjun.service.HighVoltageCurrentService;
import com.gdut.dongjun.service.HighVoltageSwitchService;
import com.gdut.dongjun.service.HighVoltageVoltageService;
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
	private LowVoltageServer lowVoltageServer;
	
	@Autowired
	private HighVoltageServer highVoltageServer;

	@Resource(name = "HighVoltageServer_V1_3")
	private HighVoltageServer_V1_3 highVoltageServer_v1_3;
	
	@Autowired
	private ControlMeasureServer controlMeasureServer;
	
	@Autowired
	private ProtocolPortMapper protocolPortDAOImpl;

	/**
	 * 高压电流
     */
	@Autowired
	private HighVoltageCurrentService hvCurrentService;

	/**
	 * 高压电压
     */
	@Autowired
	private HighVoltageVoltageService hvVoltageService;

	/**
	 * 高压设备
     */
	@Autowired
	private HighVoltageSwitchService hvSwitchService;

	/**
	 * 缓存
     */
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
	private void monitorStartup() {
		List<ProtocolPort> ports = protocolPortDAOImpl.selectByParameters(null);
		for (ProtocolPort port : ports) {
			if (port.getRemark().equals("high_voltage")) {
				highVoltageServer.setPort(port.getPort());
			}
			else if (port.getRemark().equals("low_voltage")) {
				lowVoltageServer.setPort(port.getPort());
			}
			else if (port.getRemark().equals("control_device")) {
				controlMeasureServer.setPort(port.getPort());
			}
			else if (port.getRemark().equals("high_voltage_1.3")) {
				highVoltageServer_v1_3.setPort(port.getPort());
			}
			else {
				logger.warn("没有和端口相符合的服务器，请查看服务器启动代码是否有误");
			}
		}

		logger.info("低压开关端口号：" + lowVoltageServer.getPort());
		logger.info("高压开关端口号：" + highVoltageServer.getPort());
		logger.info("管控开关端口号：" + controlMeasureServer.getPort());
		logger.info("高压版本1.3端口号：" + highVoltageServer_v1_3.getPort());

		lowVoltageServer.start();
		highVoltageServer.start();
		controlMeasureServer.start();
		highVoltageServer_v1_3.start();
	}	
}