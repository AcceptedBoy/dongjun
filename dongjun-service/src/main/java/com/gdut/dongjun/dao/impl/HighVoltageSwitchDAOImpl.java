/**
 * 
 */
package com.gdut.dongjun.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.dao.HighVoltageSwitchMapper;
import com.gdut.dongjun.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.po.HighVoltageSwitch;


/**
 * @author zjd
 * @email 452880294@qq.com
 * @date 2015年11月21日
 * @description
 * @package com.gdut.dongjun.domain.dao.impl
 */
@Repository
public class HighVoltageSwitchDAOImpl extends SinglePrimaryKeyBaseDAOImpl<HighVoltageSwitch>
implements HighVoltageSwitchMapper {

	@Override
	public List<String> selectIdByCompanyId(String companyId) {
		return template.selectList(getNamespace("selectIdByCompanyId"), companyId);
	}

	@Override
	public List<String> selectIdByLineIds(List<String> list) {
		return template.selectList(getNamespace("selectIdByLineIds"), list);
	}

	@Override
	public int deleteByIds(List<String> list) {
		return template.delete(getNamespace("deleteByIds"), list); 
	}

	@Override
	public List<String> selectAddrAvailableByCompanyId(String companyId) {
		return template.selectList(getNamespace("selectAddrAvailableByCompanyId"), companyId);
	}


}
