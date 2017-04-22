package com.gdut.dongjun.core.handler.thread;

import org.apache.log4j.Logger;

import com.gdut.dongjun.core.HitchConst;
import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;
import com.gdut.dongjun.service.base.EnhancedService;

public class TemperatreHitchEventTask extends HitchEventTask<TemperatureMeasureHitchEvent> {

	private Logger logger = Logger.getLogger(TemperatreHitchEventTask.class);
	
	public TemperatreHitchEventTask(EnhancedService<TemperatureMeasureHitchEvent> service, TemperatureMeasureHitchEvent event) {
		super();
		this.setService(service);
		this.hitchEvent = event;
	}

	@Override
	public void doPreHandle() {
		service.updateByPrimaryKey(hitchEvent);
		//TODO 日志打印不够准确，需要进一步抽象HitchEvent
		logger.info(HitchConst.HITCH_OVER_TEMPERATURE );
	}

	@Override
	public void doPostHandle() {}


}
