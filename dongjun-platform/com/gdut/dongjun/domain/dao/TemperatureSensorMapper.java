package com.gdut.dongjun.domain.dao;

import com.gdut.dongjun.domain.po.TemperatureSensor;

public interface TemperatureSensorMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table temperature_sensor
     *
     * @mbggenerated Sat Jul 29 15:30:24 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table temperature_sensor
     *
     * @mbggenerated Sat Jul 29 15:30:24 CST 2017
     */
    int insert(TemperatureSensor record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table temperature_sensor
     *
     * @mbggenerated Sat Jul 29 15:30:24 CST 2017
     */
    int insertSelective(TemperatureSensor record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table temperature_sensor
     *
     * @mbggenerated Sat Jul 29 15:30:24 CST 2017
     */
    TemperatureSensor selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table temperature_sensor
     *
     * @mbggenerated Sat Jul 29 15:30:24 CST 2017
     */
    int updateByPrimaryKeySelective(TemperatureSensor record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table temperature_sensor
     *
     * @mbggenerated Sat Jul 29 15:30:24 CST 2017
     */
    int updateByPrimaryKey(TemperatureSensor record);
}