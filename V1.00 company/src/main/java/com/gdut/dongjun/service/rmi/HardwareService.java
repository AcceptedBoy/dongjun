package com.gdut.dongjun.service.rmi;

import java.rmi.RemoteException;
import java.util.List;

import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.domain.HighVoltageStatus;
import com.gdut.dongjun.domain.vo.ActiveHighSwitch;

/**
 * 远程方法调用rmi的接口类
 * @author link xiaoMian <972192420@qq.com>
 */
public interface HardwareService {

	public String generateOpenSwitchMessage(String address, int type) throws RemoteException;
	
	public String generateCloseSwitchMessage(String address, int type) throws RemoteException;
	
	public String getOnlineAddressById(String id) throws RemoteException;
    
	public List<SwitchGPRS> getCtxInstance() throws RemoteException;
	
	public SwitchGPRS getSwitchGPRS(String id) throws RemoteException;
	
	public HighVoltageStatus getStatusbyId(String id) throws RemoteException;
	
	public boolean changeCtxOpen(String switchId) throws RemoteException;
	
	public List<ActiveHighSwitch> getActiveSwitchStatus() throws RemoteException;
	
	public boolean whetherChangeInfo() throws RemoteException;
}
