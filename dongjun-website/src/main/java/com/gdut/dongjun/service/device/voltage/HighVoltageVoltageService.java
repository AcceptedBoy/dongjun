package com.gdut.dongjun.service.device.voltage;

import java.util.List;
import java.util.Map;

import com.gdut.dongjun.domain.po.HighVoltageVoltage;

/**
 * 
 * @author zjd
 * @email 452880294@qq.com
 * @date 2015年11月21日
 * @description
 * @package com.gdut.dongjun.service
 */
public interface HighVoltageVoltageService extends DeviceVoltageService<HighVoltageVoltage> {

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

	/**
	 * 
	 * @Title: getRecentlyVoltage
	 * @Description: TODO
	 * @param @param switchId
	 * @param @return
	 * @return List<Voltage>
	 * @throws
	 */
	public List<HighVoltageVoltage> getRecentlyVoltage(String switchId, String phase);
	
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
	public List<HighVoltageVoltage> selectByTime(String switchId, String beginDate,
                                     String endDate, String phase);
	
	public int insertMulti(List<HighVoltageVoltage> list);

}
