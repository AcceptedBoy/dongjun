package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.dao.CompanyMapper;
import com.gdut.dongjun.po.Company;
import com.gdut.dongjun.service.CompanyService;

@Service
public class CompanyServiceImpl extends BaseServiceImpl<Company> implements CompanyService {

	@Autowired
	private CompanyMapper mapper;
	
	@Override
	protected boolean isExist(Company record) {
		if (null != record && null != record.getId()
				&& null != mapper.selectByPrimaryKey(record.getId())) {
			return true;
		}
		return false;
	}
 }