package com.gdut.dongjun.core;

public class InfoConst {

	// 001
	public static final int MODULE_ONLINE = 1;
	// 002
	public static final int MODULE_OFFLINE = 2;
	// 003
	public static final int MODULE_NOT_DEFINED = 3;

	// //001
	// private static final String MODULE_ONLINE = "设备上线";
	// //002
	// private static final String MODULE_OFFLINE = "设备下线";
	// //003
	// private static final String MODULE_NOT_DEFINED = "设备未定义";

	public static String getInfo(int type) {
		switch (type) {
		case MODULE_ONLINE:
			return "设备上线";
		case MODULE_OFFLINE:
			return "设备下线";
		case MODULE_NOT_DEFINED:
			return "设备未定义";
		default:
			return null;
		}
	}
}
