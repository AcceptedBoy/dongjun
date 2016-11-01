package com.gdut.dongjun.service;

import com.gdut.dongjun.domain.po.TemperatureDevice;
import com.gdut.dongjun.service.base.BaseService;

public interface TemperatureDeviceService extends BaseService<TemperatureDevice> {

	void createDeviceExcel(String filePath, Object object);

}
