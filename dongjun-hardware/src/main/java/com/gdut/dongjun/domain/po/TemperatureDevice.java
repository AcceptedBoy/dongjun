package com.gdut.dongjun.domain.po;

import java.sql.Timestamp;

public class TemperatureDevice {

	String id;
	
	String name;
	
	String address;
	
	String addTime;
	
	String onlineTime;
	
	String availableTime;
	
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

	public String getAvailableTime() {
		return availableTime;
	}

	public void setAvailableTime(String available_time) {
		this.availableTime = available_time;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(String onlineTime) {
		this.onlineTime = onlineTime;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	
	
	
	
	
}
