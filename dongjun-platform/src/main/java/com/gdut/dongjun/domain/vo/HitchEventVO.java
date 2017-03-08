package com.gdut.dongjun.domain.vo;

import java.io.Serializable;

public class HitchEventVO implements Serializable {

	private static final long serialVersionUID = 2069917454567540315L;

	private String id;

	private String switchId;
	
	private String groupId;
	
	private Integer type;
	
	private String hitchTime;
	
	private String hitchReason;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSwitchId() {
		return switchId;
	}

	public void setSwitchId(String switchId) {
		this.switchId = switchId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getHitchTime() {
		return hitchTime;
	}

	public void setHitchTime(String hitchTime) {
		this.hitchTime = hitchTime;
	}

	public String getHitchReason() {
		return hitchReason;
	}

	public void setHitchReason(String hitchReason) {
		this.hitchReason = hitchReason;
	}

	@Override
	public String toString() {
		return "HitchEventVO [id=" + id + ", switchId=" + switchId + ", groupId=" + groupId + ", type=" + type
				+ ", hitchTime=" + hitchTime + ", hitchReason=" + hitchReason + "]";
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}