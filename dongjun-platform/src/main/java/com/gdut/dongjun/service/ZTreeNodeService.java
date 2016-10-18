package com.gdut.dongjun.service;

import com.gdut.dongjun.service.impl.ZTreeNode;

import java.util.List;

/**
 * @Title: CommandService.java
 * @Package com.gdut.dongjun.service
 * @Description: TODO
 * @author Sherlock-lee
 * @date 2015年7月30日 下午11:51:28
 * @version V1.0
 */
public interface ZTreeNodeService {

	/**
	 *
	 * @param company_id 公司id
	 * @param type 开关类型
	 * @return
	 */
	public List<ZTreeNode> getSwitchTree(String company_id, String type);

	public void groupTree(String companyId, Integer deviceType);
}
