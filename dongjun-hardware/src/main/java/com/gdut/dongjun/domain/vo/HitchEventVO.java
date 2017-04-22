package com.gdut.dongjun.domain.vo;

import java.io.Serializable;

/**
 * 通过cxf回调平台版的推送方法，此vo类用于前端推送报警信息
 * @author Gordan_Deng
 * @date 2017年4月20日
 */
public class HitchEventVO implements Serializable {
	
	private static final long serialVersionUID = 7492353823737332433L;

	private String id;
	
	private Integer type;
	
	private String monitorId;
	
	private String groupId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getMonitorId() {
		return monitorId;
	}

	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "HitchEventVO [id=" + id + ", type=" + type + ", groupId=" + groupId + "]";
	}
	
	
}
