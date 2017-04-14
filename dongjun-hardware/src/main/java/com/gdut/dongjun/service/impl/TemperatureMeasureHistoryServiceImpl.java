package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.TemperatureMeasureMapper;
import com.gdut.dongjun.domain.po.TemperatureMeasureHistory;
import com.gdut.dongjun.service.TemperatureMeasureHistoryService;
import com.gdut.dongjun.service.base.impl.EnhancedServiceImpl;

@Service
public class TemperatureMeasureHistoryServiceImpl extends EnhancedServiceImpl<TemperatureMeasureHistory>
		implements TemperatureMeasureHistoryService{

	@Autowired
	private TemperatureMeasureMapper measureMapper;
	
	@Override
	protected boolean isExist(TemperatureMeasureHistory record) {

		if (record != null
				&& measureMapper.selectByPrimaryKey(record.getId()) != null) {

			return true;
		} else {
			return false;
		}
	}
	

}
