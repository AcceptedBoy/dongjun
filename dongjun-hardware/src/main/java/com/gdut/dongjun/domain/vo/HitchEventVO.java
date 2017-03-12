package com.gdut.dongjun.domain.vo;

import java.io.Serializable;

public class HitchEventVO implements Serializable {
	
	private static final long serialVersionUID = 7492353823737332433L;

	private String id;
	
	private Integer type;
	
	private String switchId;
	
	private String groupId;

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

	public String getSwitchId() {
		return switchId;
	}

	public void setSwitchId(String switchId) {
		this.switchId = switchId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "HitchEventVO [id=" + id + ", type=" + type + ", groupId=" + groupId + "]";
	}
	
	
}
