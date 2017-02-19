package com.gdut.dongjun.service.device.voltage;

import java.util.List;
import java.util.Map;

import com.gdut.dongjun.domain.po.ControlMearsureVoltage;


public interface ControlMearsureVoltageService extends
		DeviceVoltageService<ControlMearsureVoltage> {

	/**
	 * @description	读取实时电压
	 * @return	TODO
	 */
	public List<ControlMearsureVoltage> getRecentlyVoltage();
	
	/**
	 * 
	 * @Title: selectBySwitchId
	 * @Description: TODO
	 * @param @param switchId
	 * @param @return
	 * @return List<Voltage>
	 * @throws
	 */
	public Map<String, Object> selectBySwitchId(String switchId);

	/**
	 * @param string 
	 * 
	 * @Title: selectByTime
	 * @Description: TODO
	 * @param @param date
	 * @param @return
	 * @return Object
	 * @throws
	 */
	public List<Object> selectByTime(String switchId, String date, String string);

}
