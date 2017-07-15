package com.gdut.dongjun.core.handler;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.service.HighVoltageSwitchService;
import com.gdut.dongjun.util.HighVoltageDeviceCommandUtil;

/**
 * 用于website系统的实时监测报文模块
 * @author Gordan_Deng
 * @date 2017年7月15日
 */
@Component
public class DataMonitorService {
	
	@Autowired
	private HighVoltageSwitchService switchService;

	/**
	 * 这里存储的是设备的地址到设备的id的映射关系
	 */
	private static Map<String, String> monitorMap = new ConcurrentHashMap<>();
	
	/**
	 * 增加被监视的设备
	 * @param switchId
	 */
	public void addMonitor(String switchId) {
		HighVoltageSwitch s = switchService.selectByPrimaryKey(switchId);
		char[] char_addr = Integer.toHexString(Integer.parseInt(s.getAddress())).toCharArray();
		char[] address = new char[4];
		if (char_addr.length != 4) {
			for (int i = 0; i < 4; i++) {
				if (i < char_addr.length) {
					address[i] = char_addr[i];
				} else {
					address[i] = 0;
				}
			}
		}
		String address1 = String.valueOf(address);
		address1  = new HighVoltageDeviceCommandUtil().reverseString(address1);
		monitorMap.put(address1, switchId);
	}
	
	/**
	 * 移除被监视的设备
	 * @param switchId
	 */
	public void removeMonitor(String switchId) {
		String key = null;
		for (Entry<String, String> entry : monitorMap.entrySet()) {
			if (entry.getValue().equals(switchId)) {
				key = entry.getKey();
			}
		}
		monitorMap.remove(key);
	}
	
	/**
	 * 该地址是否被监视中
	 * @param address
	 * @return
	 */
	public boolean isMonitored(String address) {
		if (monitorMap.containsKey(address)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 通过id得到设备地址
	 * @param switchId
	 * @return
	 */
	public String getDeviceAddressBySwitchId(String switchId) {
		for (Entry<String, String> entry : monitorMap.entrySet()) {
			if (entry.getValue().equals(switchId)) {
				return entry.getKey();
			}
		}
		return null;
	}
}
