package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.dao.CompanyMapper;
import com.gdut.dongjun.po.Company;
import com.gdut.dongjun.service.CompanyService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;

@Service
public class CompanyServiceImpl extends
BaseServiceImpl<Company> implements CompanyService {

	@Autowired
	private CompanyMapper mapper;
	
	@Override
	protected boolean isExist(Company record) {
		if (record != null && null != record.getId()
				&& mapper.selectByPrimaryKey(record.getId()) != null) {
			return true;
		} else {
			return false;
		}
	}

}
