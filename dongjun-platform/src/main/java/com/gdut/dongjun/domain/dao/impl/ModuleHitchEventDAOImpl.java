package com.gdut.dongjun.domain.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.ModuleHitchEventMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.ModuleHitchEvent;

@Repository
public class ModuleHitchEventDAOImpl extends SinglePrimaryKeyBaseDAOImpl<ModuleHitchEvent> implements ModuleHitchEventMapper {

	@Override
	public List<ModuleHitchEvent> selectByType(Map<String, Object> map) {
		return template.selectList(getNamespace("selectByType"), map);
	}

	@Override
	public List<ModuleHitchEvent> selectByTypeAndModuleId(Map<String, Object> map) {
		return template.selectList(getNamespace("selectByTypeAndModuleId"), map);
	}

}