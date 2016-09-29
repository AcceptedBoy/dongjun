package com.symon.service.base.impl;

import java.util.List;
import java.util.Map;

import com.symon.dao.base.SinglePrimaryKeyBaseMapper;
import com.symon.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @Title: BaseServiceImpl.java
 * @Package com.gdut.dongjun.service.impl.system
 * @Description: TODO
 * @author Sherlock-lee
 * @date 2015年7月24日 下午2:33:08
 * @version V1.0
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

	@Autowired
	private SinglePrimaryKeyBaseMapper<T> baseMapper;

	@Override
	public boolean deleteByPrimaryKey(String id) {

		if(baseMapper.deleteByPrimaryKey(id) == 1) {
			return true;
		}
		return false;
	}

	@Override
	public int insert(T record) {
		
		return baseMapper.insert(record);
	}

	@Override
	public int insertSelective(T record) {
		
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

			return baseMapper.updateByPrimaryKey(record);
		} else {

			return baseMapper.insert(record);
		}
	}

	@Override
	public int updateByPrimaryKeySelective(T record) {
		
		if (isExist(record)) {

			return baseMapper.updateByPrimaryKeySelective(record);
		} else {

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
