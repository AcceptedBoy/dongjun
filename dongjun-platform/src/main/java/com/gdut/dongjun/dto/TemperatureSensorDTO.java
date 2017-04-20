package com.gdut.dongjun.dto;

import com.gdut.dongjun.domain.po.TemperatureSensor;

public class TemperatureSensorDTO {
	
	private static final String NO_TYPE = "无类型";
	private static final String IN_A = "进线A相";
	private static final String IN_B = "进线B相";
	private static final String IN_C = "进线C相";
	private static final String OUT_A = "出线A相";
	private static final String OUT_B = "出线B相";
	private static final String OUT_C = "出线C相";
	
	private String id;
	
	private String name;
	
	private String typeName;
	
	private int type;
	
	private int tag;
	
	private String deviceId;
	
	public static TemperatureSensorDTO wrap(TemperatureSensor sensor) {
		TemperatureSensorDTO dto = new TemperatureSensorDTO();
		dto.setId(sensor.getId());
		dto.setName(sensor.getName());
		dto.setType(sensor.getType());
		dto.setTag(sensor.getTag());
		dto.setDeviceId(sensor.getDeviceId());
		switch (sensor.getType()) {
		case 0 : dto.setTypeName(NO_TYPE); break;
		case 1 : dto.setTypeName(IN_A); break;
		case 2 : dto.setTypeName(IN_B); break;
		case 3 : dto.setTypeName(IN_C); break;
		case 4 : dto.setTypeName(OUT_A); break;
		case 5 : dto.setTypeName(OUT_B); break;
		case 6 : dto.setTypeName(OUT_C); break;
		default : break;
		}
		return dto;
	}

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

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	
}
