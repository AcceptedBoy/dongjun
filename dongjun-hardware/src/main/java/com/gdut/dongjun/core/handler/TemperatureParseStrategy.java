package com.gdut.dongjun.core.handler;

import java.util.List;

/**
 * List<Object>返回：
 * 序号    类型    意义
 * 1       String  设备地址
 * 2       String  设备十进制地址
 * 
 * 以后的架构改动就是AbstractDataReceiver调用ParseStrategyAdaptor，然后调用ParseStrategy
 * @author Gordan_Deng
 * @date 2017年5月11日
 */
public class TemperatureParseStrategy extends ParseStrategy {

	@Override
	public List<Object> parse(char[] data) {
		
		return null;
	}

}
