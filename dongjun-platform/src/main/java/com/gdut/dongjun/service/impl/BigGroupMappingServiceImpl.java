package com.gdut.dongjun.service.impl;

import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.po.BigGroupMapping;
import com.gdut.dongjun.service.BigGroupMappingService;
import com.gdut.dongjun.service.base.impl.EnhancedServiceImpl;

@Service
public class BigGroupMappingServiceImpl extends EnhancedServiceImpl<BigGroupMapping> implements BigGroupMappingService {

	@Override
	protected boolean isExist(BigGroupMapping record) {
		if (null != record && null != record.getId()
				&& null != baseMapper.selectByPrimaryKey(record.getId())) {
			return true;
		}
		return false;
	}
 }