package com.gdut.dongjun.core.message.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.gdut.dongjun.core.message.MessageCreator;
import com.gdut.dongjun.util.TemperatureDeviceCommandUtil;

/**
 * 电能表DLT645_2007规约
 * @author Gordan_Deng
 * @date 2017年4月15日
 */
@Component
public class DLT645_07MessageCreator implements MessageCreator {

	private static final String CODE_00 = "00";
	private static final String CODE_68 = "68";
	private static final String CODE_16 = "16";
	private static final String CODE_11 = "11";
	private static final String CODE_04 = "04";
	private static final String CODE_0 = "0";
	//16进制数字字符串
	private static final String HEX = "0123456789abcdef";
	//帧校验和CS被除数
	private static final int checkSumFactor = 256;
	//寄存器地址
	private static final List<String> ADDRESS_LIST = new ArrayList<String>() {
		{
			//电压ABC相
			add("02010100");
			add("02010200");
			add("02010300");
			//电流ABC相
			add("02020100");
			add("02020200");
			add("02020300");
			//总功率因数、功率ABC相
			add("02060000");
			add("02060100");
			add("02060200");
			add("02060300");
		}
	};

	
	public List<String> generateTotalCall(String address) {
		List<String> msgList = new ArrayList<String>();
		for (String dataField : ADDRESS_LIST) {
			msgList.add(getReadMessage(address, TemperatureDeviceCommandUtil.reverseString(dataField)));
		}
		return msgList;
	}
	
	/**
	 * 读数据请求帧
	 * @param address
	 * @param dataField
	 * @return
	 */
	public String getReadMessage(String address, String dataField) {
		String msg = CODE_68 + address + CODE_68 + CODE_11 + CODE_04 + dataField;
		return msg + getCheckSum(msg) + CODE_16;
	}
	
	/**
	 * 得到校验和
	 * @param msg
	 * @return
	 */
	public String getCheckSum(String msg) {
		char[] data = msg.replace(" ", "").toCharArray();
		int sum = 0;
		for (int i = 0; i < data.length; i += 2) {
			sum += byteToHex(data[i], data[i + 1]);
		}
		int remainder = sum % checkSumFactor;
		//补0
		if (remainder < 16) {
			return CODE_0 + Integer.toHexString(remainder);
		} else {
			return Integer.toHexString(remainder); 
		}
	}

	/**
	 * byte转16进制，用10进制数字表示
	 * @param data
	 * @return
	 */
	public int byteToHex(char[] data) {
		if (data.length < 2) {
			return 0;
		}
		return 16 * charToHex(data[0]) + charToHex(data[1]);
	}
	
	/**
	 * byte转16进制，用10进制数字表示
	 * @param pre
	 * @param post
	 * @return
	 */
	public int byteToHex(char pre, char post) {
		return 16 * charToHex(pre) + charToHex(post);
	}
	
	/**
	 * char转16进制，用10进制数字表示
	 * 0 48
	 * 9 57
	 * a 97
	 * f 102
	 * A 65
	 * Z 70
	 * @param c
	 * @return
	 */
	public int charToHex(char c) {
		int num = (int)c;
		if (num >= 48 && num <= 57) {
			return num - 48;
		}
		else if (num >= 97 && num <= 102) {
			return num - 87;
		}
		else if (num >= 65 && num <= 70) {
			return num - 55;
		}
		return 0;
	}
	
	public char intToHex(int num) {
		return HEX.charAt(num);
	}
	
	/*
	 * 这个例子用于比较String.subString和上面方法的速度
	 * 快了4倍以上
	 */
//	public static void main(String[] args) {
//		String a = "681122ab16";
//		char[] c = a.toCharArray();
//		long nano = System.currentTimeMillis();
//		int sum;
//		for (int i = 0; i < 10000; i++) {
//			sum = 0;
//			for (int j = 0; j < a.length(); j = j + 2) {
//				sum += Integer.parseInt(a.substring(j, j + 2),
//						16);
//			}
//		}
//		System.out.println(System.currentTimeMillis() - nano);
//		nano = System.currentTimeMillis();
//		for (int i = 0; i < 10000; i++) {
//			sum = 0;
//			for (int j = 0; j < a.length(); j = j + 2) {
//				sum += byteToHex(c[j], c[j + 1]);
//			}
//		}
//		System.out.println(System.currentTimeMillis() - nano);
//	}
	
}
