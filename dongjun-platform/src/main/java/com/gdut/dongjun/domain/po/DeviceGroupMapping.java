package com.gdut.dongjun.domain.po;

import java.util.Date;

public class DeviceGroupMapping extends AbstractBean {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column device_group_mapping.id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column device_group_mapping.device_group_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private String deviceGroupId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column device_group_mapping.device_id
     * DataMonitorId
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private String deviceId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column device_group_mapping.type
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private Integer type;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column device_group_mapping.id
     *
     * @return the value of device_group_mapping.id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column device_group_mapping.id
     *
     * @param id the value for device_group_mapping.id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column device_group_mapping.device_group_id
     *
     * @return the value of device_group_mapping.device_group_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public String getDeviceGroupId() {
        return deviceGroupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column device_group_mapping.device_group_id
     *
     * @param deviceGroupId the value for device_group_mapping.device_group_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setDeviceGroupId(String deviceGroupId) {
        this.deviceGroupId = deviceGroupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column device_group_mapping.device_id
     *
     * @return the value of device_group_mapping.device_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column device_group_mapping.device_id
     *
     * @param deviceId the value for device_group_mapping.device_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column device_group_mapping.type
     *
     * @return the value of device_group_mapping.type
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column device_group_mapping.type
     *
     * @param type the value for device_group_mapping.type
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column device_group_mapping.gmt_create
     *
     * @return the value of device_group_mapping.gmt_create
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column device_group_mapping.gmt_create
     *
     * @param gmtCreate the value for device_group_mapping.gmt_create
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column device_group_mapping.gmt_modified
     *
     * @return the value of device_group_mapping.gmt_modified
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column device_group_mapping.gmt_modified
     *
     * @param gmtModified the value for device_group_mapping.gmt_modified
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}