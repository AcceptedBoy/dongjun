package com.gdut.dongjun.domain.po;

import java.sql.Timestamp;

public class TemperatureMeasure {

	String id;
	
	String deviceId;
	
	Timestamp date;
	
	int tag;
	
	String value;

	public TemperatureMeasure() {
		super();
	}

	public TemperatureMeasure(String deviceId, Timestamp date, int tag, String value) {
		super();
		this.deviceId = deviceId;
		this.date = date;
		this.tag = tag;
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

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
