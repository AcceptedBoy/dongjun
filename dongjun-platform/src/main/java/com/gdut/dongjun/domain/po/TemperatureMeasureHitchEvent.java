package com.gdut.dongjun.domain.po;

import java.math.BigDecimal;
import java.util.Date;

import com.gdut.dongjun.domain.po.abstractmodel.AbstractHitchEvent;

public class TemperatureMeasureHitchEvent extends AbstractHitchEvent {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure_hitch_event.id
     *
     * @mbggenerated Fri Mar 10 17:05:29 CST 2017
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure_hitch_event.switch_id
     *
     * @mbggenerated Fri Mar 10 17:05:29 CST 2017
     */
    private String switchId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure_hitch_event.value
     *
     * @mbggenerated Fri Mar 10 17:05:29 CST 2017
     */
    private BigDecimal value;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure_hitch_event.tag
     *
     * @mbggenerated Fri Mar 10 17:05:29 CST 2017
     */
    private Integer tag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure_hitch_event.hitch_reason
     *
     * @mbggenerated Fri Mar 10 17:05:29 CST 2017
     */
    private String hitchReason;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure_hitch_event.hitch_time
     *
     * @mbggenerated Fri Mar 10 17:05:29 CST 2017
     */
    private String hitchTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure_hitch_event.group_id
     *
     * @mbggenerated Fri Mar 10 17:05:29 CST 2017
     */
    private String groupId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure_hitch_event.max_hitch_value
     *
     * @mbggenerated Fri Mar 10 17:05:29 CST 2017
     */
    private BigDecimal maxHitchValue;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure_hitch_event.min_hitch_value
     *
     * @mbggenerated Fri Mar 10 17:05:29 CST 2017
     */
    private BigDecimal minHitchValue;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure_hitch_event.gmt_create
     *
     * @mbggenerated Fri Mar 10 17:05:29 CST 2017
     */
    private Date gmtCreate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure_hitch_event.gmt_modified
     *
     * @mbggenerated Fri Mar 10 17:05:29 CST 2017
     */
    private Date gmtModified;

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

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Integer getTag() {
		return tag;
	}

	public void setTag(Integer tag) {
		this.tag = tag;
	}

	public String getHitchReason() {
		return hitchReason;
	}

	public void setHitchReason(String hitchReason) {
		this.hitchReason = hitchReason;
	}

	public String getHitchTime() {
		return hitchTime;
	}

	public void setHitchTime(String hitchTime) {
		this.hitchTime = hitchTime;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public BigDecimal getMaxHitchValue() {
		return maxHitchValue;
	}

	public void setMaxHitchValue(BigDecimal maxHitchValue) {
		this.maxHitchValue = maxHitchValue;
	}

	public BigDecimal getMinHitchValue() {
		return minHitchValue;
	}

	public void setMinHitchValue(BigDecimal minHitchValue) {
		this.minHitchValue = minHitchValue;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}
}