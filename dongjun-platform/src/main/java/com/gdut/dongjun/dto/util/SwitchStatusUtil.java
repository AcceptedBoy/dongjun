package com.gdut.dongjun.dto.util;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.dto.SwitchStatus;
import com.gdut.dongjun.service.rmi.HardwareService;
import com.gdut.dongjun.util.GenericUtil;

public class SwitchStatusUtil {

	@Autowired
	private HardwareService hardService;
	
	public <T> List<SwitchStatus> wrap(List<T> list) throws RemoteException {
//		List<SwitchGPRS> gprsList = hardService.getCtxInstance();
		List<SwitchStatus> statusList = new ArrayList<SwitchStatus>();
		for (T s : list) {
			boolean isOpen = false;
			SwitchStatus status = new SwitchStatus();
			status.setId((String)GenericUtil.getPrivateObjectValue(s, "id"));
			status.setDeviceNumber((String)GenericUtil.getPrivateObjectValue(s, "deviceNumber"));
			status.setAddress((String)GenericUtil.getPrivateObjectValue(s, "address"));
			status.setName((String)GenericUtil.getPrivateObjectValue(s, "name"));
			status.setOnlineTime((String)GenericUtil.getPrivateObjectValue(s, "onlineTime"));
			status.setSimNumber((String)GenericUtil.getPrivateObjectValue(s, "simNumber"));
			
//			for (SwitchGPRS gprs : gprsList) {
//				if (gprs.getId().equals(status.getId())) {
//					if (gprs.isOpen()) {
//						isOpen = true;
//						break;
//					}
//					else {
//						break;
//					}
//				}
//			} //如果在运行，有gprs在线，就可以获取在线值
			
			if (isOpen) {
				status.setStatus("true");
			}
			else {
				status.setStatus("false");
			} //如果没有运行，直接返回不在线
			
			statusList.add(status);
		}
		return statusList;
	}
}
