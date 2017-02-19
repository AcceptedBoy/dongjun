package com.gdut.dongjun.domain.dao;

import com.gdut.dongjun.domain.po.HighVoltageSwitch;

public interface HighVoltageSwitchMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table high_voltage_switch
     *
     * @mbggenerated Sun Feb 19 12:54:09 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table high_voltage_switch
     *
     * @mbggenerated Sun Feb 19 12:54:09 CST 2017
     */
    int insert(HighVoltageSwitch record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table high_voltage_switch
     *
     * @mbggenerated Sun Feb 19 12:54:09 CST 2017
     */
    int insertSelective(HighVoltageSwitch record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table high_voltage_switch
     *
     * @mbggenerated Sun Feb 19 12:54:09 CST 2017
     */
    HighVoltageSwitch selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table high_voltage_switch
     *
     * @mbggenerated Sun Feb 19 12:54:09 CST 2017
     */
    int updateByPrimaryKeySelective(HighVoltageSwitch record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table high_voltage_switch
     *
     * @mbggenerated Sun Feb 19 12:54:09 CST 2017
     */
    int updateByPrimaryKey(HighVoltageSwitch record);
}