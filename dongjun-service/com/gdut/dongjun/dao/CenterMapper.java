package com.gdut.dongjun.dao;

import com.gdut.dongjun.po.Center;

public interface CenterMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table center
     *
     * @mbggenerated Thu Jul 13 16:20:08 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table center
     *
     * @mbggenerated Thu Jul 13 16:20:08 CST 2017
     */
    int insert(Center record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table center
     *
     * @mbggenerated Thu Jul 13 16:20:08 CST 2017
     */
    int insertSelective(Center record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table center
     *
     * @mbggenerated Thu Jul 13 16:20:08 CST 2017
     */
    Center selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table center
     *
     * @mbggenerated Thu Jul 13 16:20:08 CST 2017
     */
    int updateByPrimaryKeySelective(Center record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table center
     *
     * @mbggenerated Thu Jul 13 16:20:08 CST 2017
     */
    int updateByPrimaryKey(Center record);
}