package com.gdut.dongjun.service;

import com.gdut.dongjun.domain.po.GPRSModule;
import com.gdut.dongjun.service.base.EnhancedService;

public interface GPRSModuleService extends EnhancedService<GPRSModule> {

	public String isGPRSAvailable(String deviceNumber);
}
