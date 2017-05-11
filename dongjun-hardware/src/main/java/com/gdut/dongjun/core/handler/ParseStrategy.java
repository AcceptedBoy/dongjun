package com.gdut.dongjun.core.handler;

/**
 * 协议解析策略
 * @author Gordan_Deng
 * @date 2017年5月11日
 */
public abstract class ParseStrategy implements MessageParser {
	
	protected String parserId;
	
//	public void changeStrategy(Channel channel, String parseId);

	public String getParserId() {
		return parserId;
	}

	public void setParserId(String parserId) {
		this.parserId = parserId;
	}
	
	
}
