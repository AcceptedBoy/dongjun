package com.gdut.dongjun.domain.dao;

import com.gdut.dongjun.domain.po.TemperatureDevice;

public interface TemperatureDeviceMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table temperature_device
     *
     * @mbggenerated Wed Mar 01 22:07:28 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table temperature_device
     *
     * @mbggenerated Wed Mar 01 22:07:28 CST 2017
     */
    int insert(TemperatureDevice record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table temperature_device
     *
     * @mbggenerated Wed Mar 01 22:07:28 CST 2017
     */
    int insertSelective(TemperatureDevice record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table temperature_device
     *
     * @mbggenerated Wed Mar 01 22:07:28 CST 2017
     */
    TemperatureDevice selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table temperature_device
     *
     * @mbggenerated Wed Mar 01 22:07:28 CST 2017
     */
    int updateByPrimaryKeySelective(TemperatureDevice record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table temperature_device
     *
     * @mbggenerated Wed Mar 01 22:07:28 CST 2017
     */
    int updateByPrimaryKey(TemperatureDevice record);
}