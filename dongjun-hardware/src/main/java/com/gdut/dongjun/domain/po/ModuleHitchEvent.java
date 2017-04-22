package com.gdut.dongjun.domain.po;

import java.util.Date;

import com.gdut.dongjun.domain.po.abstractmodel.AbstractBean;

public class ModuleHitchEvent extends AbstractBean {
	
    public ModuleHitchEvent() {
		super();
	}

	public ModuleHitchEvent(String id, String moduleId, String monitorId, String groupId, String hitchReason,
			Date hitchTime, Integer type) {
		super();
		this.id = id;
		this.moduleId = moduleId;
		this.monitorId = monitorId;
		this.groupId = groupId;
		this.hitchReason = hitchReason;
		this.hitchTime = hitchTime;
		this.type = type;
	}

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column module_hitch_event.id
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column module_hitch_event.module_id
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    private String moduleId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column module_hitch_event.monitor_id
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    private String monitorId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column module_hitch_event.group_id
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    private String groupId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column module_hitch_event.hitch_reason
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    private String hitchReason;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column module_hitch_event.hitch_time
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    private Date hitchTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column module_hitch_event.type
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    private Integer type;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column module_hitch_event.id
     *
     * @return the value of module_hitch_event.id
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column module_hitch_event.id
     *
     * @param id the value for module_hitch_event.id
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column module_hitch_event.module_id
     *
     * @return the value of module_hitch_event.module_id
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public String getModuleId() {
        return moduleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column module_hitch_event.module_id
     *
     * @param moduleId the value for module_hitch_event.module_id
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column module_hitch_event.monitor_id
     *
     * @return the value of module_hitch_event.monitor_id
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public String getMonitorId() {
        return monitorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column module_hitch_event.monitor_id
     *
     * @param monitorId the value for module_hitch_event.monitor_id
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column module_hitch_event.group_id
     *
     * @return the value of module_hitch_event.group_id
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column module_hitch_event.group_id
     *
     * @param groupId the value for module_hitch_event.group_id
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column module_hitch_event.hitch_reason
     *
     * @return the value of module_hitch_event.hitch_reason
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public String getHitchReason() {
        return hitchReason;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column module_hitch_event.hitch_reason
     *
     * @param hitchReason the value for module_hitch_event.hitch_reason
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public void setHitchReason(String hitchReason) {
        this.hitchReason = hitchReason;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column module_hitch_event.hitch_time
     *
     * @return the value of module_hitch_event.hitch_time
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public Date getHitchTime() {
        return hitchTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column module_hitch_event.hitch_time
     *
     * @param hitchTime the value for module_hitch_event.hitch_time
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public void setHitchTime(Date hitchTime) {
        this.hitchTime = hitchTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column module_hitch_event.type
     *
     * @return the value of module_hitch_event.type
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column module_hitch_event.type
     *
     * @param type the value for module_hitch_event.type
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column module_hitch_event.gmt_create
     *
     * @return the value of module_hitch_event.gmt_create
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column module_hitch_event.gmt_create
     *
     * @param gmtCreate the value for module_hitch_event.gmt_create
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column module_hitch_event.gmt_modified
     *
     * @return the value of module_hitch_event.gmt_modified
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column module_hitch_event.gmt_modified
     *
     * @param gmtModified the value for module_hitch_event.gmt_modified
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}