package com.gdut.dongjun.domain.po;

import java.util.Date;

public class TemperatureMeasure {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure.id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure.device_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private String deviceId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure.date
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private Date date;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure.tag
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private Integer tag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure.value
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private String value;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure.gmt_create
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private Date gmtCreate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure.gmt_modified
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private Date gmtModified;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_measure.id
     *
     * @return the value of temperature_measure.id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_measure.id
     *
     * @param id the value for temperature_measure.id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_measure.device_id
     *
     * @return the value of temperature_measure.device_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_measure.device_id
     *
     * @param deviceId the value for temperature_measure.device_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_measure.date
     *
     * @return the value of temperature_measure.date
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public Date getDate() {
        return date;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_measure.date
     *
     * @param date the value for temperature_measure.date
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_measure.tag
     *
     * @return the value of temperature_measure.tag
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public Integer getTag() {
        return tag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_measure.tag
     *
     * @param tag the value for temperature_measure.tag
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setTag(Integer tag) {
        this.tag = tag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_measure.value
     *
     * @return the value of temperature_measure.value
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public String getValue() {
        return value;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_measure.value
     *
     * @param value the value for temperature_measure.value
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_measure.gmt_create
     *
     * @return the value of temperature_measure.gmt_create
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_measure.gmt_create
     *
     * @param gmtCreate the value for temperature_measure.gmt_create
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_measure.gmt_modified
     *
     * @return the value of temperature_measure.gmt_modified
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_measure.gmt_modified
     *
     * @param gmtModified the value for temperature_measure.gmt_modified
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}