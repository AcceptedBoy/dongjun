package com.gdut.dongjun.domain.po;

import java.util.Date;

public class UserLog {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_log.id
     *
     * @mbggenerated Tue Jan 10 21:01:25 CST 2017
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_log.user_id
     *
     * @mbggenerated Tue Jan 10 21:01:25 CST 2017
     */
    private String userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_log.type
     *
     * @mbggenerated Tue Jan 10 21:01:25 CST 2017
     */
    private Integer type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_log.date
     *
     * @mbggenerated Tue Jan 10 21:01:25 CST 2017
     */
    private Date date;
    
    private String companyId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_log.id
     *
     * @return the value of user_log.id
     *
     * @mbggenerated Tue Jan 10 21:01:25 CST 2017
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_log.id
     *
     * @param id the value for user_log.id
     *
     * @mbggenerated Tue Jan 10 21:01:25 CST 2017
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_log.user_id
     *
     * @return the value of user_log.user_id
     *
     * @mbggenerated Tue Jan 10 21:01:25 CST 2017
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_log.user_id
     *
     * @param userId the value for user_log.user_id
     *
     * @mbggenerated Tue Jan 10 21:01:25 CST 2017
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_log.type
     *
     * @return the value of user_log.type
     *
     * @mbggenerated Tue Jan 10 21:01:25 CST 2017
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_log.type
     *
     * @param type the value for user_log.type
     *
     * @mbggenerated Tue Jan 10 21:01:25 CST 2017
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_log.date
     *
     * @return the value of user_log.date
     *
     * @mbggenerated Tue Jan 10 21:01:25 CST 2017
     */
    public Date getDate() {
        return date;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_log.date
     *
     * @param date the value for user_log.date
     *
     * @mbggenerated Tue Jan 10 21:01:25 CST 2017
     */
    public void setDate(Date date) {
        this.date = date;
    }

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
}