package com.gdut.dongjun.domain.po;

import java.util.Date;

public class DeviceGroup extends AbstractBean {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column device_group.id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column device_group.name
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column device_group.platform_group_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private String platformGroupId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column device_group.id
     *
     * @return the value of device_group.id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column device_group.id
     *
     * @param id the value for device_group.id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column device_group.name
     *
     * @return the value of device_group.name
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column device_group.name
     *
     * @param name the value for device_group.name
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column device_group.platform_group_id
     *
     * @return the value of device_group.platform_group_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public String getPlatformGroupId() {
        return platformGroupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column device_group.platform_group_id
     *
     * @param platformGroupId the value for device_group.platform_group_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setPlatformGroupId(String platformGroupId) {
        this.platformGroupId = platformGroupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column device_group.gmt_create
     *
     * @return the value of device_group.gmt_create
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column device_group.gmt_create
     *
     * @param gmtCreate the value for device_group.gmt_create
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column device_group.gmt_modified
     *
     * @return the value of device_group.gmt_modified
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column device_group.gmt_modified
     *
     * @param gmtModified the value for device_group.gmt_modified
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}