package com.gdut.dongjun.domain.dao;

import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;

public interface TemperatureMeasureHitchEventMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table temperature_measure_hitch_event1
     *
     * @mbggenerated Thu Apr 20 16:31:39 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table temperature_measure_hitch_event1
     *
     * @mbggenerated Thu Apr 20 16:31:39 CST 2017
     */
    int insert(TemperatureMeasureHitchEvent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table temperature_measure_hitch_event1
     *
     * @mbggenerated Thu Apr 20 16:31:39 CST 2017
     */
    int insertSelective(TemperatureMeasureHitchEvent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table temperature_measure_hitch_event1
     *
     * @mbggenerated Thu Apr 20 16:31:39 CST 2017
     */
    TemperatureMeasureHitchEvent selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table temperature_measure_hitch_event1
     *
     * @mbggenerated Thu Apr 20 16:31:39 CST 2017
     */
    int updateByPrimaryKeySelective(TemperatureMeasureHitchEvent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table temperature_measure_hitch_event1
     *
     * @mbggenerated Thu Apr 20 16:31:39 CST 2017
     */
    int updateByPrimaryKey(TemperatureMeasureHitchEvent record);
}