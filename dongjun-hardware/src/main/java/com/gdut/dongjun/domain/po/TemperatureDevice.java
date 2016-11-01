package com.gdut.dongjun.domain.po;

import java.sql.Timestamp;

public class TemperatureDevice {

	String id;
	
	String name;
	
	String address;
	
	Timestamp addTime;
	
	String available_time;
	
	int groupId;

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

	public Timestamp getAddTime() {
		return addTime;
	}

	public void setAddTime(Timestamp addTime) {
		this.addTime = addTime;
	}

	public String getAvailable_time() {
		return available_time;
	}

	public void setAvailable_time(String available_time) {
		this.available_time = available_time;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	
	
	
}
