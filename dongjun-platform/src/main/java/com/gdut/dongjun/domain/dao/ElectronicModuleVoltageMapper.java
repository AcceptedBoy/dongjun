package com.gdut.dongjun.domain.dao;

import java.util.List;
import java.util.Map;

import com.gdut.dongjun.domain.dao.base.SinglePrimaryKeyBaseMapper;
import com.gdut.dongjun.domain.po.ElectronicModuleVoltage;

public interface ElectronicModuleVoltageMapper extends SinglePrimaryKeyBaseMapper <ElectronicModuleVoltage> {

	List<ElectronicModuleVoltage> selectByTime(Map<String, Object> map);
}