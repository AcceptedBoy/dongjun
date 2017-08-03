package com.gdut.dongjun.service.device.current;

import com.gdut.dongjun.domain.po.HighVoltageCurrent;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author zjd
 * @email 452880294@qq.com
 * @date 2015年11月21日
 * @description
 * @package com.gdut.dongjun.service
 */
public interface HighVoltageCurrentService extends
		DeviceCurrentService<HighVoltageCurrent> {

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
	 * 
	 * @Title: getRecentlyHitchEvent
	 * @Description: TODO
	 * @param @param switchId
	 * @return void
	 * @throws
	 */
	public List<HighVoltageCurrent> getRecentlyCurrent(String switchId, String phase);
	
	/**
	 * 现在只返回电流一次值
	 * @param switchId
	 * @param list
	 * @return
	 */
	public List<Float> getRealCurrent(String switchId, List<Integer> list);
	
	/**
	 * 现在只返回电流一次值
	 * @param switchId
	 * @param list
	 * @return
	 */
	public Float getRealCurrent(String switchId, Integer list); 
	
	/**
	 * @param string2 
	 * 
	 * @Title: selectByTime
	 * @Description: TODO
	 * @param @param switchId
	 * @param @param date
	 * @param @param string
	 * @param @return
	 * @return Map<String,Object>
	 * @throws
	 */
	public List<HighVoltageCurrent> selectByTime(String switchId, String beginDate,
                                     String endDate, String phase);
}
