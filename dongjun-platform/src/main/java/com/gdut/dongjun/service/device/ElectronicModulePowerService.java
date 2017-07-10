package com.gdut.dongjun.service.device;

import java.util.List;

import com.gdut.dongjun.domain.po.ElectronicModulePower;
import com.gdut.dongjun.domain.vo.chart.ChartData;
import com.gdut.dongjun.service.base.EnhancedService;

public interface ElectronicModulePowerService extends EnhancedService<ElectronicModulePower> {

	List<ElectronicModulePower> selectByTime(String deviceId, String beginDate, String endDate, String phase);

	void createExcel(ChartData chart, String filePath);
}