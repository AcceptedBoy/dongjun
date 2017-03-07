package com.gdut.dongjun.service.device.event.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.TemperatureMeasureHitchEventMapper;
import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;
import com.gdut.dongjun.service.device.event.TemperatureMeasureHitchEventService;
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

}
