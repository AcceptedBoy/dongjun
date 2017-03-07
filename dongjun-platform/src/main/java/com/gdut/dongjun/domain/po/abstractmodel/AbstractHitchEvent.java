package com.gdut.dongjun.domain.po.abstractmodel;

public abstract class AbstractHitchEvent {
	
	private String id;
	
	private byte type;
	
	private String hitchTime;
	
	private String hitchReason;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
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
}
