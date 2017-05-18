package com.gdut.dongjun.domain.dto;

import java.io.Serializable;
import java.util.Date;

public class DeviceOnlineDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	
	private Integer deviceType;
	//0下线，1上线
	private Integer status;
	
	private Date date;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}