package com.gdut.dongjun.service.impl;

import java.util.List;

/**
 * @Title: ZNodeTree.java
 * @Package com.gdut.dongjun.service.impl
 * @Description: TODO
 * @author Sherlock-lee
 * @author Gordan_Deng
 * @date 2015年8月3日 下午5:10:57
 * @version V1.0
 */
public class ZTreeNode {
	/**
	 * @ClassName: ZNodeTree
	 * @Description: TODO
	 * @author Sherlock-lee
	 * @date 2015年8月3日 下午5:10:57
	 */
	private String id;
	private String name; // 节点显示的值
	private boolean open = true; // 是否展开节点
	private List<ZTreeNode> children;// 子节点
	private String parentName;
	private String platformGroupId;//小组id
	private int type;//类型标志
	private String address;
	private String showName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public String getPlatformGroupId() {
		return platformGroupId;
	}

	public void setPlatformGroupId(String platformGroupId) {
		this.platformGroupId = platformGroupId;
	}

	public List<ZTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<ZTreeNode> children) {
		this.children = children;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}
	
	public String getShowName() {
		return showName;
	}
	
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	public String getParentName() {
		return parentName;
	}
}
