package com.gdut.dongjun.domain.po;

public class UserLog extends SystemLog {

	int type;
	
	String userId;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
