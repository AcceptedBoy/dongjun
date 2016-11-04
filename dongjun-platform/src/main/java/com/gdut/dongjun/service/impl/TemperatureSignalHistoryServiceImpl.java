package com.gdut.dongjun.service.impl;

import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.po.TemperatureSignalHistory;
import com.gdut.dongjun.service.TemperatureSignalHistoryService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;

@Service
public class TemperatureSignalHistoryServiceImpl extends BaseServiceImpl<TemperatureSignalHistory>
		implements TemperatureSignalHistoryService{

	@Override
	protected boolean isExist(TemperatureSignalHistory record) {
		// TODO Auto-generated method stub
		return false;
	}

}
