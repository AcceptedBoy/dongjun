package com.gdut.dongjun.domain.dao;

import java.util.List;
import java.util.Map;

import com.gdut.dongjun.domain.dao.base.SinglePrimaryKeyBaseMapper;
import com.gdut.dongjun.domain.po.TemperatureMeasure;


public interface TemperatureMeasureMapper 
			extends SinglePrimaryKeyBaseMapper<TemperatureMeasure> {

	public List<TemperatureMeasure> selectMeasureByTimeAndSensorId(Map<String ,Object> xx);
}
