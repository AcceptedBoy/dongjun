package com.gdut.dongjun.core.handler.thread;

import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.po.ElectronicModuleHitchEvent;
import com.gdut.dongjun.service.ElectronicModuleHitchEventService;
import com.gdut.dongjun.service.impl.ElectronicModuleHitchEventServiceImpl;

@Component
public class ElectronicModuleHitchEventTaskFactory extends HitchEventTaskFactory<ElectronicModuleHitchEvent> {

	@Override
	public HitchEventTask<ElectronicModuleHitchEvent> buildHitchEventTask(ElectronicModuleHitchEvent event) {
		ElectronicModuleHitchEventService service = (ElectronicModuleHitchEventService)applicationContext.getBean(ElectronicModuleHitchEventServiceImpl.class);
		ElectronicModuleHitchEventTask thread = new ElectronicModuleHitchEventTask(service, event);
		return thread;
	}

}
