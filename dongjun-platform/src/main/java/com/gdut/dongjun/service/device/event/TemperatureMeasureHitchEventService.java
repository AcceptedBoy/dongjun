package com.gdut.dongjun.service.device.event;

import java.util.List;

import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;
import com.gdut.dongjun.service.base.EnhancedService;
import com.gdut.dongjun.web.vo.TemperatureMeasureHitchEventVO;

public interface TemperatureMeasureHitchEventService 
		extends EnhancedService<TemperatureMeasureHitchEvent>{

	List<TemperatureMeasureHitchEventVO> selectMeasureHitch(String companyId);

}
