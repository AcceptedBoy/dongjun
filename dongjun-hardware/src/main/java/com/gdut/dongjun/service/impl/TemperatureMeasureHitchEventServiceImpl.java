package com.gdut.dongjun.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.TemperatureMeasureHitchEventMapper;
import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;
import com.gdut.dongjun.service.TemperatureMeasureHitchEventService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;
import com.gdut.dongjun.util.UUIDUtil;

@Service
public class TemperatureMeasureHitchEventServiceImpl extends BaseServiceImpl<TemperatureMeasureHitchEvent>
		implements TemperatureMeasureHitchEventService{

	@Autowired
	private TemperatureMeasureHitchEventMapper currentMapper;
	
	@Override
	protected boolean isExist(TemperatureMeasureHitchEvent record) {
		if (record != null
				&& currentMapper.selectByPrimaryKey(record.getId()) != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void insertHitchEvent(String deviceId, double d) {
		TemperatureMeasureHitchEvent event = new TemperatureMeasureHitchEvent();
		Date date = new Date();
		event.setGmtCreate(date);
		event.setGmtModified(date);
		event.setHitchReason("温度超过阈值");
		event.setId(UUIDUtil.getUUID());
	}

}
