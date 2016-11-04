package com.gdut.dongjun.service;

import java.util.List;

import com.gdut.dongjun.domain.po.TemperatureMeasureHistory;
import com.gdut.dongjun.service.base.BaseService;

public interface TemperatureMeasureHistoryService extends BaseService<TemperatureMeasureHistory> {

	public List<TemperatureMeasureHistory> selectByTime(String id, int tag, String beginDate, String endDate);

}
