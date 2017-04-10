package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.TemperatureMeasureMapper;
import com.gdut.dongjun.domain.po.TemperatureMeasure;
import com.gdut.dongjun.service.TemperatureMeasureService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;

@Service
public class TemperatureMeasureServiceImpl extends BaseServiceImpl<TemperatureMeasure>
		implements TemperatureMeasureService {

	@Autowired
	private TemperatureMeasureMapper temperatureMeasureMapper;

	@Override
	protected boolean isExist(TemperatureMeasure record) {
		if (record != null &&
				temperatureMeasureMapper.selectByPrimaryKey(record.getId()) != null)
			return true;
		return false;
	}


}
