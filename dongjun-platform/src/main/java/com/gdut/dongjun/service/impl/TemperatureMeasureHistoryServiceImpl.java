package com.gdut.dongjun.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.TemperatureMeasureHistoryMapper;
import com.gdut.dongjun.domain.po.TemperatureMeasureHistory;
import com.gdut.dongjun.service.TemperatureMeasureHistoryService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;

@Service
public class TemperatureMeasureHistoryServiceImpl extends BaseServiceImpl<TemperatureMeasureHistory>
		implements TemperatureMeasureHistoryService{

	@Autowired
	private TemperatureMeasureHistoryMapper measureHistoryMapper;
	
	@Override
	protected boolean isExist(TemperatureMeasureHistory record) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<TemperatureMeasureHistory> selectByTime(String id, String aSensorAddress, String beginDate,
			String endDate) {
		Map<String, Object> xx = new HashMap<String, Object>();
		xx.put("deviceId", id);
		xx.put("sensorAddress", aSensorAddress);
		xx.put("beginDate", beginDate);
		xx.put("endDate", endDate);
		return measureHistoryMapper.selectByTime(xx);
	}
	

}
