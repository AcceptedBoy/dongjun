package com.gdut.dongjun.service.webservice.client.po;

import java.io.Serializable;

/**
 * Channel相关信息
 * @author Gordan_Deng
 * @date 2017年5月11日
 */
public class ChannelInfo implements Serializable {

	private static final long serialVersionUID = -3246597730301962347L;
	
	private String monitorId;	//数据监控id
	private String groupId;		//所属公司id
	private String moduleId; 	//子模块id
	private String address;		//子模块真实地址
	private String decimalAddress;	//子模块的十进制地址
	
	public ChannelInfo() {
		super();
	}

	public String getMonitorId() {
		return monitorId;
	}

	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDecimalAddress() {
		return decimalAddress;
	}

	public void setDecimalAddress(String decimalAddress) {
		this.decimalAddress = decimalAddress;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	} 


}
