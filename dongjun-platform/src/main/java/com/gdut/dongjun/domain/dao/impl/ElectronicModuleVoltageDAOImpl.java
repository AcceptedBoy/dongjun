package com.gdut.dongjun.domain.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.ElectronicModuleVoltageMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.ElectronicModulePower;
import com.gdut.dongjun.domain.po.ElectronicModuleVoltage;

@Repository
public class ElectronicModuleVoltageDAOImpl extends SinglePrimaryKeyBaseDAOImpl<ElectronicModuleVoltage> implements ElectronicModuleVoltageMapper {

	@Override
	public List<ElectronicModuleVoltage> implements> selectByTime(Map<String, Object> map) {
		return template.selectList(getNamespace("selectByTime"), map);
	}
}