package com.gdut.dongjun.core.handler.thread;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.po.abstractmodel.AbstractHitchEvent;
import com.gdut.dongjun.service.base.BaseService;
import com.gdut.dongjun.service.impl.TemperatureMeasureHitchEventServiceImpl;

@Component
public class TemperatureHitchEventThreadFactory extends HitchEventThreadFactory {
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (null == this.applicationContext) {
			synchronized(TemperatureHitchEventThreadFactory.class) {
				if (null == this.applicationContext) {
					this.applicationContext = applicationContext;
				}
			}
		}
	}

	@Override
	public HitchEventThread buildHitchEventThread(AbstractHitchEvent event) {
		BaseService service = (BaseService)applicationContext.getBean(TemperatureMeasureHitchEventServiceImpl.class);
		HitchEventThread thread = new TemperatreHitchEventThread(service, event);
		return thread;
	}

}
