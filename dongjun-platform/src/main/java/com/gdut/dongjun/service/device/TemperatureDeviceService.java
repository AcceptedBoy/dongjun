package com.gdut.dongjun.service.device;

import java.util.List;

import com.gdut.dongjun.domain.po.TemperatureDevice;
import com.gdut.dongjun.service.base.BaseService;

public interface TemperatureDeviceService extends BaseService<TemperatureDevice> {

	boolean createDeviceExcel(String filePath, List<TemperatureDevice> object);

	boolean uploadDevice(String realPath, String platformGroupId);
	
	public List<TemperatureDevice> selectDeviceByIds(List<String> ids);
}
