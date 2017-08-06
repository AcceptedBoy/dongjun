package com.gdut.dongjun.web.vo;

public class DeviceGroupMappingVO {

	//	DeviceGroupMapping的id
	private String id;
	//	设备名字
	private String name;
	//	设备种类
	private int type;
	//	DeviceGroup的id
	private String deviceGroupId;
	//	设备的id
	private String deviceId;
	
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDeviceGroupId() {
		return deviceGroupId;
	}
	public void setDeviceGroupId(String deviceGroupId) {
		this.deviceGroupId = deviceGroupId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	
}
