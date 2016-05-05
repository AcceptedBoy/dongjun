package com.gdut.dongjun.service.cxf;

import java.util.List;

import javax.jws.WebService;

import com.gdut.dongjun.domain.HighVoltageStatus;

@WebService
public interface HardwareService {

	public String generateOpenSwitchMessage(String address, int type);
	
	public String generateCloseSwitchMessage(String address, int type);
	
	public String getOnlineAddressById(String id);
    
	public List<com.gdut.dongjun.service.cxf.po.SwitchGPRS> getCtxInstance();
	
	public com.gdut.dongjun.service.cxf.po.SwitchGPRS getSwitchGPRS(String id);
	
	public HighVoltageStatus getStatusbyId(String id);
	
	public boolean changeCtxOpen(String switchId);
}
