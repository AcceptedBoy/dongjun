package com.gdut.dongjun.domain.po.enums;

/**
 * @author Sherlock-lee
 * @date 2015年11月10日 下午8:47:55
 * @see TODO
 * @since 1.0
 */
public enum Protocol {

	LOW_VOLTAGE_07_PROTOCAL, HIGH_VOLTAGE_101_ROTOCAL, CONTROL_MEASURE_DEVICE_PROTOCAL;
	
	public static boolean hasContain(Protocol protocol) {
		
		for(int i = Protocol.values().length - 1; i >= 0; --i) {
			if(Protocol.values()[i] == protocol) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasContain(Integer protocol) {
		
		if(protocol <= Protocol.values().length) {
			return true;
		}
		return false;
	}
}
