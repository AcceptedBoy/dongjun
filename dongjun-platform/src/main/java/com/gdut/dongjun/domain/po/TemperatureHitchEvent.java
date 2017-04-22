package com.gdut.dongjun.domain.po;

public class TemperatureHitchEvent extends AbstractBean {

	private String id;
	
	private String deviceId;
	
	private Integer changeType;
	
	private String hitchReason;
		
	private String hitchPhase;
	
	private String hitchTime;
	
	private String solvePeople;
	
	private String solveTime;
	
	private String solveWay;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getChangeType() {
		return changeType;
	}

	public void setChangeType(Integer changeType) {
		this.changeType = changeType;
	}

	public String getHitchReason() {
		return hitchReason;
	}

	public void setHitchReason(String hitchReason) {
		this.hitchReason = hitchReason;
	}

	public String getHitchPhase() {
		return hitchPhase;
	}

	public void setHitchPhase(String hitchPhase) {
		this.hitchPhase = hitchPhase;
	}

	public String getHitchTime() {
		return hitchTime;
	}

	public void setHitchTime(String hitchTime) {
		this.hitchTime = hitchTime;
	}

	public String getSolvePeople() {
		return solvePeople;
	}

	public void setSolvePeople(String solvePeople) {
		this.solvePeople = solvePeople;
	}

	public String getSolveTime() {
		return solveTime;
	}

	public void setSolveTime(String solveTime) {
		this.solveTime = solveTime;
	}

	public String getSolveWay() {
		return solveWay;
	}

	public void setSolveWay(String solveWay) {
		this.solveWay = solveWay;
	}
	
	

	
}
