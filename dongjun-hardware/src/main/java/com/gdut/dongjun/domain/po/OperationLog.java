package com.gdut.dongjun.domain.po;

import java.util.Date;

public class OperationLog {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column operation_log.id
     *
     * @mbggenerated Sat Feb 18 14:48:10 CST 2017
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column operation_log.user_id
     *
     * @mbggenerated Sat Feb 18 14:48:10 CST 2017
     */
    private String userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column operation_log.type
     *
     * @mbggenerated Sat Feb 18 14:48:10 CST 2017
     */
    private Integer type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column operation_log.switch_id
     *
     * @mbggenerated Sat Feb 18 14:48:10 CST 2017
     */
    private String switchId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column operation_log.date
     *
     * @mbggenerated Sat Feb 18 14:48:10 CST 2017
     */
    private Date date;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column operation_log.company_id
     *
     * @mbggenerated Sat Feb 18 14:48:10 CST 2017
     */
    private String companyId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column operation_log.id
     *
     * @return the value of operation_log.id
     *
     * @mbggenerated Sat Feb 18 14:48:10 CST 2017
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column operation_log.id
     *
     * @param id the value for operation_log.id
     *
     * @mbggenerated Sat Feb 18 14:48:10 CST 2017
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column operation_log.user_id
     *
     * @return the value of operation_log.user_id
     *
     * @mbggenerated Sat Feb 18 14:48:10 CST 2017
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column operation_log.user_id
     *
     * @param userId the value for operation_log.user_id
     *
     * @mbggenerated Sat Feb 18 14:48:10 CST 2017
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column operation_log.type
     *
     * @return the value of operation_log.type
     *
     * @mbggenerated Sat Feb 18 14:48:10 CST 2017
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column operation_log.type
     *
     * @param type the value for operation_log.type
     *
     * @mbggenerated Sat Feb 18 14:48:10 CST 2017
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column operation_log.switch_id
     *
     * @return the value of operation_log.switch_id
     *
     * @mbggenerated Sat Feb 18 14:48:10 CST 2017
     */
    public String getSwitchId() {
        return switchId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column operation_log.switch_id
     *
     * @param switchId the value for operation_log.switch_id
     *
     * @mbggenerated Sat Feb 18 14:48:10 CST 2017
     */
    public void setSwitchId(String switchId) {
        this.switchId = switchId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column operation_log.date
     *
     * @return the value of operation_log.date
     *
     * @mbggenerated Sat Feb 18 14:48:10 CST 2017
     */
    public Date getDate() {
        return date;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column operation_log.date
     *
     * @param date the value for operation_log.date
     *
     * @mbggenerated Sat Feb 18 14:48:10 CST 2017
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column operation_log.company_id
     *
     * @return the value of operation_log.company_id
     *
     * @mbggenerated Sat Feb 18 14:48:10 CST 2017
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column operation_log.company_id
     *
     * @param companyId the value for operation_log.company_id
     *
     * @mbggenerated Sat Feb 18 14:48:10 CST 2017
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}