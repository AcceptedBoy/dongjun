package com.gdut.dongjun.domain.dao;

import com.gdut.dongjun.domain.po.ElectronicModuleCurrent;

public interface ElectronicModuleCurrentMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electronic_module_current
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electronic_module_current
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    int insert(ElectronicModuleCurrent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electronic_module_current
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    int insertSelective(ElectronicModuleCurrent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electronic_module_current
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    ElectronicModuleCurrent selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electronic_module_current
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    int updateByPrimaryKeySelective(ElectronicModuleCurrent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electronic_module_current
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    int updateByPrimaryKey(ElectronicModuleCurrent record);
}