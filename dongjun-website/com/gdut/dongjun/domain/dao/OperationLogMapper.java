package com.gdut.dongjun.domain.dao;

import com.gdut.dongjun.domain.po.OperationLog;

public interface OperationLogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table operation_log
     *
     * @mbggenerated Tue Jan 10 21:04:33 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table operation_log
     *
     * @mbggenerated Tue Jan 10 21:04:33 CST 2017
     */
    int insert(OperationLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table operation_log
     *
     * @mbggenerated Tue Jan 10 21:04:33 CST 2017
     */
    int insertSelective(OperationLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table operation_log
     *
     * @mbggenerated Tue Jan 10 21:04:33 CST 2017
     */
    OperationLog selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table operation_log
     *
     * @mbggenerated Tue Jan 10 21:04:33 CST 2017
     */
    int updateByPrimaryKeySelective(OperationLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table operation_log
     *
     * @mbggenerated Tue Jan 10 21:04:33 CST 2017
     */
    int updateByPrimaryKey(OperationLog record);
}