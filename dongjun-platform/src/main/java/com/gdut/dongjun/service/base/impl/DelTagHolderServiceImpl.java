package com.gdut.dongjun.service.base.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.gdut.dongjun.domain.dao.base.DelTagHolderMapper;
import com.gdut.dongjun.domain.po.CommonBean;
import com.gdut.dongjun.service.base.DelTagHolderService;

public abstract class DelTagHolderServiceImpl<T extends CommonBean> 
			extends EnhancedServiceImpl<T> implements DelTagHolderService<T> {

	@Autowired
	protected DelTagHolderMapper<T> delTagMapper;
	
	@Override
	public int updateWithTag(String id) {
		return delTagMapper.updateWithTag(id);
	}

	@Override
	public int deleteWithTag(String id) {
		return delTagMapper.deleteWithTag(id);
	}

	@Override
	public List<T> selectByParametersNoDel(Map<String, Object> map) {
		return delTagMapper.selectByParametersNoDel(map);
	}

	@Override
	public T selectByPrimaryKeyNoDel(String id) {
		return delTagMapper.selectByPrimaryKeyNoDel(id);
	}

}
