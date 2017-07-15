package com.gdut.dongjun.web.vo;

import java.util.Date;

public class ErrorInfo {

	private String url;
	private Date date;
	private String exception;
	private Integer status;
	private String message;
	
	public ErrorInfo(String url, String exception, Integer status, String message) {
		this.url = url;
		this.exception = exception;
		this.status = status;
		this.message = message;
		this.date = new Date();
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getException() {
		return exception;
	}
	public void setException(String exception) {
		this.exception = exception;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
