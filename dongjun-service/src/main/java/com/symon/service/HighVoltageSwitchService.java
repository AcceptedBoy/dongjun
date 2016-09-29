package com.symon.service;

import com.symon.po.HighVoltageSwitch;
import com.symon.service.base.BaseService;

import java.util.List;


/**
 * 
 * @author zjd
 * @email 452880294@qq.com
 * @date 2015年11月21日
 * @description
 * @package com.gdut.dongjun.service
 */
public interface HighVoltageSwitchService extends
		BaseService<HighVoltageSwitch> {
	/**
	 * 
	 * @Title: delSwitchByLineId
	 * @Description: TODO
	 * @param @param lineId
	 * @return void
	 * @throws
	 */
	public void delSwitchByLineId(String lineId);

	/**
	 * 
	 * @Title: updateSwitch
	 * @Description: TODO
	 * @param @param switch1
	 * @return void
	 * @throws
	 */
	public void updateSwitch(HighVoltageSwitch switch1);

	/**
	 * 
	 * @Title: 判断开关是否存在
	 * @Description: TODO
	 * @param @param switch1
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean isSwitchExist(HighVoltageSwitch switch1);

	/**
	 * 
	 * @Title: getAddress
	 * @Description: TODO
	 * @param @param switchId
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String getAddress(String switchId);

	/**
	 * 
	 * @Title: selectAll
	 * @Description: 根据线路id查找开关
	 * @param @param lineId
	 * @param @return
	 * @return Object
	 * @throws
	 */
	public List<HighVoltageSwitch> selectByLineId(String lineId);

	/**
	 * 
	 * @Title: getSwitchId
	 * @Description: TODO
	 * @param @param address
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String getSwitchId(String address);

}
