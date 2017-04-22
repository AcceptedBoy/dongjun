package com.gdut.dongjun.service;

import com.gdut.dongjun.domain.po.ElectronicModule;
import com.gdut.dongjun.service.base.EnhancedService;

public interface ElectronicModuleService extends EnhancedService<ElectronicModule> {

	public ElectronicModule selectByDeviceNumber(String deviceNumber);
}