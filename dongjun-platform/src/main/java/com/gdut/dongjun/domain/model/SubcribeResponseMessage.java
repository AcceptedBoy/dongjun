package com.gdut.dongjun.domain.model;

import java.util.Date;

public class SubcribeResponseMessage {

	private Object text;
	
	private Integer messageType;
	
	private Date date;

	public Object getText() {
		return text;
	}

	public void setText(Object text) {
		this.text = text;
	}

	public Integer getMessageType() {
		return messageType;
	}

	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
}
