package com.gdut.dongjun.service;

import com.gdut.dongjun.domain.po.DataMonitorSubmodule;
import com.gdut.dongjun.service.base.EnhancedService;

public interface DataMonitorSubmoduleService extends EnhancedService<DataMonitorSubmodule> {

	String selectMonitorIdByModuleId(String moduleId);
}