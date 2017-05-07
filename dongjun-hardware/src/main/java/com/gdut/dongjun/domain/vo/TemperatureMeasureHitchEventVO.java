package com.gdut.dongjun.domain.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.gdut.dongjun.domain.po.abstractmodel.AbstractHitchEvent;

public class TemperatureMeasureHitchEventVO extends AbstractHitchEvent {

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure_hitch_event1.hitch_id
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    private String hitchId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure_hitch_event1.tag
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    private Integer tag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure_hitch_event1.value
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    private BigDecimal value;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure_hitch_event1.max_hitch_value
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    private BigDecimal maxHitchValue;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure_hitch_event1.min_hitch_value
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    private BigDecimal minHitchValue;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_measure_hitch_event1.id
     *
     * @return the value of temperature_measure_hitch_event1.id
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_measure_hitch_event1.id
     *
     * @param id the value for temperature_measure_hitch_event1.id
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_measure_hitch_event1.hitch_id
     *
     * @return the value of temperature_measure_hitch_event1.hitch_id
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public String getHitchId() {
        return hitchId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_measure_hitch_event1.hitch_id
     *
     * @param hitchId the value for temperature_measure_hitch_event1.hitch_id
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public void setHitchId(String hitchId) {
        this.hitchId = hitchId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_measure_hitch_event1.tag
     *
     * @return the value of temperature_measure_hitch_event1.tag
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public Integer getTag() {
        return tag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_measure_hitch_event1.tag
     *
     * @param tag the value for temperature_measure_hitch_event1.tag
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public void setTag(Integer tag) {
        this.tag = tag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_measure_hitch_event1.value
     *
     * @return the value of temperature_measure_hitch_event1.value
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_measure_hitch_event1.value
     *
     * @param value the value for temperature_measure_hitch_event1.value
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_measure_hitch_event1.max_hitch_value
     *
     * @return the value of temperature_measure_hitch_event1.max_hitch_value
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public BigDecimal getMaxHitchValue() {
        return maxHitchValue;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_measure_hitch_event1.max_hitch_value
     *
     * @param maxHitchValue the value for temperature_measure_hitch_event1.max_hitch_value
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public void setMaxHitchValue(BigDecimal maxHitchValue) {
        this.maxHitchValue = maxHitchValue;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_measure_hitch_event1.min_hitch_value
     *
     * @return the value of temperature_measure_hitch_event1.min_hitch_value
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public BigDecimal getMinHitchValue() {
        return minHitchValue;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_measure_hitch_event1.min_hitch_value
     *
     * @param minHitchValue the value for temperature_measure_hitch_event1.min_hitch_value
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public void setMinHitchValue(BigDecimal minHitchValue) {
        this.minHitchValue = minHitchValue;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_measure_hitch_event1.gmt_create
     *
     * @return the value of temperature_measure_hitch_event1.gmt_create
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_measure_hitch_event1.gmt_create
     *
     * @param gmtCreate the value for temperature_measure_hitch_event1.gmt_create
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_measure_hitch_event1.gmt_modified
     *
     * @return the value of temperature_measure_hitch_event1.gmt_modified
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_measure_hitch_event1.gmt_modified
     *
     * @param gmtModified the value for temperature_measure_hitch_event1.gmt_modified
     *
     * @mbggenerated Fri Apr 21 20:09:38 CST 2017
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}
