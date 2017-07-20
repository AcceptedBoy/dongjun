package com.gdut.dongjun.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gdut.dongjun.service.common.CommonSwitch;
import com.gdut.dongjun.service.webservice.client.CommonServiceClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.SubstationMapper;
import com.gdut.dongjun.domain.po.Substation;
import com.gdut.dongjun.service.SubstationService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;

/**
 * @Title: UserServiceImpl.java
 * @Package com.gdut.dongjun.service.impl.system
 * @Description: TODO
 * @author Sherlock-lee
 * @date 2015年7月24日 下午2:33:08
 * @version V1.0
 */
@Service
public class SubstationServiceImpl extends BaseServiceImpl<Substation>
		implements SubstationService {
	/**
	 * @ClassName: UserServiceImpl
	 * @Description: TODO
	 * @author Sherlock-lee
	 * @date 2015年7月24日 下午2:33:08
	 */
	@Autowired
	private SubstationMapper substationMapper;

	@Autowired
	private CommonSwitch commonSwitch;

	@Autowired
	private CommonServiceClient centorServiceClient;

	@Override
	public List<Substation> selectByCompanyId(String companyId) {

		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> xx = new HashMap<String, Object>();
		map.put("company_id", companyId);
		xx.put("map", map);
		List<Substation> list = substationMapper.selectByParameters(xx);

		if (list != null) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public void deleteByPrimaryKeyWithCxf(String switchId) {

		substationMapper.deleteByPrimaryKey(switchId);

		if(commonSwitch.canService()) {
			centorServiceClient.getService().deleteSubstation(switchId);
		}
	}

	@Override
	protected boolean isExist(Substation record) {

		if (record != null
				&& substationMapper.selectByPrimaryKey(record.getId()) != null) {

			return true;
		} else {
			return false;
		}
	}
	
	
	
	
	
	
}
