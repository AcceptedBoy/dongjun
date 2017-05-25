package com.gdut.dongjun.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.gdut.dongjun.dao.SinglePrimaryKeyBaseMapper;
import com.gdut.dongjun.po.AbstractBean;
import com.gdut.dongjun.service.EnhancedService;

public abstract class EnhancedServiceImpl<T extends AbstractBean> implements EnhancedService<T> {
	
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
		AbstractBean bean = (AbstractBean)record;
		bean.setGmtCreate(new Date());
		bean.setGmtModified(new Date());
		return baseMapper.insert(record);
	}

	@Override
	public int insertSelective(T record) {
		AbstractBean bean = (AbstractBean)record;
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
			AbstractBean bean = (AbstractBean)record;
			bean.setGmtModified(new Date());
			return baseMapper.updateByPrimaryKey(record);
		} else {
			AbstractBean bean = (AbstractBean)record;
			bean.setGmtCreate(new Date());
			bean.setGmtModified(new Date());
			return baseMapper.insert(record);
		}
	}

	@Override
	public int updateByPrimaryKeySelective(T record) {
		
		if (isExist(record)) {
			AbstractBean bean = (AbstractBean)record;
			bean.setGmtModified(new Date());
			return baseMapper.updateByPrimaryKeySelective(record);
		} else {
			AbstractBean bean = (AbstractBean)record;
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
