package com.gdut.dongjun.service.device.event;

import java.util.List;

import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;
import com.gdut.dongjun.dto.TemperatureMeasureHitchEventDTO;
import com.gdut.dongjun.service.base.EnhancedService;

public interface TemperatureMeasureHitchEventService 
		extends EnhancedService<TemperatureMeasureHitchEvent>{

	List<TemperatureMeasureHitchEventDTO> selectMeasureHitch(String companyId);

}
