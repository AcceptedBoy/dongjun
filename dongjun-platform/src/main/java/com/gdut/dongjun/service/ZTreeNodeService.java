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
	 * 超管返回设备树
	 * @return
	 */
	public List<ZTreeNode> getSwitchTree();
	
	/**
	 * 返回用户设备树
	 * @param companyId
	 * @return
	 */
	public List<ZTreeNode> getSwitchTree(String userId);
}
