package com.gdut.dongjun.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.netty.channel.ChannelHandlerContext;


/**
 * @Title: SwitchGPRS.java
 * @Package com.gdut.dongjun.service.impl
 * @Description: TODO
 * @author Sherlock-lee
 * @date 2015年8月11日 下午7:10:20
 * @version V1.0
 */
@Deprecated
public class SwitchGPRS implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;//这个id只是子模块的id
	private String address;//地址
	private transient ChannelHandlerContext ctx;//上下文对象，可以用于发送报文
	private String monitorId;
	
	/**
	 * 1为预备合闸， 2为预备分闸，3为未预备
	 */
	private Integer prepareType;
	private boolean isOpen = false;//开关是否跳闸的标志(从合闸->分闸才算跳闸)

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

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}
	
	public Integer getPrepareType() {
		return prepareType;
	}
	
	public void setPrepareType(Integer prepareType) {
		this.prepareType = prepareType;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public String getMonitorId() {
		return monitorId;
	}

	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
	}
	
}
