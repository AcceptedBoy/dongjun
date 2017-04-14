package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.TemperatureMeasureHitchEventMapper;
import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;
import com.gdut.dongjun.service.TemperatureMeasureHitchEventService;
import com.gdut.dongjun.service.base.impl.EnhancedServiceImpl;

@Service
public class TemperatureMeasureHitchEventServiceImpl extends EnhancedServiceImpl<TemperatureMeasureHitchEvent>
		implements TemperatureMeasureHitchEventService{

	@Autowired
	private TemperatureMeasureHitchEventMapper currentMapper;
	
	@Override
	protected boolean isExist(TemperatureMeasureHitchEvent record) {
		if (record != null
				&& currentMapper.selectByPrimaryKey(record.getId()) != null) {
			return true;
		} else {
			return false;
		}
	}
}
