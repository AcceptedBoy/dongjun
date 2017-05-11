package com.gdut.dongjun.web.vo;

import com.gdut.dongjun.util.GenericUtil;

/**
 * 或许可以删除了
 * @author Gordan_Deng
 * @date 2017年4月10日
 */
public class SwitchVO {
	
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
	
	public static SwitchVO wrap(Object obj, Integer type) {
		SwitchVO decorator = new SwitchVO();
		decorator.setId((String)GenericUtil.getPrivateObjectValue(obj, "id"));
		decorator.setName((String)GenericUtil.getPrivateObjectValue(obj, "name"));
		decorator.setDeviceNumber((String)GenericUtil.getPrivateObjectValue(obj, "deviceNumber"));
		decorator.setType(type);
		return decorator;
	}
	
}