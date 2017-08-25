package com.gdut.dongjun.domain.dao.base.impl;

import java.util.List;
import java.util.Map;

import com.gdut.dongjun.domain.dao.base.DelTagHolderMapper;

/**
 * 给有假删除标志位的表用
 * @author Gordan_Deng
 * @date 2017年7月29日
 * @param <T>
 */
public class DelTagHolderDAOImpl<T> extends SinglePrimaryKeyBaseDAOImpl<T> implements DelTagHolderMapper<T> {

	@Override
	public int updateWithTag(String id) {
		return template.update(getNamespace("updateWithTag"), id);
	}

	@Override
	public int deleteWithTag(String id) {
		return template.update(getNamespace("deleteWithTag"), id);
	}

	@Override
	public List<T> selectByParametersNoDel(Map<String, Object> map) {
		return template.selectList(getNamespace("selectByParametersNoDel"), map);
	}

	@Override
	public T selectByPrimaryKeyNoDel(String id) {
		return template.selectOne(getNamespace("selectByPrimaryKeyNoDel"), id);
	}

}
