package com.gdut.dongjun.domain.po.abstractmodel;

/**
 * TODO
 * @author Gordan_Deng
 * @date 2017年3月8日
 */
public abstract class AbstractHitchEvent extends AbstractBean {

	protected String id;

	protected String monitorId;
	
	protected String groupId;
	
	protected Integer type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMonitorId() {
		return monitorId;
	}

	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
