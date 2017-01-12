package com.gdut.dongjun.service.webservice.server.impl;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.core.device.Device;
import com.gdut.dongjun.core.device_message_engine.impl.HighVoltageSwitchMessageEngine;
import com.gdut.dongjun.domain.HighVoltageStatus;
import com.gdut.dongjun.domain.po.HighVoltageHitchEvent;
import com.gdut.dongjun.domain.vo.ActiveHighSwitch;
import com.gdut.dongjun.service.HighVoltageHitchEventService;
import com.gdut.dongjun.service.webservice.server.HardwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class HardwareServiceImpl implements HardwareService {

	/**
	 * 总召池
	 */
	private static ExecutorService callerPool = Executors.newFixedThreadPool(
			Runtime.getRuntime().availableProcessors() << 1 - 1);

	@Resource(name = "LowVoltageDevice")
	private Device lowVoltageDevice;

	@Resource(name = "HighVoltageDevice")
	private Device highVoltageDevice;

	@Resource(name = "ControlMeasureDevice")
	private Device controlMeasureDevice;
	
	@Autowired
	private HighVoltageHitchEventService eventService;

	@Autowired
	private HighVoltageSwitchMessageEngine messageEngine;
	
	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.rmi.HardwareService#generateOpenSwitchMessage(java.lang.String, int)
	 */
	@Override
	public String generateOpenSwitchMessage(final String address, Integer type) {
	
		if(address == null) {
			return null;
		}
		String msg = null;
		SwitchGPRS gprs = null;
		String callMsg = null;
		switch (type) {
		case 0:// 低压开关
			msg = lowVoltageDevice.generateOpenSwitchMessage(address);
			break;
		case 1:// 高压开关
			msg = highVoltageDevice.generateOpenSwitchMessage(address);
			callMsg = messageEngine.generateTotalCallMsg(address);
			break;
		case 2:// 管控开关
			msg = controlMeasureDevice.generateOpenSwitchMessage(address);
			break;
		default:
			break;
		}
		if (msg != null && CtxStore.getCtxByAddress(address) != null) {
			
			CtxStore.getCtxByAddress(address).writeAndFlush(msg);
			final String callMsg0 = callMsg;
			callerPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(1000 * 5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally{
						CtxStore.getCtxByAddress(address).writeAndFlush(callMsg0);
					}
				}
			});
			return msg;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.rmi.HardwareService#generateCloseSwitchMessage(java.lang.String, int)
	 */
	@Override
	public String generateCloseSwitchMessage(final String address, Integer type) {
		if(address == null) {
			return null;
		}
		String msg = null;
		String callMsg = null; //总召报文
		SwitchGPRS gprs = null;
		switch (type) {
		case 0:// 低压开关
			msg = lowVoltageDevice.generateCloseSwitchMessage(address);
			break;
		case 1:// 高压开关
			msg = highVoltageDevice.generateCloseSwitchMessage(address);
			callMsg = messageEngine.generateTotalCallMsg(address);
			break;
		case 2:// 管控开关
			msg = controlMeasureDevice.generateCloseSwitchMessage(address);
			break;
		default:
			break;
		}
		if (msg != null && CtxStore.getCtxByAddress(address) != null) {
			CtxStore.getCtxByAddress(address).writeAndFlush(msg);
			final String callMsg0 = callMsg;
			callerPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(1000 * 5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally{
						CtxStore.getCtxByAddress(address).writeAndFlush(callMsg0);
					}
				}
			});
		}
		return msg;
	}

	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.rmi.HardwareService#getOnlineAddressById(java.lang.String)
	 */
	@Override
	public String getOnlineAddressById(String id) {
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
	public List<SwitchGPRS> getCtxInstance() {
		return CtxStore.getInstance();
	}
	
	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.rmi.HardwareService#getSwitchGPRS(java.lang.String)
	 */
	@Override
	public SwitchGPRS getSwitchGPRS(String id) {
		return CtxStore.get(id);
	}

	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.rmi.HardwareService#getStatusbyId(java.lang.String)
	 */
	@Override
	public HighVoltageStatus getStatusbyId(String id) {
		return CtxStore.getStatusbyId(id);
	}
	
	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.rmi.HardwareService#changeCtxOpen(java.lang.String)
	 */
	@Override
	public boolean changeCtxOpen(String switchId) {
		if(CtxStore.changeOpen(switchId)) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.rmi.HardwareService#getActiveSwitchStatus()
	 */
	@Override
	public List<ActiveHighSwitch> getActiveSwitchStatus() {
		//被客户拉取信息后将变化设为false
		//CtxStore.falseChange();
		
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
	@Deprecated
	public boolean whetherChangeInfo() {
		return CtxStore.whetherChangeInfo();
	}

}
