package com.gdut.dongjun.domain.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.GPRSModuleMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.GPRSModule;

@Repository
public class GPRSModuleDAOImpl extends SinglePrimaryKeyBaseDAOImpl<GPRSModule>
		implements GPRSModuleMapper  {

	@Override
	public String isGPRSAvailable(String deviceNumber) {
		return template.selectOne("isGPRSAvailable", deviceNumber);
	}

}
