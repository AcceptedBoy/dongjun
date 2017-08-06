package com.gdut.dongjun.domain.dao;

import java.util.List;
import java.util.Map;

import com.gdut.dongjun.domain.dao.base.SinglePrimaryKeyBaseMapper;
import com.gdut.dongjun.domain.po.ModuleHitchEvent;

public interface ModuleHitchEventMapper extends SinglePrimaryKeyBaseMapper <ModuleHitchEvent> {

	List<ModuleHitchEvent> selectByType(Map<String, Object> map);

	List<ModuleHitchEvent> selectByTypeAndModuleId(Map<String, Object> map);

}