package com.gdut.dongjun.service.base.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.gdut.dongjun.domain.dao.base.SinglePrimaryKeyBaseMapper;
import com.gdut.dongjun.domain.po.CommonBean;
import com.gdut.dongjun.service.base.EnhancedService;

public abstract class EnhancedServiceImpl<T extends CommonBean> implements EnhancedService<T> {
	
	@Autowired
	protected SinglePrimaryKeyBaseMapper<T> baseMapper;
	
	@Override
	public boolean deleteByPrimaryKey(String id) {

		if(baseMapper.deleteByPrimaryKey(id) == 1) {
			return true;
		}
		return false;
	}

	@Override
	public int insert(T record) {
		CommonBean bean = (CommonBean)record;
		bean.setGmtCreate(new Date());
		bean.setGmtModified(new Date());
		return baseMapper.insert(record);
	}

	@Override
	public int insertSelective(T record) {
		CommonBean bean = (CommonBean)record;
		bean.setGmtModified(new Date());
		return baseMapper.insertSelective(record);
	}

	@Override
	public T selectByPrimaryKey(String id) {
		
		return baseMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<T> selectByParameters(Map<String, Object> map) {
		
		return baseMapper.selectByParameters(map);
	}

	@Override
	public int updateByPrimaryKey(T record) {
		
		if (isExist(record)) {
			CommonBean bean = (CommonBean)record;
			bean.setGmtModified(new Date());
			return baseMapper.updateByPrimaryKey(record);
		} else {
			CommonBean bean = (CommonBean)record;
			bean.setGmtCreate(new Date());
			bean.setGmtModified(new Date());
			return baseMapper.insert(record);
		}
	}

	@Override
	public int updateByPrimaryKeySelective(T record) {
		
		if (isExist(record)) {
			CommonBean bean = (CommonBean)record;
			bean.setGmtModified(new Date());
			return baseMapper.updateByPrimaryKeySelective(record);
		} else {
			CommonBean bean = (CommonBean)record;
			bean.setGmtCreate(new Date());
			bean.setGmtModified(new Date());
			return baseMapper.insert(record);
		}
	}

	/**
	 * 
	 * @Title: isExist
	 * @Description: TODO
	 * @param @param record
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	protected abstract boolean isExist(T record);

}
