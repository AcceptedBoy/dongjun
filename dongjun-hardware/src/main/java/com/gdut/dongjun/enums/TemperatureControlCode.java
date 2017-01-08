package com.gdut.dongjun.enums;

public enum TemperatureControlCode {

	PRE_CONTROL("0", "53", "预备控制域"), 
	CONTROL("1", "73", "控制域"),
	TOTALCALL("2", "64010601", "总召"), 
	CONNECTION("3", "1007FFFF0516", "主站建立连接"),
	TIME("4", "67010601", "对时"),
	FIXED_VALUE_CONTROL("5", "F3", "召唤定值控制域"),
	FIXED_VALUE("6", "7A010601", "召唤定值"),
	CHANGE_FIXED_VALUE("7", "71", "修改定值"),
	PARAMETER("8", "0F", "参数限定词"),
	FIXED_VALUE_CONTROL2("9", "F4", "返回定值确认控制域");

	@SuppressWarnings("unused")
	private String sign;// 标示
	private String code;// 16进制代码
	@SuppressWarnings("unused")
	private String statement;// 说明

	private TemperatureControlCode(String sign, String code, String statement) {
		this.sign = sign;
		this.code = code;
		this.statement = statement;
	}

	public static TemperatureControlCode valueOf(int sign) {

		switch (sign) {
		case 0:
			return CONTROL;
		case 1:
			return TOTALCALL;
		}
		return null;
	}
	

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.code;
	}

	public int value() {
		// TODO Auto-generated method stub
		return Integer.parseInt(sign);
	}

}
