package com.gdut.dongjun.service.device.event.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.ModuleHitchEventMapper;
import com.gdut.dongjun.domain.po.ModuleHitchEvent;
import com.gdut.dongjun.service.base.impl.EnhancedServiceImpl;
import com.gdut.dongjun.service.device.event.ModuleHitchEventService;

@Service
public class ModuleHitchEventServiceImpl extends EnhancedServiceImpl<ModuleHitchEvent> implements ModuleHitchEventService {

	@Autowired
	private ModuleHitchEventMapper mapper;
	
	@Override
	protected boolean isExist(ModuleHitchEvent record) {
		if (null != record && null != record.getId() &&
				null != mapper.selectByPrimaryKey(record.getId())) {
			return true;
		}
		return false;
	}

	@Override
	public List<ModuleHitchEvent> selectByType(int pre, int post, String groupId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pre", pre);
		map.put("post", post);
		map.put("groupId", groupId);
		return mapper.selectByType(map);
	}
 }