package com.gdut.dongjun.dto;

import com.gdut.dongjun.util.GenericUtil;

public class SwitchDTO {
	
	public String id;
	
	public String name;
	
	public String deviceNumber;
	
	public Integer type;

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

	public String getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public static SwitchDTO wrap(Object obj, Integer type) {
		SwitchDTO decorator = new SwitchDTO();
		decorator.setId((String)GenericUtil.getPrivateObjectValue(obj, "id"));
		decorator.setName((String)GenericUtil.getPrivateObjectValue(obj, "name"));
		decorator.setDeviceNumber((String)GenericUtil.getPrivateObjectValue(obj, "deviceNumber"));
		decorator.setType(type);
		return decorator;
	}
	
}