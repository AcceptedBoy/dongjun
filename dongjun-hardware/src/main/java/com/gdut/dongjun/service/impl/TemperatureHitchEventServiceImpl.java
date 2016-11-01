package com.gdut.dongjun.service.impl;

import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.po.TemperatureHitchEvent;
import com.gdut.dongjun.service.TemperatureHitchEventService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;

@Service
public class TemperatureHitchEventServiceImpl extends BaseServiceImpl<TemperatureHitchEvent>
		implements TemperatureHitchEventService {

	@Override
	protected boolean isExist(TemperatureHitchEvent record) {
		// TODO Auto-generated method stub
		return false;
	}


}
