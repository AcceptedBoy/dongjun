package com.gdut.dongjun.domain.dao;

import com.gdut.dongjun.domain.po.GPRSModule;

public interface GPRSModuleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gprs_module
     *
     * @mbggenerated Sun Mar 19 20:22:25 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gprs_module
     *
     * @mbggenerated Sun Mar 19 20:22:25 CST 2017
     */
    int insert(GPRSModule record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gprs_module
     *
     * @mbggenerated Sun Mar 19 20:22:25 CST 2017
     */
    int insertSelective(GPRSModule record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gprs_module
     *
     * @mbggenerated Sun Mar 19 20:22:25 CST 2017
     */
    GPRSModule selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gprs_module
     *
     * @mbggenerated Sun Mar 19 20:22:25 CST 2017
     */
    int updateByPrimaryKeySelective(GPRSModule record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gprs_module
     *
     * @mbggenerated Sun Mar 19 20:22:25 CST 2017
     */
    int updateByPrimaryKey(GPRSModule record);
}