package com.gdut.dongjun.domain.dao;

import java.util.List;

import com.gdut.dongjun.domain.dao.base.DelTagHolderMapper;
import com.gdut.dongjun.domain.po.Company;

public interface CompanyMapper extends DelTagHolderMapper<Company> {

	/**
	 * 模糊搜索
	 * @param name
	 * @return
	 */
	public List<Company> fuzzySearch(String name);

}