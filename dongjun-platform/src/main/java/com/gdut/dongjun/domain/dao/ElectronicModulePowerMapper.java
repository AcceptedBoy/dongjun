package com.gdut.dongjun.domain.dao;

import java.util.List;
import java.util.Map;

import com.gdut.dongjun.domain.dao.base.SinglePrimaryKeyBaseMapper;
import com.gdut.dongjun.domain.po.ElectronicModulePower;

public interface ElectronicModulePowerMapper extends SinglePrimaryKeyBaseMapper <ElectronicModulePower> {

	List<ElectronicModulePower> selectByTime(Map<String, Object> map);
}