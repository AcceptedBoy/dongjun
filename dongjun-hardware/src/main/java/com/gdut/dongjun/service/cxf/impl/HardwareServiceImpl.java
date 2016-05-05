package com.gdut.dongjun.service.cxf.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebService;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.core.device.Device;
import com.gdut.dongjun.domain.HighVoltageStatus;
import com.gdut.dongjun.service.cxf.HardwareService;

@WebService(endpointInterface = "com.gdut.dongjun.service.cxf.HardwareService")
public class HardwareServiceImpl implements HardwareService {

	@Resource(name = "LowVoltageDevice")
	private Device lowVoltageDevice;

	@Resource(name = "HighVoltageDevice")
	private Device highVoltageDevice;

	@Resource(name = "ControlMeasureDevice")
	private Device controlMeasureDevice;
	
	@Override
	public String generateOpenSwitchMessage(String address, int type) {
		// TODO Auto-generated method stub
		if(address == null) {
			return null;
		}
		String msg = null;
		switch (type) {
		case 0:// 低压开关
			msg = lowVoltageDevice.generateOpenSwitchMessage(address);
			break;
		case 1:// 高压开关
			msg = highVoltageDevice.generateOpenSwitchMessage(address);
			break;
		case 2:// 管控开关
			msg = controlMeasureDevice.generateOpenSwitchMessage(address);
			break;
		default:
			break;
		}
		if (msg != null && CtxStore.getCtxByAddress(address) != null) {
			
			CtxStore.getCtxByAddress(address).writeAndFlush(msg);
			return msg;
		}
		return null;
	}

	@Override
	public String generateCloseSwitchMessage(String address, int type) {
		// TODO Auto-generated method stub
		if(address == null) {
			return null;
		}
		String msg = null;
		switch (type) {
		case 0:// 低压开关
			msg = lowVoltageDevice.generateCloseSwitchMessage(address);
			break;
		case 1:// 高压开关
			msg = highVoltageDevice.generateCloseSwitchMessage(address);
			break;
		case 2:// 管控开关
			msg = controlMeasureDevice.generateCloseSwitchMessage(address);
			break;
		default:
			break;
		}
		if (msg != null && CtxStore.getCtxByAddress(address) != null) {
			
			CtxStore.getCtxByAddress(address).writeAndFlush(msg);
			return msg;
		}
		return null;
	}

	@Override
	public String getOnlineAddressById(String id) {
		// TODO Auto-generated method stub
		SwitchGPRS gprs = CtxStore.get(id);
		if(gprs != null) {
			return gprs.getAddress();
		}
		return null;
	}

	@Override
	public List<com.gdut.dongjun.service.cxf.po.SwitchGPRS> getCtxInstance() {
		
		return CtxStore.getPostInstance();
	}
	
	@Override
	public com.gdut.dongjun.service.cxf.po.SwitchGPRS getSwitchGPRS(String id) {
	
		SwitchGPRS gprs = CtxStore.get(id);
		if(gprs == null) {
			return null;
		}
		return new com.gdut.dongjun.service.cxf.po.SwitchGPRS(gprs);
	}

	@Override
	public HighVoltageStatus getStatusbyId(String id) {
		
		return CtxStore.getStatusbyId(id);
	}
	
	@Override
	public boolean changeCtxOpen(String switchId) {
		
		if(CtxStore.changeOpen(switchId)) {
			return true;
		}
		return false;
	}
}
