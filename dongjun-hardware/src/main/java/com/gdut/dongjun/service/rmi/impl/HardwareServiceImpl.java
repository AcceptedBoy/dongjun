package com.gdut.dongjun.service.rmi.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.core.device.Device;
import com.gdut.dongjun.domain.HighVoltageStatus;
import com.gdut.dongjun.domain.po.HighVoltageHitchEvent;
import com.gdut.dongjun.domain.vo.ActiveHighSwitch;
import com.gdut.dongjun.service.HighVoltageHitchEventService;
import com.gdut.dongjun.service.rmi.HardwareService;

/**
 * 在远程方法实现类中，如果是spring配置rmi的话一定不能继承  {@code UnicastRemoteObject},否则会出现如下提示：
 * {@code Caused by: java.rmi.server.ExportException: object already exported}
 * @author link xiaoMian <972192420@qq.com>
 */
public class HardwareServiceImpl implements HardwareService {

	public HardwareServiceImpl() throws RemoteException {
		super();
	}

	@Resource(name = "LowVoltageDevice")
	private Device lowVoltageDevice;

	@Resource(name = "HighVoltageDevice")
	private Device highVoltageDevice;

	@Resource(name = "ControlMeasureDevice")
	private Device controlMeasureDevice;
	
	@Autowired
	private HighVoltageHitchEventService eventService;
	
	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.rmi.HardwareService#generateOpenSwitchMessage(java.lang.String, int)
	 */
	@Override
	public String generateOpenSwitchMessage(String address, int type) throws RemoteException {
	
		if(address == null) {
			return null;
		}
		String msg = null;
		SwitchGPRS gprs = null;
		switch (type) {
		case 0:// 低压开关
			msg = lowVoltageDevice.generateOpenSwitchMessage(address);
			break;
		case 1:// 高压开关
			gprs = CtxStore.getByAddress(address);
			if(gprs != null) {
				gprs.setPrepareType(2);
			}
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

	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.rmi.HardwareService#generateCloseSwitchMessage(java.lang.String, int)
	 */
	@Override
	public String generateCloseSwitchMessage(String address, int type) throws RemoteException { 
		if(address == null) {
			return null;
		}
		String msg = null;
		SwitchGPRS gprs = null;
		switch (type) {
		case 0:// 低压开关
			msg = lowVoltageDevice.generateCloseSwitchMessage(address);
			break;
		case 1:// 高压开关
			gprs = CtxStore.getByAddress(address);
			if(gprs != null) {
				gprs.setPrepareType(1);
			}
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
		}
		return msg;
	}

	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.rmi.HardwareService#getOnlineAddressById(java.lang.String)
	 */
	@Override
	public String getOnlineAddressById(String id) throws RemoteException {
		SwitchGPRS gprs = CtxStore.get(id);
		if(gprs != null) {
			return gprs.getAddress();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.rmi.HardwareService#getCtxInstance()
	 */
	@Override
	public List<SwitchGPRS> getCtxInstance() throws RemoteException {
		return CtxStore.getInstance();
	}
	
	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.rmi.HardwareService#getSwitchGPRS(java.lang.String)
	 */
	@Override
	public SwitchGPRS getSwitchGPRS(String id) throws RemoteException {
		return CtxStore.get(id);
	}

	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.rmi.HardwareService#getStatusbyId(java.lang.String)
	 */
	@Override
	public HighVoltageStatus getStatusbyId(String id) throws RemoteException {
		return CtxStore.getStatusbyId(id);
	}
	
	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.rmi.HardwareService#changeCtxOpen(java.lang.String)
	 */
	@Override
	public boolean changeCtxOpen(String switchId) throws RemoteException {
		if(CtxStore.changeOpen(switchId)) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.rmi.HardwareService#getActiveSwitchStatus()
	 */
	@Override
	public List<ActiveHighSwitch> getActiveSwitchStatus()
			throws RemoteException {
		//被客户拉取信息后将变化设为false
		CtxStore.falseChange();
		
		List<SwitchGPRS> switchs = CtxStore.getInstance();
		List<ActiveHighSwitch> list = new ArrayList<>();

		if(switchs != null) {
			for(SwitchGPRS s : switchs) {
				
				HighVoltageStatus status = getStatusbyId(s.getId());
				if(status != null) {
					
					ActiveHighSwitch as = new ActiveHighSwitch();
					as.setId(s.getId());
					as.setOpen(s.isOpen());
					as.setStatus(status.getStatus());
					if(s.isOpen() == true) {
						//唯一一个需要去查找数据库的操作
						HighVoltageHitchEvent event = eventService.getRecentHitchEvent(s.getId());
						if(event != null) {
							as.setHitchEventId(event.getId());
						}
					}
					list.add(as);
				}
			}
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.rmi.HardwareService#whetherChangeInfo()
	 */
	@Override
	public boolean whetherChangeInfo() throws RemoteException {
		return CtxStore.whetherChangeInfo();
	}

}