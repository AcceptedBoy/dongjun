package com.gdut.dongjun.service.rmi.po;

import java.io.Serializable;


/**
 * 为了能够提供接口服务，删去了ctx
 * @author acceptedboy
 */
public class SwitchGPRS implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;//ID
	private String address;//地址
	private boolean isOpen = false;//开关是否跳闸的标志(从合闸->分闸才算跳闸)
	/**
	 * 1为预备合闸， 2为预备分闸，3为未预备
	 */
	private Integer prepareType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	
	public Integer getPrepareType() {
		return prepareType;
	}
	
	public void setPrepareType(Integer prepareType) {
		this.prepareType = prepareType;
	}
	
	
	
	public SwitchGPRS() {
		super();
	}
	
	public SwitchGPRS(String id, String address, boolean isOpen) {
		this.id = id;
		this.address = address;
		this.isOpen = isOpen;
	}
}
