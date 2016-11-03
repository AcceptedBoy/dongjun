package com.gdut.dongjun.domain.dao;

import java.util.List;
import java.util.Map;

import com.gdut.dongjun.domain.dao.base.SinglePrimaryKeyBaseMapper;
import com.gdut.dongjun.domain.po.TemperatureMeasureHistory;

public interface TemperatureMeasureHistoryMapper
		extends SinglePrimaryKeyBaseMapper<TemperatureMeasureHistory>{
	
	public List<TemperatureMeasureHistory> selectByTime(Map<String, Object> xx);

}
