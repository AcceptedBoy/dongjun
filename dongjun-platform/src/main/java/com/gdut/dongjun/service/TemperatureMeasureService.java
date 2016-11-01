package com.gdut.dongjun.service;

import java.util.Map;

import com.gdut.dongjun.domain.po.TemperatureMeasure;
import com.gdut.dongjun.service.base.BaseService;

public interface TemperatureMeasureService extends BaseService<TemperatureMeasure> {

	Map<String, Object> selectByTimeAndSensorId(String id, String sensorId, String beginDate, String endDate);

}
