package com.gdut.dongjun.util;

public class Standard101Util {

	private static final String CODE_10 = "10";
	private static final String CODE_16 = "16";
	private static final String CODE_49 = "49";
	private static final String CODE_40 = "40";
	private static final String CODE_00 = "00";
	
	/**
	 * 主站发起链路请求
	 * @param addr
	 * @return
	 */
	public static String getLinkRequest(String addr) {
		return getFixedFrame(CODE_49, addr);
	}
	
	/**
	 * 主站复位远方链路
	 * @param addr
	 * @return
	 */
	public static String getResetLinkRequest(String addr) {
		return getFixedFrame(CODE_40, addr);
	}
	
	/**
	 * 主站确认帧
	 * @param addr
	 * @return
	 */
	public static String getConfirmFrame(String addr) {
		return getFixedFrame(CODE_00, addr);
	}

	public static String getFixedFrame(String control, String address) {
		String msg = control + address;
		String check = CS(msg);
		return CODE_10 + control + address + check + CODE_16;
	}

	private static String CS(String data) {
		int sum = 0;
		for (int i = 0; i < data.replace(" ", "").length(); i = i + 2) {
			sum += Integer.parseInt(data.replace(" ", "").substring(i, i + 2), 16);
		}
		sum %= 256;
		String check = Integer.toHexString(sum);
		if (check.matches(".")) {
			check = "0" + check;
		}
		return check;
	}
}
