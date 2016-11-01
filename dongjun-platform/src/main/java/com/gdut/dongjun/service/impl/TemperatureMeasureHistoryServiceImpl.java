package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.TemperatureMeasureMapper;
import com.gdut.dongjun.domain.po.TemperatureMeasureHistory;
import com.gdut.dongjun.service.TemperatureMeasureHistoryService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;

@Service
public class TemperatureMeasureHistoryServiceImpl extends BaseServiceImpl<TemperatureMeasureHistory>
		implements TemperatureMeasureHistoryService{

	@Autowired
	private TemperatureMeasureMapper measureMapper;
	
	@Override
	protected boolean isExist(TemperatureMeasureHistory record) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
