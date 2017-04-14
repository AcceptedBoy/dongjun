package com.gdut.dongjun.domain.po;

import java.util.Date;

public class BigGroup {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column big_group.id
     *
     * @mbggenerated Thu Apr 13 22:18:34 CST 2017
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column big_group.name
     *
     * @mbggenerated Thu Apr 13 22:18:34 CST 2017
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column big_group.is_default
     *
     * @mbggenerated Thu Apr 13 22:18:34 CST 2017
     */
    private Integer isDefault;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column big_group.gmt_create
     *
     * @mbggenerated Thu Apr 13 22:18:34 CST 2017
     */
    private Date gmtCreate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column big_group.gmt_modified
     *
     * @mbggenerated Thu Apr 13 22:18:34 CST 2017
     */
    private Date gmtModified;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column big_group.id
     *
     * @return the value of big_group.id
     *
     * @mbggenerated Thu Apr 13 22:18:34 CST 2017
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column big_group.id
     *
     * @param id the value for big_group.id
     *
     * @mbggenerated Thu Apr 13 22:18:34 CST 2017
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column big_group.name
     *
     * @return the value of big_group.name
     *
     * @mbggenerated Thu Apr 13 22:18:34 CST 2017
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column big_group.name
     *
     * @param name the value for big_group.name
     *
     * @mbggenerated Thu Apr 13 22:18:34 CST 2017
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column big_group.is_default
     *
     * @return the value of big_group.is_default
     *
     * @mbggenerated Thu Apr 13 22:18:34 CST 2017
     */
    public Integer getIsDefault() {
        return isDefault;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column big_group.is_default
     *
     * @param isDefault the value for big_group.is_default
     *
     * @mbggenerated Thu Apr 13 22:18:34 CST 2017
     */
    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column big_group.gmt_create
     *
     * @return the value of big_group.gmt_create
     *
     * @mbggenerated Thu Apr 13 22:18:34 CST 2017
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column big_group.gmt_create
     *
     * @param gmtCreate the value for big_group.gmt_create
     *
     * @mbggenerated Thu Apr 13 22:18:34 CST 2017
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column big_group.gmt_modified
     *
     * @return the value of big_group.gmt_modified
     *
     * @mbggenerated Thu Apr 13 22:18:34 CST 2017
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column big_group.gmt_modified
     *
     * @param gmtModified the value for big_group.gmt_modified
     *
     * @mbggenerated Thu Apr 13 22:18:34 CST 2017
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}