package com.gdut.dongjun.domain.po;

public class TemperatureSensor {

	String id;

	String address;
	
	String deviceId;
	
	public TemperatureSensor() {
		super();
	}

	public TemperatureSensor(String id, String address, String deviceId) {
		super();
		this.id = id;
		this.address = address;
		this.deviceId = deviceId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
}
