package com.gdut.dongjun.domain.dao;

import com.gdut.dongjun.domain.po.BigGroup;

public interface BigGroupMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_group
     *
     * @mbggenerated Sun Feb 19 12:54:09 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_group
     *
     * @mbggenerated Sun Feb 19 12:54:09 CST 2017
     */
    int insert(BigGroup record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_group
     *
     * @mbggenerated Sun Feb 19 12:54:09 CST 2017
     */
    int insertSelective(BigGroup record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_group
     *
     * @mbggenerated Sun Feb 19 12:54:09 CST 2017
     */
    BigGroup selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_group
     *
     * @mbggenerated Sun Feb 19 12:54:09 CST 2017
     */
    int updateByPrimaryKeySelective(BigGroup record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_group
     *
     * @mbggenerated Sun Feb 19 12:54:09 CST 2017
     */
    int updateByPrimaryKey(BigGroup record);
}