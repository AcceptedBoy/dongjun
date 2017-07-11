package com.gdut.dongjun.core.handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;

/**
 * 对Channel所对应的信息的抽象 对之前的{@link com.gdut.dongjun.core.SwitchGPRS}功能有所取舍
 * 为什么会有{@code List<ChannelHandlerContext>}这个成员变量？
 * 因为GPRS连接上系统的时候，系统并不知道具体的ChannelHandlerContext，只能用GPRS报文发过来的通道，
 * 也就是GPRSParseStrategy里的ChannelHandlerContext来给电表发总召。当接收到电表的报文时，相关
 * ParseStrategy会清空ChannelInfo里的原有ChannelHandlerContext，将新的ChannelHandlerContext付给它。
 * 由于业务特殊性，现在一个DataMonitor里有多个GPRS，各个子模块都能用这些GPRS，那么就存在不知道哪个
 * GPRS对应电表的情况。为了方便，就设置List<ChannelHandlerContext>，发总召报文时往所有GPRS发。
 * 这的确是个坑
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
	private transient List<ChannelHandlerContext> ctxList;

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
		ctxList = new ArrayList<ChannelHandlerContext>();
		ctxList.add(ctx);
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

	public List<ChannelHandlerContext> getCtxList() {
		return ctxList;
	}

	public void setCtxList(List<ChannelHandlerContext> ctxList) {
		this.ctxList = ctxList;
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
