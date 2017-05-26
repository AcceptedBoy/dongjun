package com.gdut.dongjun.domain.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.DataMonitorSubmoduleMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.DataMonitorSubmodule;

@Repository
public class DataMonitorSubmoduleDAOImpl extends SinglePrimaryKeyBaseDAOImpl<DataMonitorSubmodule> implements DataMonitorSubmoduleMapper {

	@Override
	public String selectMonitorIdByModuleId(String moduleId) {
		return template.selectOne(getNamespace("selectMonitorIdByModuleId"), moduleId);
	}

}