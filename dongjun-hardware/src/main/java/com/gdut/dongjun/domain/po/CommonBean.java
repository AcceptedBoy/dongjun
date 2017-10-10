package com.gdut.dongjun.domain.po;

import java.util.Date;

public abstract class CommonBean {

	protected Date gmtCreate;
	protected Date gmtModified;

	public Date getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public Date getGmtModified() {
		return gmtModified;
	}
	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}
}
