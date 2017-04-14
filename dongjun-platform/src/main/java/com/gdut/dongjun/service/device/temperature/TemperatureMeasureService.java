package com.gdut.dongjun.service.device.temperature;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.gdut.dongjun.domain.po.TemperatureMeasure;
import com.gdut.dongjun.service.base.EnhancedService;

public interface TemperatureMeasureService extends EnhancedService<TemperatureMeasure> {

	Map<String, Object> selectByTimeAndSensorId(String id, String sensorId, String beginDate, String endDate);

	List<TemperatureMeasure> selectByTime(String id, int tag, String beginDate, String endDate);

	int getCount(String deviceId);
	
	int updateTime(int pre, int after, String deviceId, Date date);
}
