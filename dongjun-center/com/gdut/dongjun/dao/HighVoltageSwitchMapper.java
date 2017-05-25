package com.gdut.dongjun.dao;

import com.gdut.dongjun.po.HighVoltageSwitch;

public interface HighVoltageSwitchMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table high_voltage_switch
     *
     * @mbggenerated Thu May 25 17:25:15 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table high_voltage_switch
     *
     * @mbggenerated Thu May 25 17:25:15 CST 2017
     */
    int insert(HighVoltageSwitch record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table high_voltage_switch
     *
     * @mbggenerated Thu May 25 17:25:15 CST 2017
     */
    int insertSelective(HighVoltageSwitch record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table high_voltage_switch
     *
     * @mbggenerated Thu May 25 17:25:15 CST 2017
     */
    HighVoltageSwitch selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table high_voltage_switch
     *
     * @mbggenerated Thu May 25 17:25:15 CST 2017
     */
    int updateByPrimaryKeySelective(HighVoltageSwitch record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table high_voltage_switch
     *
     * @mbggenerated Thu May 25 17:25:15 CST 2017
     */
    int updateByPrimaryKey(HighVoltageSwitch record);
}