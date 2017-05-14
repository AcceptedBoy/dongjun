package com.gdut.dongjun.core.handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;

/**
 * 对Channel所对应的信息的抽象 对之前的{@link com.gdut.dongjun.core.SwitchGPRS}功能有所取舍
 * 
 * @author Gordan_Deng
 * @date 2017年5月10日
 */
public class ChannelInfo implements Serializable {

	private static final long serialVersionUID = -3246597730301962347L;

	private String monitorId; // 数据监控id
	private String groupId; // 所属公司id
	private String moduleId; // 子模块id
	private String address; // 子模块真实地址
	private String decimalAddress; // 子模块的十进制地址
	private transient List<Class<?>> handlerClassList; // 现在ChannelPipeline拥有的ChannelHandler的Class
	private transient ChannelHandlerContext ctx;

	public ChannelInfo(String moduleId, String monitorId, String groupId, String decimalAddress,
			ChannelHandlerContext ctx) {
		this(ctx);
		this.moduleId = moduleId;
		this.monitorId = monitorId;
		this.groupId = groupId;
		this.decimalAddress = decimalAddress;
	}

	public ChannelInfo(ChannelHandlerContext ctx) {
		super();
		this.handlerClassList = new ArrayList<Class<?>>();
		checkHandler(ctx.pipeline());
		this.ctx = ctx;
	}

	public ChannelInfo() {
		super();
	}

	private void checkHandler(ChannelPipeline pipeline) {
		// ChannelHandler head = pipeline.firstContext().handler();
		// ChannelHandler tail = pipeline.lastContext().handler();
		Iterator<Entry<String, ChannelHandler>> i = pipeline.iterator();
		while (i.hasNext()) {
			Entry<String, ChannelHandler> entry = (Entry<String, ChannelHandler>) i.next();
			ChannelHandler handler = entry.getValue();
			// if (handler != head || handler != tail) {
			handlerClassList.add(handler.getClass());
			// }
		}
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getMonitorId() {
		return monitorId;
	}

	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<Class<?>> getHandlerClassList() {
		return handlerClassList;
	}

	public void setHandlerClassList(List<Class<?>> handlerClassList) {
		this.handlerClassList = handlerClassList;
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getDecimalAddress() {
		return decimalAddress;
	}

	public void setDecimalAddress(String decimalAddress) {
		this.decimalAddress = decimalAddress;
	}

	@Override
	public String toString() {
		return "ChannelInfo [monitorId=" + monitorId + ", groupId=" + groupId + ", moduleId=" + moduleId + ", address="
				+ address + ", decimalAddress=" + decimalAddress + "]";
	}

}
