package com.gdut.dongjun.domain.dao;

import com.gdut.dongjun.domain.po.PersistentHitchMessage;

public interface PersistentHitchMessageMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table persistent_hitch_message
     *
     * @mbggenerated Mon Mar 27 21:43:40 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table persistent_hitch_message
     *
     * @mbggenerated Mon Mar 27 21:43:40 CST 2017
     */
    int insert(PersistentHitchMessage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table persistent_hitch_message
     *
     * @mbggenerated Mon Mar 27 21:43:40 CST 2017
     */
    int insertSelective(PersistentHitchMessage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table persistent_hitch_message
     *
     * @mbggenerated Mon Mar 27 21:43:40 CST 2017
     */
    PersistentHitchMessage selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table persistent_hitch_message
     *
     * @mbggenerated Mon Mar 27 21:43:40 CST 2017
     */
    int updateByPrimaryKeySelective(PersistentHitchMessage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table persistent_hitch_message
     *
     * @mbggenerated Mon Mar 27 21:43:40 CST 2017
     */
    int updateByPrimaryKey(PersistentHitchMessage record);
}