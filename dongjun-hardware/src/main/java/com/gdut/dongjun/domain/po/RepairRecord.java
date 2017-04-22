package com.gdut.dongjun.domain.po;

import java.util.Date;

import com.gdut.dongjun.domain.po.abstractmodel.AbstractBean;

public class RepairRecord extends AbstractBean {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_record.id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_record.device_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private String deviceId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_record.type
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private Integer type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_record.time
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private Date time;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_record.reason
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private String reason;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_record.description
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private String description;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_record.solve_man
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private String solveMan;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_record.user_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private String userId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_record.id
     *
     * @return the value of repair_record.id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_record.id
     *
     * @param id the value for repair_record.id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_record.device_id
     *
     * @return the value of repair_record.device_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_record.device_id
     *
     * @param deviceId the value for repair_record.device_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_record.type
     *
     * @return the value of repair_record.type
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_record.type
     *
     * @param type the value for repair_record.type
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_record.time
     *
     * @return the value of repair_record.time
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public Date getTime() {
        return time;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_record.time
     *
     * @param time the value for repair_record.time
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_record.reason
     *
     * @return the value of repair_record.reason
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public String getReason() {
        return reason;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_record.reason
     *
     * @param reason the value for repair_record.reason
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_record.description
     *
     * @return the value of repair_record.description
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_record.description
     *
     * @param description the value for repair_record.description
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_record.solve_man
     *
     * @return the value of repair_record.solve_man
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public String getSolveMan() {
        return solveMan;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_record.solve_man
     *
     * @param solveMan the value for repair_record.solve_man
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setSolveMan(String solveMan) {
        this.solveMan = solveMan;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_record.user_id
     *
     * @return the value of repair_record.user_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_record.user_id
     *
     * @param userId the value for repair_record.user_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_record.gmt_create
     *
     * @return the value of repair_record.gmt_create
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_record.gmt_create
     *
     * @param gmtCreate the value for repair_record.gmt_create
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_record.gmt_modified
     *
     * @return the value of repair_record.gmt_modified
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_record.gmt_modified
     *
     * @param gmtModified the value for repair_record.gmt_modified
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}