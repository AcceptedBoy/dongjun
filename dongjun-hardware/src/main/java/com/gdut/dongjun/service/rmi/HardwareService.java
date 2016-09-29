package com.gdut.dongjun.service.rmi;

import java.rmi.RemoteException;
import java.util.List;

import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.domain.HighVoltageStatus;
import com.gdut.dongjun.domain.vo.ActiveHighSwitch;

/**
 * <p> All of the methods on a RMI Remote interface must declare RemoteException in their throws clause
 * <p>所有在继承了Remote类的方法中都必须申明异常
 * @author link xiaoMian <972192420@qq.com>
 */
public interface HardwareService {
	
	/**
	 * 产生开闸报文并且发送开闸预置
	 */
	public String generateOpenSwitchMessage(String address, int type) throws RemoteException;
	
	/**
	 * 产生合闸报文并发送合闸预置报文
	 */
	public String generateCloseSwitchMessage(String address, int type) throws RemoteException;
	
	/**
	 * 通过开关id来获取在线开关的地址
	 */
	public String getOnlineAddressById(String id) throws RemoteException;
    
	/**
	 * 获取在线开关的所有信息
	 */
	public List<SwitchGPRS> getCtxInstance() throws RemoteException;
	
	/**
	 * 通过开关id来获取在线开关
	 */
	public SwitchGPRS getSwitchGPRS(String id) throws RemoteException;
	
	/**
	 * 通过开关id来获取在线开关的高压状态
	 */
	public HighVoltageStatus getStatusbyId(String id) throws RemoteException;
	
	/**
	 * 通过开关id来改变开关的状态
	 */
	public boolean changeCtxOpen(String switchId) throws RemoteException;
	
	/**
	 * 获取所有在线开关的详细状态
	 */
	public List<ActiveHighSwitch> getActiveSwitchStatus() throws RemoteException;
	
	/**
	 * 这个方法只有在等于true的时候软件客户端才会去发请求向这边请求获取所有在线开关的详细
	 * @see {@link #getActiveSwitchStatus()}
	 */
	public boolean whetherChangeInfo() throws RemoteException;
}
