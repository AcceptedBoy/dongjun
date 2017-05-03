package com.gdut.dongjun.service.device.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.ElectronicModulePowerMapper;
import com.gdut.dongjun.domain.po.ElectronicModuleCurrent;
import com.gdut.dongjun.domain.po.ElectronicModulePower;
import com.gdut.dongjun.service.base.impl.EnhancedServiceImpl;
import com.gdut.dongjun.service.device.ElectronicModulePowerService;

@Service
public class ElectronicModulePowerServiceImpl extends EnhancedServiceImpl<ElectronicModulePower> implements ElectronicModulePowerService {
 
	@Autowired
	private ElectronicModulePowerMapper mapper;
	
	@Override
	protected boolean isExist(ElectronicModulePower record) {
		if (record != null && null != record.getId()
				&& (mapper.selectByPrimaryKey(record.getId()) != null))
			return true;
		return false;
	}
	
	@Override
	public List<ElectronicModulePower> selectByTime(String deviceId, String beginDate, String endDate, String phase) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("beginDate", beginDate);
		map.put("endDate", endDate);
		map.put("deviceId", deviceId);
		map.put("phase", phase);
		return mapper.selectByTime(map);
	}
}