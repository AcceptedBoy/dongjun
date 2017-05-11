package com.gdut.dongjun.web.vo;

import java.io.Serializable;

public class HitchEventVO implements Serializable {
	
	private static final long serialVersionUID = -7396867530194618589L;

	protected String hitchTime;
	
	protected String hitchReason;
	
	protected String name;
	
	protected String	groupId;
	//type直接填高压、低压、温度之类的
	protected String type;

	public String getHitchReason() {
		return hitchReason;
	}

	public void setHitchReason(String hitchReason) {
		this.hitchReason = hitchReason;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHitchTime() {
		return hitchTime;
	}

	public void setHitchTime(String hitchTime) {
		this.hitchTime = hitchTime;
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
	
	

	@Override
	public String toString() {
		return "HitchEventDTO [hitchTime=" + hitchTime + ", hitchReason=" + hitchReason + ", name=" + name
				+ ", groupId=" + groupId + ", type=" + type + "]";
	}
}
