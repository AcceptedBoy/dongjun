package com.gdut.dongjun.service;

import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;
import com.gdut.dongjun.service.base.BaseService;

public interface TemperatureMeasureHitchEventService 
		extends BaseService<TemperatureMeasureHitchEvent>{

	public void insertHitchEvent(String deviceId, double d);

}
