package com.gdut.dongjun.service;

import java.util.List;

import com.gdut.dongjun.domain.po.Company;
import com.gdut.dongjun.service.base.BaseService;

/**
 * @Title: UserService.java
 * @Package com.gdut.dongjun.service.system
 * @Description: TODO
 * @author Sherlock-lee
 * @date 2015年7月24日 下午2:34:11
 * @version V1.0
 */
public interface CompanyService extends BaseService<Company>{
	
	static final String UPDATE_POSTFIX = "_is_update";
	
	/**
	 * 更改设备树状图
	 * @param id
	 */
	public void updateChartCache(String companyId);
	
	/**
	 * 确认设备进行过修改
	 * @param id
	 */
	public void isModifiedChart(String companyId);
	
	/**
	 * 根据公司id得到设备树状图
	 * @param companyId
	 * @return
	 */
	public Object getChart(String companyId);
	
	/**
	 * 模糊搜索
	 * @param name
	 * @return
	 */
	public List<Company> fuzzySearch(String name);
	
}
