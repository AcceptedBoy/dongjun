package com.gdut.dongjun.service.device;

import java.util.List;

import com.gdut.dongjun.domain.po.ElectronicModuleVoltage;
import com.gdut.dongjun.service.base.EnhancedService;

public interface ElectronicModuleVoltageService extends EnhancedService<ElectronicModuleVoltage> {

	List<ElectronicModuleVoltage> selectByTime(String deviceId, String beginDate, String endDate, String phase);
}