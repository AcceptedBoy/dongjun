package com.gdut.dongjun.domain.po;

import java.util.Date;

/**
 * type相关
 * 1进线A相
 * 2进线B相
 * 3进线C相
 * 4出线A相
 * 5出线B相
 * 6出线C相
 * @author Gordan_Deng
 * @date 2017年4月17日
 */
public class TemperatureSensor extends AbstractBean {

	public TemperatureSensor() {
		super();
	}

	public TemperatureSensor(String id, Integer tag, Integer type, String deviceId, String name) {
		super();
		this.id = id;
		this.tag = tag;
		this.type = type;
		this.deviceId = deviceId;
		this.name = name;
	}

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_sensor1.id
     *
     * @mbggenerated Mon Apr 17 15:46:51 CST 2017
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_sensor1.tag
     *
     * @mbggenerated Mon Apr 17 15:46:51 CST 2017
     */
    private Integer tag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_sensor1.type
     *
     * @mbggenerated Mon Apr 17 15:46:51 CST 2017
     */
    private Integer type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_sensor1.device_id
     *
     * @mbggenerated Mon Apr 17 15:46:51 CST 2017
     */
    private String deviceId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_sensor1.name
     *
     * @mbggenerated Mon Apr 17 15:46:51 CST 2017
     */
    private String name;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_sensor1.id
     *
     * @return the value of temperature_sensor1.id
     *
     * @mbggenerated Mon Apr 17 15:46:51 CST 2017
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_sensor1.id
     *
     * @param id the value for temperature_sensor1.id
     *
     * @mbggenerated Mon Apr 17 15:46:51 CST 2017
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_sensor1.tag
     *
     * @return the value of temperature_sensor1.tag
     *
     * @mbggenerated Mon Apr 17 15:46:51 CST 2017
     */
    public Integer getTag() {
        return tag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_sensor1.tag
     *
     * @param tag the value for temperature_sensor1.tag
     *
     * @mbggenerated Mon Apr 17 15:46:51 CST 2017
     */
    public void setTag(Integer tag) {
        this.tag = tag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_sensor1.type
     *
     * @return the value of temperature_sensor1.type
     *
     * @mbggenerated Mon Apr 17 15:46:51 CST 2017
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_sensor1.type
     *
     * @param type the value for temperature_sensor1.type
     *
     * @mbggenerated Mon Apr 17 15:46:51 CST 2017
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_sensor1.device_id
     *
     * @return the value of temperature_sensor1.device_id
     *
     * @mbggenerated Mon Apr 17 15:46:51 CST 2017
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_sensor1.device_id
     *
     * @param deviceId the value for temperature_sensor1.device_id
     *
     * @mbggenerated Mon Apr 17 15:46:51 CST 2017
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_sensor1.name
     *
     * @return the value of temperature_sensor1.name
     *
     * @mbggenerated Mon Apr 17 15:46:51 CST 2017
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_sensor1.name
     *
     * @param name the value for temperature_sensor1.name
     *
     * @mbggenerated Mon Apr 17 15:46:51 CST 2017
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_sensor1.gmt_create
     *
     * @return the value of temperature_sensor1.gmt_create
     *
     * @mbggenerated Mon Apr 17 15:46:51 CST 2017
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_sensor1.gmt_create
     *
     * @param gmtCreate the value for temperature_sensor1.gmt_create
     *
     * @mbggenerated Mon Apr 17 15:46:51 CST 2017
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_sensor1.gmt_modified
     *
     * @return the value of temperature_sensor1.gmt_modified
     *
     * @mbggenerated Mon Apr 17 15:46:51 CST 2017
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_sensor1.gmt_modified
     *
     * @param gmtModified the value for temperature_sensor1.gmt_modified
     *
     * @mbggenerated Mon Apr 17 15:46:51 CST 2017
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}