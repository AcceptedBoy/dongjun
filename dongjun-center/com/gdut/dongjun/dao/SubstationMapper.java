package com.gdut.dongjun.dao;

import com.gdut.dongjun.po.Substation;

public interface SubstationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table substation
     *
     * @mbggenerated Thu May 25 17:25:15 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table substation
     *
     * @mbggenerated Thu May 25 17:25:15 CST 2017
     */
    int insert(Substation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table substation
     *
     * @mbggenerated Thu May 25 17:25:15 CST 2017
     */
    int insertSelective(Substation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table substation
     *
     * @mbggenerated Thu May 25 17:25:15 CST 2017
     */
    Substation selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table substation
     *
     * @mbggenerated Thu May 25 17:25:15 CST 2017
     */
    int updateByPrimaryKeySelective(Substation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table substation
     *
     * @mbggenerated Thu May 25 17:25:15 CST 2017
     */
    int updateByPrimaryKey(Substation record);
}