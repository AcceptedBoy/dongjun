package com.gdut.dongjun.service.webservice.server.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.HighVoltageCtxStore;
import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.core.TemperatureCtxStore;
import com.gdut.dongjun.core.device.Device;
import com.gdut.dongjun.core.device.HighVoltageDevice;
import com.gdut.dongjun.core.device_message_engine.impl.HighVoltageSwitchMessageEngine;
import com.gdut.dongjun.core.handler.DataMonitorService;
import com.gdut.dongjun.domain.HighVoltageStatus;
import com.gdut.dongjun.domain.po.AbnormalDevice;
import com.gdut.dongjun.domain.po.HighVoltageHitchEvent;
import com.gdut.dongjun.domain.vo.ActiveHighSwitch;
import com.gdut.dongjun.service.AbnormalDeviceService;
import com.gdut.dongjun.service.HighVoltageHitchEventService;
import com.gdut.dongjun.service.HighVoltageSwitchService;
import com.gdut.dongjun.service.webservice.server.HardwareService;

@Component
public class HardwareServiceImpl implements HardwareService {

	@Resource(name = "LowVoltageDevice")
	private Device lowVoltageDevice;
	@Autowired
	private HighVoltageDevice highVoltageDevice;
	@Resource(name = "ControlMeasureDevice")
	private Device controlMeasureDevice;
	@Autowired
	private HighVoltageHitchEventService eventService;
	@Autowired
	private HighVoltageSwitchMessageEngine messageEngine;
	@Autowired
	private AbnormalDeviceService abDeviceService;
	@Autowired
	private HighVoltageCtxStore hvCtxStore;
	@Autowired
	private HighVoltageSwitchService highVoltageSwitchService;
	@Autowired
	private DataMonitorService monitorService;
	
	/**
	 * 总召池，下面方法得到当前可用处理器个数，数量x2-1，就是线程池固有线程数
	 */
	private static ExecutorService callerPool = Executors.newFixedThreadPool(
			Runtime.getRuntime().availableProcessors() << 1 - 1);

	private final Logger logger = Logger.getLogger(HardwareServiceImpl.class);
	
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
		msg = msg.toLowerCase();
		if (msg != null && hvCtxStore.getCtxByAddress(address) != null) {
			logger.info("地址为" + address + "的设备执行分闸：" + msg);
			hvCtxStore.getCtxByAddress(address).writeAndFlush(msg);
			final String callMsg0 = callMsg;
			callerPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(1000 * 10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally{
						//680c0c68530100640106010100000014d516
						logger.info("地址为" + address + "的设备发送分闸后的总召");
						hvCtxStore.getCtxByAddress(address).writeAndFlush(callMsg0);
					}
				}
			});
//			handleOldMachineTotalCall(address, callMsg);
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
		msg = msg.toLowerCase();
		if (msg != null && hvCtxStore.getCtxByAddress(address) != null) {
			logger.info("地址为" + address + "的设备执行合闸：" + msg);
			hvCtxStore.getCtxByAddress(address).writeAndFlush(msg);
			final String callMsg0 = callMsg;
			callerPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(1000 * 10);	//等待一分钟
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally{
						//680c0c68530100640106010100000014d516
						hvCtxStore.getCtxByAddress(address).writeAndFlush(callMsg0);
					}
				}
			});
//			handleOldMachineTotalCall(address, callMsg);
		}
		return msg;
	}

	/**
	 * 老机器开关闸报文发出后，有的异常机器不会响应总召。
	 * 这里30秒内每10秒发一次总召，解决这种情况。
	 * @param address
	 * @param callMsg
	 */
	private void handleOldMachineTotalCall(String address, String callMsg) {
		List<AbnormalDevice> abDevices = abDeviceService.selectByParameters(null);
		
		String id = hvCtxStore.getIdbyAddress(address);
		if (id == null || id.equals("")) {
			//TODO
			return ;
		}
		for (AbnormalDevice device : abDevices) {
			if (id.equals(device.getSwitchId()) || id == device.getSwitchId()) {
				if (device.getReason() == 1) {
					//TODO
					final String realAddress = address;
					final String callMsg1 = callMsg;
					callerPool.execute(new Runnable() {
						@Override
						public void run() {
							int count = 0;
							while (count < 3) {
								try {
									Thread.sleep(1000 * 30);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								hvCtxStore.getCtxByAddress(realAddress).writeAndFlush(callMsg1);
								logger.info(realAddress + "老机器额外总召: " + callMsg1);
								count++;
							}
						}
					});
				}
				else
					break;
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.rmi.HardwareService#getOnlineAddressById(java.lang.String)
	 */
	@Override
	public String getOnlineAddressById(String id) {
		SwitchGPRS gprs = hvCtxStore.get(id);
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
		return hvCtxStore.getInstance();
	}
	
	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.rmi.HardwareService#getSwitchGPRS(java.lang.String)
	 */
	@Override
	public SwitchGPRS getSwitchGPRS(String id) {
		return hvCtxStore.get(id);
	}

	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.rmi.HardwareService#getStatusbyId(java.lang.String)
	 */
	@Override
	public HighVoltageStatus getStatusbyId(String id) {
		return hvCtxStore.getStatusbyId(id);
	}
	
	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.rmi.HardwareService#changeCtxOpen(java.lang.String)
	 */
	@Deprecated
	@Override
	public boolean changeCtxOpen(String switchId) {
		if(hvCtxStore.changeOpen(switchId)) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.rmi.HardwareService#getActiveSwitchStatus()
	 */
	@Override
	public List<ActiveHighSwitch> getActiveSwitchStatus() {

		List<SwitchGPRS> switchs = hvCtxStore.getInstance();
		List<ActiveHighSwitch> list = new ArrayList<>();

		if(switchs != null) {
			for(SwitchGPRS s : switchs) {
				
				HighVoltageStatus status = getStatusbyId(s.getId());
				if(status != null) {
					
					ActiveHighSwitch as = new ActiveHighSwitch();
					as.setId(s.getId());
					//open代表跳闸状态，true是分闸，false为合闸
					as.setOpen(s.isOpen());
					//status代表设备状态，00为分闸，01为合闸
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

	@Override
	public void changeTemperatureDevice(String id) {
		TemperatureCtxStore.setBound(id);
	}

	@Override
	public List<Integer> getGPRSModuleStatus(List<String> deviceNumbers) {
		return TemperatureCtxStore.isGPRSAlive(deviceNumbers);
	}

	@Override
	public boolean sendText(String switchId, String text) {
		String addr = monitorService.getDeviceAddressBySwitchId(switchId);
		CtxStore.getByAddress(addr).getCtx().writeAndFlush(text);
		return true;
	}

	@Override
	public boolean regisMonitor(String switchId) {
		monitorService.addMonitor(switchId);
		return true;
	}

	@Override
	public boolean removeMonitor(String switchId) {
		monitorService.removeMonitor(switchId);
		return true;
	}

}
