package com.gdut.dongjun.domain.vo;

import java.io.Serializable;

public class HitchEventVO implements Serializable {

	private static final long serialVersionUID = 2069917454567540315L;
	
	private String id;
	//后面再加个const类吧 TODO
	private Integer type;
	
	private String groupId;
	
	private String monitorId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getMonitorId() {
		return monitorId;
	}

	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
	}

	@Override
	public String toString() {
		return "HitchEventVO [id=" + id + ", type=" + type + ", groupId=" + groupId + "]";
	}
}