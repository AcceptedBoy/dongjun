package com.gdut.dongjun.domain.dao;

import com.gdut.dongjun.domain.po.ElectronicModuleHitchEvent;

public interface ElectronicModuleHitchEventMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electronic_module_hitch_event
     *
     * @mbggenerated Wed May 03 17:23:26 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electronic_module_hitch_event
     *
     * @mbggenerated Wed May 03 17:23:26 CST 2017
     */
    int insert(ElectronicModuleHitchEvent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electronic_module_hitch_event
     *
     * @mbggenerated Wed May 03 17:23:26 CST 2017
     */
    int insertSelective(ElectronicModuleHitchEvent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electronic_module_hitch_event
     *
     * @mbggenerated Wed May 03 17:23:26 CST 2017
     */
    ElectronicModuleHitchEvent selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electronic_module_hitch_event
     *
     * @mbggenerated Wed May 03 17:23:26 CST 2017
     */
    int updateByPrimaryKeySelective(ElectronicModuleHitchEvent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electronic_module_hitch_event
     *
     * @mbggenerated Wed May 03 17:23:26 CST 2017
     */
    int updateByPrimaryKey(ElectronicModuleHitchEvent record);
}