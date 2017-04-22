package com.gdut.dongjun.domain.po;

import java.math.BigDecimal;
import java.util.Date;

import com.gdut.dongjun.domain.po.abstractmodel.AbstractBean;

public class ElectronicModuleVoltage extends AbstractBean {
	
    public ElectronicModuleVoltage() {
		super();
	}

	public ElectronicModuleVoltage(String id, String phase, BigDecimal value, Date time, String groupId) {
		super();
		this.id = id;
		this.phase = phase;
		this.value = value;
		this.time = time;
		this.groupId = groupId;
	}

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electronic_module_voltage.id
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electronic_module_voltage.phase
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    private String phase;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electronic_module_voltage.value
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    private BigDecimal value;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electronic_module_voltage.time
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    private Date time;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electronic_module_voltage.group_id
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    private String groupId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electronic_module_voltage.id
     *
     * @return the value of electronic_module_voltage.id
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electronic_module_voltage.id
     *
     * @param id the value for electronic_module_voltage.id
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electronic_module_voltage.phase
     *
     * @return the value of electronic_module_voltage.phase
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    public String getPhase() {
        return phase;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electronic_module_voltage.phase
     *
     * @param phase the value for electronic_module_voltage.phase
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    public void setPhase(String phase) {
        this.phase = phase;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electronic_module_voltage.value
     *
     * @return the value of electronic_module_voltage.value
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electronic_module_voltage.value
     *
     * @param value the value for electronic_module_voltage.value
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electronic_module_voltage.time
     *
     * @return the value of electronic_module_voltage.time
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    public Date getTime() {
        return time;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electronic_module_voltage.time
     *
     * @param time the value for electronic_module_voltage.time
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electronic_module_voltage.group_id
     *
     * @return the value of electronic_module_voltage.group_id
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electronic_module_voltage.group_id
     *
     * @param groupId the value for electronic_module_voltage.group_id
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electronic_module_voltage.gmt_create
     *
     * @return the value of electronic_module_voltage.gmt_create
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electronic_module_voltage.gmt_create
     *
     * @param gmtCreate the value for electronic_module_voltage.gmt_create
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electronic_module_voltage.gmt_modified
     *
     * @return the value of electronic_module_voltage.gmt_modified
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electronic_module_voltage.gmt_modified
     *
     * @param gmtModified the value for electronic_module_voltage.gmt_modified
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}