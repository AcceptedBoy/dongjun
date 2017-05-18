package com.gdut.dongjun.domain.dto;

import java.io.Serializable;

import com.gdut.dongjun.core.InfoConst;
import com.gdut.dongjun.core.handler.ChannelInfo;

/**
 * 普通事件推送
 * @author Gordan_Deng
 * @date 2017年5月13日
 */
public class InfoEventDTO implements Serializable {
	
	private static final long serialVersionUID = -7505881917977316995L;
	
	private String id;					//事件id
	private String monitorId;		//DataMonitor的id
	private String moduleId;		//子模块的id
	private String groupId;			//公司id
	private Integer type;			//通知信息类别
	private Object text;				//附加信息
	
	public InfoEventDTO() {
		super();
	}

	public InfoEventDTO(String id, String 
			monitorId, String moduleId, String groupId, Integer type, Object text) {
		super();
		this.id = id;
		this.monitorId = monitorId;
		this.moduleId = moduleId;
		this.groupId = groupId;
		this.type = type;
		this.text = text;
	}

	public InfoEventDTO(ChannelInfo info) {
		this.monitorId = info.getMonitorId();
		this.moduleId = info.getModuleId();
		this.groupId = info.getGroupId();
	}
	
	public String getMonitorId() {
		return monitorId;
	}
	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public Object getText() {
		return text;
	}
	public void setText(Object text) {
		this.text = text;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
