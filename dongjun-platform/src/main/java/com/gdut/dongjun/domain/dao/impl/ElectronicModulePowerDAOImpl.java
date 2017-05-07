package com.gdut.dongjun.domain.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.ElectronicModulePowerMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.ElectronicModuleCurrent;
import com.gdut.dongjun.domain.po.ElectronicModulePower;

@Repository
public class ElectronicModulePowerDAOImpl extends SinglePrimaryKeyBaseDAOImpl<ElectronicModulePower> implements ElectronicModulePowerMapper {

	@Override
	public List<ElectronicModulePower> selectByTime(Map<String, Object> map) {
		return template.selectList(getNamespace("selectByTime"), map);
	}
}