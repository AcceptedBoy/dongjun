package com.gdut.dongjun.domain.dao;

import com.gdut.dongjun.domain.po.ElectronicModuleVoltage;

public interface ElectronicModuleVoltageMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electronic_module_voltage
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electronic_module_voltage
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    int insert(ElectronicModuleVoltage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electronic_module_voltage
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    int insertSelective(ElectronicModuleVoltage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electronic_module_voltage
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    ElectronicModuleVoltage selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electronic_module_voltage
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    int updateByPrimaryKeySelective(ElectronicModuleVoltage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electronic_module_voltage
     *
     * @mbggenerated Wed Apr 12 13:30:24 CST 2017
     */
    int updateByPrimaryKey(ElectronicModuleVoltage record);
}