package com.gdut.dongjun.domain.dao;

import com.gdut.dongjun.domain.dao.base.SinglePrimaryKeyBaseMapper;
import com.gdut.dongjun.domain.po.ElectronicModule;

public interface ElectronicModuleMapper extends SinglePrimaryKeyBaseMapper<ElectronicModule> {

	public ElectronicModule selectByDeviceNumber(String deviceNumber);
}