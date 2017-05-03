package com.gdut.dongjun.domain.dao;

import java.util.List;

import com.gdut.dongjun.domain.dao.base.SinglePrimaryKeyBaseMapper;
import com.gdut.dongjun.domain.po.TemperatureSensor;

public interface TemperatureSensorMapper extends SinglePrimaryKeyBaseMapper<TemperatureSensor> {

	List<TemperatureSensor> selectAllType(String id);
   
}