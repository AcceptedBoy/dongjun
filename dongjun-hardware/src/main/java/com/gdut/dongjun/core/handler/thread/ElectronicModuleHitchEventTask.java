package com.gdut.dongjun.core.handler.thread;

import org.apache.log4j.Logger;

import com.gdut.dongjun.core.HitchConst;
import com.gdut.dongjun.domain.po.ElectronicModuleHitchEvent;
import com.gdut.dongjun.service.ElectronicModuleHitchEventService;
import com.gdut.dongjun.service.base.EnhancedService;
import com.gdut.dongjun.util.UUIDUtil;

public class ElectronicModuleHitchEventTask extends HitchEventTask<ElectronicModuleHitchEvent> {

	private Logger logger = Logger.getLogger(ElectronicModuleHitchEventTask.class);
	
	public ElectronicModuleHitchEventTask(ElectronicModuleHitchEventService service, ElectronicModuleHitchEvent event) {
		super(service, event);
	}

	@Override
	public void doPreHandle() {
		hitchEvent.setId(UUIDUtil.getUUID());
		service.updateByPrimaryKey(hitchEvent);
		//由于拼凑HitchEventVO的时候要ModuleHitchEvent的id，所以这里设置hitchEvent的id为ModuleHitchEvent的id
		hitchEvent.setId(hitchEvent.getHitchId());
		logger.info(HitchConst.getHitchReason(hitchEvent.getType()));
	}

	@Override
	public void doPostHandle() {}

}
