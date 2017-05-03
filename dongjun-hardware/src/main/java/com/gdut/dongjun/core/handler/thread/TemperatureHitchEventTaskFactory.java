package com.gdut.dongjun.core.handler.thread;

import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;
import com.gdut.dongjun.service.TemperatureMeasureHitchEventService;
import com.gdut.dongjun.service.impl.TemperatureMeasureHitchEventServiceImpl;

@Component
public class TemperatureHitchEventTaskFactory extends HitchEventTaskFactory<TemperatureMeasureHitchEvent> {

	@Override
	public HitchEventTask<TemperatureMeasureHitchEvent> buildHitchEventTask(TemperatureMeasureHitchEvent event) {
		TemperatureMeasureHitchEventService service = (TemperatureMeasureHitchEventService)applicationContext.getBean(TemperatureMeasureHitchEventServiceImpl.class);
		TemperatreHitchEventTask thread = new TemperatreHitchEventTask(service, event);
		return thread;
	}

}
