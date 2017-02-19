package com.gdut.dongjun.domain.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.CompanyMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.Company;
import com.gdut.dongjun.domain.po.ControlMearsureCurrent;

/**
 * 
 * @author Sherlock-lee
 * @date 2015年11月14日 下午4:56:43
 * @see TODO
 * @since 1.0
 */
@Repository
public class CompanyDAOImpl extends SinglePrimaryKeyBaseDAOImpl<Company> implements CompanyMapper {

	@Override
	public List<Company> fuzzySearch(String name) {
		return template.selectList(getNamespace("fuzzySearch"), name);
	}

}
