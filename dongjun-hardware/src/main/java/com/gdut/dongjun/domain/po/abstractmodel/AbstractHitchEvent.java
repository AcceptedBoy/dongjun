package com.gdut.dongjun.domain.po.abstractmodel;

/**
 * TODO
 * @author Gordan_Deng
 * @date 2017年3月8日
 */
public abstract class AbstractHitchEvent extends AbstractBean {

	protected String id;

	protected String switchId;
	
	protected String groupId;
	
	protected Integer type;
	
	protected String hitchTime;
	
	protected String hitchReason;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSwitchId() {
		return switchId;
	}

	public void setSwitchId(String switchId) {
		this.switchId = switchId;
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

	public String getHitchTime() {
		return hitchTime;
	}

	public void setHitchTime(String hitchTime) {
		this.hitchTime = hitchTime;
	}

	public String getHitchReason() {
		return hitchReason;
	}

	public void setHitchReason(String hitchReason) {
		this.hitchReason = hitchReason;
	}
}
