package com.gdut.dongjun.service.webservice.server.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.core.HighVoltageCtxStore;
import com.gdut.dongjun.service.webservice.server.CenterCallHardwareService;
import com.gdut.dongjun.util.HighVoltageDeviceCommandUtil;

@Service
public class CenterCallHardwareServiceImpl implements CenterCallHardwareService {
	
	@Autowired
	private HighVoltageCtxStore ctxStore;
	
	private static Logger logger = Logger.getLogger(CenterCallHardwareServiceImpl.class);
	
	@Override
	public void updateSwitchAddressAvailable(List<String> addrs) {
		StringBuilder sb = new StringBuilder();
		sb.append("系统更换可运作的设备地址：");
		for (String s : addrs) {
			sb.append(s + " ");
		}
		logger.info(sb.toString());
		//10进制的数字转换为16进制
		List<String> realAddr = new ArrayList<String>();
		for (String addr : addrs) {
			String hexAddr = String.valueOf(Integer.toHexString(Integer.parseInt(addr)));
			sb = new StringBuilder();
			if (hexAddr.length() < 4) {
				for (int i = 0; i < 4 - hexAddr.length(); i++) {
					sb.append("0".intern());
				}
			}
			sb.append(hexAddr);
			String address = HighVoltageDeviceCommandUtil.reverseString(
					sb.toString());
			realAddr.add(address);
		}
		ctxStore.setAvailableAddrList(realAddr);
	}

}
