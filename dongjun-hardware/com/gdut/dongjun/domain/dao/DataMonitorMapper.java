package com.gdut.dongjun.domain.dao;

import com.gdut.dongjun.domain.po.DataMonitor;

public interface DataMonitorMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_monitor
     *
     * @mbggenerated Thu Apr 13 17:06:44 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_monitor
     *
     * @mbggenerated Thu Apr 13 17:06:44 CST 2017
     */
    int insert(DataMonitor record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_monitor
     *
     * @mbggenerated Thu Apr 13 17:06:44 CST 2017
     */
    int insertSelective(DataMonitor record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_monitor
     *
     * @mbggenerated Thu Apr 13 17:06:44 CST 2017
     */
    DataMonitor selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_monitor
     *
     * @mbggenerated Thu Apr 13 17:06:44 CST 2017
     */
    int updateByPrimaryKeySelective(DataMonitor record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_monitor
     *
     * @mbggenerated Thu Apr 13 17:06:44 CST 2017
     */
    int updateByPrimaryKey(DataMonitor record);
}