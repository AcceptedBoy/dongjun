package com.gdut.dongjun.domain.dao;

import com.gdut.dongjun.domain.dao.base.DelTagHolderMapper;
import com.gdut.dongjun.domain.po.ElectronicModule;

public interface ElectronicModuleMapper extends DelTagHolderMapper<ElectronicModule> {

	public ElectronicModule selectByDeviceNumber(String deviceNumber);
}