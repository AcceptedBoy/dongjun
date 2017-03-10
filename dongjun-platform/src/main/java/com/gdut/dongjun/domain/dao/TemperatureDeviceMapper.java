package com.gdut.dongjun.domain.dao;

import java.util.List;

import com.gdut.dongjun.domain.dao.base.SinglePrimaryKeyBaseMapper;
import com.gdut.dongjun.domain.po.TemperatureDevice;

public interface TemperatureDeviceMapper 
		extends SinglePrimaryKeyBaseMapper<TemperatureDevice> {

	public List<TemperatureDevice> selectDeviceByIds(List<String> ids);
	
	public String selectNameById(String id);

}
