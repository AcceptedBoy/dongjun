package com.gdut.dongjun.domain.dao;

import com.gdut.dongjun.domain.po.ModuleInfoEvent;

public interface ModuleInfoEventMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table module_info_event
     *
     * @mbggenerated Mon Aug 07 15:59:07 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table module_info_event
     *
     * @mbggenerated Mon Aug 07 15:59:07 CST 2017
     */
    int insert(ModuleInfoEvent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table module_info_event
     *
     * @mbggenerated Mon Aug 07 15:59:07 CST 2017
     */
    int insertSelective(ModuleInfoEvent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table module_info_event
     *
     * @mbggenerated Mon Aug 07 15:59:07 CST 2017
     */
    ModuleInfoEvent selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table module_info_event
     *
     * @mbggenerated Mon Aug 07 15:59:07 CST 2017
     */
    int updateByPrimaryKeySelective(ModuleInfoEvent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table module_info_event
     *
     * @mbggenerated Mon Aug 07 15:59:07 CST 2017
     */
    int updateByPrimaryKey(ModuleInfoEvent record);
}