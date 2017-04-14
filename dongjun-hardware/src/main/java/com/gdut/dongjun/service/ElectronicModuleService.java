package com.gdut.dongjun.service;

import com.gdut.dongjun.service.base.BaseService;
import com.gdut.dongjun.domain.po.ElectronicModule;

public interface ElectronicModuleService extends BaseService<ElectronicModule> {

	public ElectronicModule selectByDeviceNumber(String deviceNumber);
}