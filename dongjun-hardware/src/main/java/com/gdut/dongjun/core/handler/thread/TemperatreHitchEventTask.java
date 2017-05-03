package com.gdut.dongjun.core.handler.thread;

import org.apache.log4j.Logger;

import com.gdut.dongjun.core.HitchConst;
import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;
import com.gdut.dongjun.service.base.EnhancedService;
import com.gdut.dongjun.util.UUIDUtil;

public class TemperatreHitchEventTask extends HitchEventTask<TemperatureMeasureHitchEvent> {

	private Logger logger = Logger.getLogger(TemperatreHitchEventTask.class);
	
	public TemperatreHitchEventTask(EnhancedService<TemperatureMeasureHitchEvent> service, TemperatureMeasureHitchEvent event) {
		super();
		this.setService(service);
		this.hitchEvent = event;
	}

	@Override
	public void doPreHandle() {
		TemperatureMeasureHitchEvent e = new TemperatureMeasureHitchEvent();
		e.setId(UUIDUtil.getUUID());
		e.setHitchId(hitchEvent.getHitchId());
		e.setMaxHitchValue(hitchEvent.getMaxHitchValue());
		e.setMinHitchValue(hitchEvent.getMinHitchValue());
		e.setValue(hitchEvent.getValue());
		e.setTag(hitchEvent.getTag());
		service.updateByPrimaryKey(e);
		hitchEvent.setId(hitchEvent.getHitchId());
		//TODO 日志打印不够准确，需要进一步抽象HitchEvent
		logger.info(HitchConst.HITCH_OVER_TEMPERATURE );
	}

	@Override
	public void doPostHandle() {}


}
