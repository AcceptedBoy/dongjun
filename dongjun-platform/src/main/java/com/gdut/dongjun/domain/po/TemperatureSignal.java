package com.gdut.dongjun.domain.po;

public class TemperatureSignal {
	int id;
	String device_id;
	String date;
	String value;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public TemperatureSignal(int id, String device_id, String date, String value) {
		super();
		this.id = id;
		this.device_id = device_id;
		this.date = date;
		this.value = value;
	}

	public TemperatureSignal() {
		super();
	}

}
