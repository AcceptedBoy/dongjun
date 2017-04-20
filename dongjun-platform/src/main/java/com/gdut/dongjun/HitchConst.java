package com.gdut.dongjun;
/**
 * 报警常数，一个数字三位
 * 电能表模块的报警参数2开头
 * 温度模块的报警参数3开头
 * @author Gordan_Deng
 * @date 2017年4月20日
 */
public class HitchConst {

	public static final int MODULE_TEMPERATURE = 3;
	
	public static final int MODULE_ELECTRICITY = 2;
	
	//301
	public static final String HITCH_OVER_TEMPERATURE = "监测温度超过设备所设阈值";
	
	//310
	public static final String HITCH_ELECTRICITY_LACK = "温度传感器电量过少";
	
	public static String getHitchReason(int type) {
		switch(type) {
		case 300 : return HITCH_OVER_TEMPERATURE;
		case 310: return HITCH_ELECTRICITY_LACK;
		default : return "";
		}
	}

}
