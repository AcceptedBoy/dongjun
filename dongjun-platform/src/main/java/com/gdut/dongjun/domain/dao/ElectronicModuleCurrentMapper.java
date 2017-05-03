package com.gdut.dongjun.domain.dao;

import java.util.List;
import java.util.Map;

import com.gdut.dongjun.domain.dao.base.SinglePrimaryKeyBaseMapper;
import com.gdut.dongjun.domain.po.ElectronicModuleCurrent;

public interface ElectronicModuleCurrentMapper extends SinglePrimaryKeyBaseMapper <ElectronicModuleCurrent> {

	List<ElectronicModuleCurrent> selectByTime(Map<String, Object> map);

}