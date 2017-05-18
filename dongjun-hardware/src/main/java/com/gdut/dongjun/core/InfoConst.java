package com.gdut.dongjun.core;

public class InfoConst {
	
	//001
	private static final String MODULE_ONLINE = "设备上线";
	//002
	private static final String MODULE_OFFLINE = "设备下线";
	//003
	private static final String MODULE_NOT_DEFINED = "设备未定义";
	
	public static String getInfo(int type) {
		switch (type) {
		case 1 : return MODULE_ONLINE;
		case 2 : return MODULE_OFFLINE;
		case 3 : return MODULE_NOT_DEFINED;
		default : return null;
		}
	}
}
