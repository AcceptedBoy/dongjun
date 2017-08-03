package com.gdut.dongjun.service.base;

import java.util.List;
import java.util.Map;

import com.gdut.dongjun.domain.po.CommonBean;

/**
 * 给有假删除标志位的表用
 * @author Gordan_Deng
 * @date 2017年7月29日
 */
public interface DelTagHolderService<T extends CommonBean> extends EnhancedService<T> {
	
	/**
	 * 重新启用假删除后的记录
	 * @param id
	 * @return
	 */
	int updateWithTag(String id);
	
	/**
	 * 假删除
	 * @param id
	 * @return
	 */
	int deleteWithTag(String id);
	
	/**
	 * 多条件查询未假删除的记录
	 * @param map
	 * @return
	 */
	List<T> selectByParametersNoDel(Map<String, Object> map);
	
	/**
	 * 查找未假删除的记录
	 * @param id
	 * @return
	 */
	T selectByPrimaryKeyNoDel(String id);
}
