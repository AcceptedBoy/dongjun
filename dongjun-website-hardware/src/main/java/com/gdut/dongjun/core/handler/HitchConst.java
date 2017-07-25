package com.gdut.dongjun.core.handler;

import org.apache.log4j.Logger;

public class HitchConst {
	
	private static Logger logger = Logger.getLogger(HitchConst.class);

	/**
	 * 根据标志位得到报警原因
	 * @param value
	 * @return
	 */
	public static String getHitchReason(String value) {
		switch (value) {
		case "15" : 
			return "手动合闸";
		case "16" : 
			return "手动分闸";
		case "18" : 
			return "遥控器合闸";
		case "19" : 
			return "遥控器分闸";
		case "24" : 
			return "过流一段";
		case "25" : 
			return "过流二段";
		case "26" : 
			return "过流三段";
		case "27" : 
			return "零序过流一段";
		case "28" : 
			return "零序过流二段";
		case "29" : 
			return "零序过流三段";
		case "37" : 
			return "快速分闸";
		case "3b" : 
			return "超温跳闸";
		default : logger.info("未知报警地址" + value); return "未知报警";
		}
	}
}
