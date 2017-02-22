package com.gdut.dongjun.dto;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.gdut.dongjun.domain.po.abstractmodel.AbstractDevice;
import com.gdut.dongjun.service.webservice.client.HardwareServiceClient;
import com.gdut.dongjun.service.webservice.client.po.SwitchGPRS;
import com.gdut.dongjun.util.GenericUtil;

public class SwitchStatus {

	private String id;

	private String name;

	private String address;

	private String simNumber;

	private String deviceNumber;

	private String onlineTime;

	private String status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSimNumber() {
		return simNumber;
	}

	public void setSimNumber(String simNumber) {
		this.simNumber = simNumber;
	}

	public String getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	public String getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(String onlineTime) {
		this.onlineTime = onlineTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 在原来设备数据的基础上包装一个status，用于管理页面的设备展示
	 * @param list
	 * @param client
	 * @return
	 * @throws RemoteException
	 */
	public static List<SwitchStatus> wrap(List<? extends AbstractDevice> list,  HardwareServiceClient client) throws RemoteException {
		List<SwitchGPRS> gprsList = client.getService().getCtxInstance();
		List<SwitchStatus> statusList = new ArrayList<SwitchStatus>();
		for (Object s : list) {
			boolean isOpen = false;
			SwitchStatus status = new SwitchStatus();
			status.setId((String) GenericUtil.getPrivateObjectValue(s, "id"));
			status.setDeviceNumber((String) GenericUtil.getPrivateObjectValue(s, "deviceNumber"));
			status.setAddress((String) GenericUtil.getPrivateObjectValue(s, "address"));
			status.setName((String) GenericUtil.getPrivateObjectValue(s, "name"));
			status.setOnlineTime((String) GenericUtil.getPrivateObjectValue(s, "onlineTime"));
			status.setSimNumber((String) GenericUtil.getPrivateObjectValue(s, "simNumber"));
			status.setStatus("-1");		//预设设备不在线
			
			for (SwitchGPRS gprs : gprsList) {
				if (gprs.getId().equals(status.getId())) {
					if (gprs.isOpen()) {
						status.setStatus("01");		//设备合闸
						break;
					} else {
						status.setStatus("00");		//设备分闸
						break;
					}
				}
			}

			statusList.add(status);
		}
		return statusList;
	}

}
