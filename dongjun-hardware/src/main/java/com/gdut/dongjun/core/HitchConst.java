package com.gdut.dongjun.core;
/**
 * 报警常数，一个数字三位
 * 电能表模块的报警参数2开头
 * 温度模块的报警参数3开头
 * @author Gordan_Deng
 * @date 2017年4月20日
 */
public class HitchConst {
	
	/**************************** 子模块 ****************************/
	//温度子模块
	public static final int MODULE_TEMPERATURE = 3;
	//电能表子模块
	public static final int MODULE_ELECTRICITY = 2;
	/**************************** end ****************************/
	
	/************************ 子模块报警 ************************/
	//200、300
	public static final String HITCH_UNKNOWN = "未知报警";
	//201
	public static final String HITCH_SHI_YA = "电能表失压";
	//202
	public static final String HITCH_QIAN_YA = "电能表失压";
	//203
	public static final String HITCH_GUO_YA = "电能表过压";
	//204
	public static final String HITCH_DUAN_XIANG = "电能表断相";
	//205
	public static final String HITCH_QUAN_SHI_YA = "电能表全失压";
	//206
	public static final String HITCH_SHI_LIU = "电能表失流";
	//207
	public static final String HITCH_GUO_LIU = "电能表过流";
	//208
	public static final String HITCH_DUAN_LIU = "电能表断流";
	//301
	public static final String HITCH_OVER_TEMPERATURE = "监测温度超过设备所设阈值";
	//310
	public static final String HITCH_ELECTRICITY_LACK = "温度传感器电量过少";
	/****************************** end ********************************/
	
	public static String getHitchReason(int type) {
		switch(type) {
		case 200: return HITCH_UNKNOWN;
		case 201: return HITCH_SHI_YA;
		case 202: return HITCH_QIAN_YA;
		case 203: return HITCH_GUO_YA;
		case 204: return HITCH_DUAN_XIANG;
		case 205: return HITCH_QUAN_SHI_YA;
		case 206: return HITCH_SHI_LIU;
		case 207: return HITCH_GUO_LIU;
		case 208: return HITCH_DUAN_LIU;
		case 300: return HITCH_UNKNOWN;
		case 301 : return HITCH_OVER_TEMPERATURE;
		case 310: return HITCH_ELECTRICITY_LACK;
		default : return "";
		}
	}

}
