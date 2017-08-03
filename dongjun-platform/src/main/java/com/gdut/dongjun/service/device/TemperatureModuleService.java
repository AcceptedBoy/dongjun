package com.gdut.dongjun.service.device;

import com.gdut.dongjun.domain.po.TemperatureModule;
import com.gdut.dongjun.domain.vo.chart.ChartData;
import com.gdut.dongjun.service.base.DelTagHolderService;

public interface TemperatureModuleService extends DelTagHolderService<TemperatureModule> {

	public void createExcel(ChartData chart, String filePath);
}
