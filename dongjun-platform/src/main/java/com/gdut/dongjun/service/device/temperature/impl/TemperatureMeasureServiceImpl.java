package com.gdut.dongjun.service.device.temperature.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.TemperatureMeasureMapper;
import com.gdut.dongjun.domain.po.TemperatureMeasure;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;
import com.gdut.dongjun.service.device.temperature.TemperatureMeasureService;
import com.gdut.dongjun.util.MyBatisMapUtil;

@Service
public class TemperatureMeasureServiceImpl extends BaseServiceImpl<TemperatureMeasure>
		implements TemperatureMeasureService {

	@Autowired
	private TemperatureMeasureMapper temperatureMeasureMapper;

	@Override
	protected boolean isExist(TemperatureMeasure record) {
		if (record != null &&
				temperatureMeasureMapper.selectByPrimaryKey(record.getId()) != null)
			return true;
		return false;
	}

	@Override
	public Map<String, Object> selectByTimeAndSensorId(String id, String sensorId, String beginDate, String endDate) {
		String[] sensors = sensorId.split(",=");
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		paramMap.put("beginDate", beginDate);
		paramMap.put("endDate", endDate);
		paramMap.put("sensorId", sensorId);
		for (int i = 0; i < sensors.length; i++) {
			if (paramMap.containsKey("sensorId")) {
				paramMap.remove("sensorId");
				paramMap.put("sensorId", sensors[i]);
			}
			map.put(sensors[i], temperatureMeasureMapper.selectByTimeAndSensorId(MyBatisMapUtil.warp(paramMap)));
		}
		return map;
	}

	@Override
	public List<TemperatureMeasure> selectByTime(String id, int tag, String beginDate, String endDate) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		paramMap.put("beginDate", beginDate);
		paramMap.put("endDate", endDate);
		paramMap.put("tag", tag);
		return temperatureMeasureMapper.selectByTime(paramMap);
	}


}
