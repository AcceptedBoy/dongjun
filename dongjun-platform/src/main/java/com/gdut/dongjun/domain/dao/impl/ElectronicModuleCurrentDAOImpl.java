package com.gdut.dongjun.domain.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.ElectronicModuleCurrentMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.ElectronicModuleCurrent;

@Repository
public class ElectronicModuleCurrentDAOImpl extends SinglePrimaryKeyBaseDAOImpl<ElectronicModuleCurrent> implements ElectronicModuleCurrentMapper {

	@Override
	public List<ElectronicModuleCurrent> selectByTime(Map<String, Object> map) {
		return template.selectList(getNamespace("selectByTime"), map);
	}

}