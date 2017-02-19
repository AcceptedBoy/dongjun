package com.gdut.dongjun.service.device.current;

import java.util.List;
import java.util.Map;

import com.gdut.dongjun.domain.po.ControlMearsureCurrent;


public interface ControlMearsureCurrentService extends
		DeviceCurrentService<ControlMearsureCurrent> {

	/**
	 * 
	 * @Title: selectBySwitchId
	 * @Description: TODO
	 * @param @param switchId
	 * @param @return
	 * @return List<Current>
	 * @throws
	 */
	public Map<String, Object> selectBySwitchId(String switchId);

	/**
	 * @param string 
	 * 
	 * @Title: selectByTime
	 * @Description: TODO
	 * @param @param switchId
	 * @param @param d
	 * @return void
	 * @throws
	 */
	public List<Object> selectByTime(String switchId, String date, String string);

	/**
	 * 
	 * @Title: getRecentlyHitchEvent
	 * @Description: 读取实时电流
	 * @param @param switchId
	 * @return void
	 * @throws
	 */
	public List<ControlMearsureCurrent> getRecentlyCurrent();
	
	
	
	
	
}
