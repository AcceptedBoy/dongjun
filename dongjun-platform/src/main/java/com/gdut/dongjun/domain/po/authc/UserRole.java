package com.gdut.dongjun.domain.po.authc;

import java.util.Date;

import com.gdut.dongjun.domain.po.AbstractBean;

public class UserRole extends AbstractBean {

	public UserRole() {
		super();
	}

	public UserRole(String userId, String roleId) {
		super();
		this.userId = userId;
		this.roleId = roleId;
	}

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_role.user_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private String userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_role.role_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private String roleId;
    
    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_role.user_id
     *
     * @return the value of user_role.user_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_role.user_id
     *
     * @param userId the value for user_role.user_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_role.role_id
     *
     * @return the value of user_role.role_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_role.role_id
     *
     * @param roleId the value for user_role.role_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
	
    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_role.gmt_create
     *
     * @return the value of user_role.gmt_create
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_role.gmt_create
     *
     * @param gmtCreate the value for user_role.gmt_create
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_role.gmt_modified
     *
     * @return the value of user_role.gmt_modified
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_role.gmt_modified
     *
     * @param gmtModified the value for user_role.gmt_modified
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}