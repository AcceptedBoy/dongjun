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
	//电能表子模块
	public static final int MODULE_ELECTRICITY = 2;
	//温度子模块
	public static final int MODULE_TEMPERATURE = 3;
	//GPRS子模块
	public static final int MODULE_GPRS = 4;
	/**************************** end ****************************/
	
	/************************ 子模块报警 ************************/
	//200 300
	public static final int HITCH_UNKNOWN = 1;
	//201
	public static final int HITCH_SHI_YA = 201;
	//202
	public static final int HITCH_QIAN_YA = 202;
	//203
	public static final int HITCH_GUO_YA = 203;
	//204
	public static final int HITCH_DUAN_XIANG = 204;
	//205
	public static final int HITCH_QUAN_SHI_YA = 205;
	//206
	public static final int HITCH_SHI_LIU = 206;
	//207
	public static final int HITCH_GUO_LIU = 207;
	//208
	public static final int HITCH_DUAN_LIU = 208;
	//301
	public static final int HITCH_OVER_TEMPERATURE = 301;
	//310
	public static final int HITCH_ELECTRICITY_LACK = 310;
	
//	//200、300
//	public static final String HITCH_UNKNOWN = "未知报警";
//	//201
//	public static final String HITCH_SHI_YA = "电能表失压";
//	//202
//	public static final String HITCH_QIAN_YA = "电能表失压";
//	//203
//	public static final String HITCH_GUO_YA = "电能表过压";
//	//204
//	public static final String HITCH_DUAN_XIANG = "电能表断相";
//	//205
//	public static final String HITCH_QUAN_SHI_YA = "电能表全失压";
//	//206
//	public static final String HITCH_SHI_LIU = "电能表失流";
//	//207
//	public static final String HITCH_GUO_LIU = "电能表过流";
//	//208
//	public static final String HITCH_DUAN_LIU = "电能表断流";
//	//301
//	public static final String HITCH_OVER_TEMPERATURE = "监测温度超过设备所设阈值";
//	//310
//	public static final String HITCH_ELECTRICITY_LACK = "温度传感器电量过少";
	/****************************** end ********************************/
	
	public static String getHitchReason(int type) {
		switch(type) {
		case HITCH_UNKNOWN : return "未知报警";
		case HITCH_SHI_YA: return "电能表失压";
		case HITCH_QIAN_YA: return "电能表欠压";
		case HITCH_GUO_YA: return "电能表过压";
		case HITCH_DUAN_XIANG: return "电能表断相";
		case HITCH_QUAN_SHI_YA: return "电能表全失压";
		case HITCH_SHI_LIU: return "电能表失流";
		case HITCH_GUO_LIU: return "电能表过流";
		case HITCH_DUAN_LIU: return "电能表断流";
		case HITCH_OVER_TEMPERATURE : return "监测温度超过设备所设阈值";
		case HITCH_ELECTRICITY_LACK: return "温度传感器电量过少";
		default : return null;
		}
	}

}
