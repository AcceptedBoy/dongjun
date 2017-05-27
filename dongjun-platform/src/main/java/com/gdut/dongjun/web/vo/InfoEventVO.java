package com.gdut.dongjun.web.vo;

import java.io.Serializable;

public class InfoEventVO implements Serializable {
	
	private String infoTime;
	private String infoReason;
	private String name;
	private String groupId;
	private String type;
	
	public String getInfoTime() {
		return infoTime;
	}
	public void setInfoTime(String infoTime) {
		this.infoTime = infoTime;
	}
	public String getInfoReason() {
		return infoReason;
	}
	public void setInfoReason(String infoReason) {
		this.infoReason = infoReason;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
