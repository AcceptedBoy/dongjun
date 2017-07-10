package com.gdut.dongjun.service.device;

import java.util.List;

import com.gdut.dongjun.domain.po.ElectronicModuleCurrent;
import com.gdut.dongjun.domain.vo.chart.ChartData;
import com.gdut.dongjun.service.base.EnhancedService;

public interface ElectronicModuleCurrentService extends EnhancedService<ElectronicModuleCurrent> {

	List<ElectronicModuleCurrent> selectByTime(String deviceId, String beginDate, String endDate, String phase);

	void createExcel(ChartData chart, String filePath);
}