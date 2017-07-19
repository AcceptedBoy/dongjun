/**
 * 
 */
package com.gdut.dongjun.dao;


import java.util.List;

import com.gdut.dongjun.dao.base.SinglePrimaryKeyBaseMapper;
import com.gdut.dongjun.po.HighVoltageSwitch;

/**
 * @author zjd
 * @email 452880294@qq.com
 * @date 2015年11月21日
 * @description
 * @package com.gdut.dongjun.domain.dao
 */
public interface HighVoltageSwitchMapper extends SinglePrimaryKeyBaseMapper<HighVoltageSwitch> {

	List<String> selectIdByCompanyId(String companyId);
	
	List<String> selectIdByLineIds(List<String> list);
	
	int deleteByIds(List<String> list);
	
	List<String> selectAddrAvailableByCompanyId(String companyId);
}
