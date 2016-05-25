package com.gdut.dongjun.service.rmi;

import java.rmi.RemoteException;
import java.util.List;

import com.gdut.dongjun.service.rmi.po.HighVoltageStatus;
import com.gdut.dongjun.service.rmi.po.SwitchGPRS;

public interface HardwareService {

	public String generateOpenSwitchMessage(String address, int type) throws RemoteException;
	
	public String generateCloseSwitchMessage(String address, int type) throws RemoteException;
	
	public String getOnlineAddressById(String id) throws RemoteException;
    
	public List<SwitchGPRS> getCtxInstance() throws RemoteException;
	
	public SwitchGPRS getSwitchGPRS(String id) throws RemoteException;
	
	public HighVoltageStatus getStatusbyId(String id) throws RemoteException;
	
	public boolean changeCtxOpen(String switchId) throws RemoteException;
	
}
