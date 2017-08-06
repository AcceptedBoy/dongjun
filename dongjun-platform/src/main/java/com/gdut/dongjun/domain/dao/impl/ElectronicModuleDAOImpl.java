package com.gdut.dongjun.domain.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.ElectronicModuleMapper;
import com.gdut.dongjun.domain.dao.base.impl.DelTagHolderDAOImpl;
import com.gdut.dongjun.domain.po.ElectronicModule;

@Repository
public class ElectronicModuleDAOImpl extends
DelTagHolderDAOImpl<ElectronicModule>
implements ElectronicModuleMapper {

	@Override
	public ElectronicModule selectByDeviceNumber(String deviceNumber) {
		return template.selectOne(getNamespace("selectByDeviceNumber"), deviceNumber);
	}

}
