package com.gdut.dongjun.core.handler.thread;

import org.springframework.beans.factory.annotation.Autowired;

import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;
import com.gdut.dongjun.service.TemperatureMeasureHitchEventService;

public class TemperatreHitchEventThread extends HitchEventThread {

	private TemperatureMeasureHitchEvent event;
	
	public TemperatreHitchEventThread(TemperatureMeasureHitchEvent event) {
		this.event = event;
	}
	
	@Autowired
	private TemperatureMeasureHitchEventService eventService;
	
	@Override
	public void run() {
		eventService.insert(event);
	}

}
