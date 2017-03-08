package com.gdut.dongjun.core.handler.thread;

import org.apache.log4j.Logger;

import com.gdut.dongjun.domain.po.abstractmodel.AbstractHitchEvent;
import com.gdut.dongjun.enums.LogConst;
import com.gdut.dongjun.service.base.BaseService;

public class TemperatreHitchEventThread extends HitchEventThread {

	private Logger logger = Logger.getLogger(TemperatreHitchEventThread.class);
	
	public TemperatreHitchEventThread(BaseService service, AbstractHitchEvent event) {
		this.setService(service);
		this.hitchEvent = event;
	}

	@Override
	public void doPreHandle() {
		service.insert(hitchEvent);
		//TODO 日志打印不够准确，需要进一步抽象HitchEvent
		logger.info(LogConst.TEMPERATURE_HITCH );
	}

	@Override
	public void doPostHandle() {}


}
