package com.gdut.dongjun.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.CompanyMapper;
import com.gdut.dongjun.domain.po.Company;
import com.gdut.dongjun.service.CompanyService;
import com.gdut.dongjun.service.ZTreeNodeService;
import com.gdut.dongjun.service.base.impl.EnhancedServiceImpl;
import com.gdut.dongjun.service.cache.CacheService;

/**   
 * @Title: UserServiceImpl.java 
 * @Package com.gdut.dongjun.service.impl.system 
 * @Description: TODO 
 * @author Sherlock-lee   
 * @date 2015年7月24日 下午2:33:08 
 * @version V1.0   
 */
@Service
public class CompanyServiceImpl extends EnhancedServiceImpl<Company> implements CompanyService{
	/** 
	 * @ClassName: UserServiceImpl 
	 * @Description: TODO
	 * @author Sherlock-lee
	 * @date 2015年7月24日 下午2:33:08 
	 */
	@Autowired
	private CompanyMapper companyMapper;
	
	@Autowired
	private ZTreeNodeService treeService;
	
	@Resource(name="EhCacheService")
	private CacheService ehCacheService;
	
	@Autowired
	private CompanyMapper mapper;

	@Override
	protected boolean isExist(Company record) {
		
		if (record != null
				&& companyMapper.selectByPrimaryKey(record.getId()) != null) {

			return true;
		} else {
			return false;
		}
	}

	@Override
	public void updateChartCache(String id) {
		ehCacheService.put(id, treeService.getSwitchTree(id));
		ehCacheService.put(id + CompanyService.UPDATE_POSTFIX, Integer.valueOf(0));
	}

	@Override
	public void isModifiedChart(String companyId) {
		ehCacheService.put(companyId + CompanyService.UPDATE_POSTFIX, Integer.valueOf(1));
	}

	@Override
	public Object getChart(String companyId) {
		Integer isUpdate = (Integer)(ehCacheService.get(companyId + CompanyService.UPDATE_POSTFIX));
		if (null != isUpdate && 1 == isUpdate) {
			updateChartCache(companyId);
		}
		return ehCacheService.get(companyId);
	}

	@Override
	public List<Company> fuzzySearch(String name) {
		StringBuilder sb = new StringBuilder();
		sb.append("%").append(name).append("%");
		return mapper.fuzzySearch(sb.toString());
	}
	
}
