package com.gdut.dongjun.domain.po.abstractmodel;

public abstract class AbstractHitchEvent {

	private String id;
	
	private String hitchReason;
	
	private Integer type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHitchReason() {
		return hitchReason;
	}

	public void setHitchReason(String hitchReason) {
		this.hitchReason = hitchReason;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
