package com.gdut.dongjun.domain.dao;

import com.gdut.dongjun.domain.po.ModuleHitchEvent;

public interface ModuleHitchEventMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table module_hitch_event
     *
     * @mbggenerated Thu Apr 20 16:31:39 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table module_hitch_event
     *
     * @mbggenerated Thu Apr 20 16:31:39 CST 2017
     */
    int insert(ModuleHitchEvent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table module_hitch_event
     *
     * @mbggenerated Thu Apr 20 16:31:39 CST 2017
     */
    int insertSelective(ModuleHitchEvent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table module_hitch_event
     *
     * @mbggenerated Thu Apr 20 16:31:39 CST 2017
     */
    ModuleHitchEvent selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table module_hitch_event
     *
     * @mbggenerated Thu Apr 20 16:31:39 CST 2017
     */
    int updateByPrimaryKeySelective(ModuleHitchEvent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table module_hitch_event
     *
     * @mbggenerated Thu Apr 20 16:31:39 CST 2017
     */
    int updateByPrimaryKey(ModuleHitchEvent record);
}