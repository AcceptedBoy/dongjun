package com.gdut.dongjun.domain.po;

import java.sql.Timestamp;

public class TemperatureMeasure {

	String id;
	
	String deviceId;
	
	Timestamp date;
	
	String sensorAddress;
	
	String value;

	public TemperatureMeasure() {
		super();
	}

	public TemperatureMeasure(String id, String deviceId, Timestamp date, String sensorAddress, String value) {
		super();
		this.id = id;
		this.deviceId = deviceId;
		this.date = date;
		this.sensorAddress = sensorAddress;
		this.value = value;
	}

	public TemperatureMeasure(String deviceId, Timestamp date, String sensorAddress, String value) {
		super();
		this.deviceId = deviceId;
		this.date = date;
		this.sensorAddress = sensorAddress;
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public String getSensorAddress() {
		return sensorAddress;
	}

	public void setSensorAddress(String sensorAddress) {
		this.sensorAddress = sensorAddress;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
