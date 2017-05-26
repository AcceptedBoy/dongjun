package com.gdut.dongjun.core.message.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.gdut.dongjun.core.message.MessageCreator;
import com.gdut.dongjun.util.TemperatureDeviceCommandUtil;

/**
 * 电能表DLT645_1997规约
 * 读数据68 地址 68 01 02 DI0 DI1 CS 16
 * @author Gordan_Deng
 * @date 2017年5月15日
 */
@Component
public class DLT645_97MessageCreator implements MessageCreator {

	private static final String CODE_00 = "00";
	private static final String CODE_68 = "68";
	private static final String CODE_16 = "16";
	private static final String CODE_11 = "11";
	private static final String CODE_04 = "04";
	private static final String CODE_02 = "02";
	private static final String CODE_01 = "01";
	private static final String CODE_0 = "0";
	//16进制数字字符串
	private static final String HEX = "0123456789abcdef";
	//帧校验和CS被除数
	private static final int checkSumFactor = 256;
	//寄存器地址
	private static final List<String> ADDRESS_LIST = new ArrayList<String>() {
		{
			//电压ABC相
			add("44e9");	//b611
			add("45e9");	//b612
			add("46e9");	//b613
			//电流ABC相
			add("54e9");	//b621
			add("55e9");	//b622
			add("56e9");	//b623
			//总功率因数、功率ABC相
			add("63e9");	//b630
			add("64e9");	//b631
			add("65e9");	//b632
			add("66e9");	//b633
			
		}
	};

	
	public List<String> generateTotalCall(String address) {
		List<String> msgList = new ArrayList<String>();
		for (String dataField : ADDRESS_LIST) {
			msgList.add(getReadMessage(address, dataField));
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
		String msg = CODE_68 + address + CODE_68 + CODE_01 + CODE_02 + dataField;
		return msg + getCheckSum(msg) + CODE_16;
	}
	
	/**
	 * 得到校验和，计算从帧起始符到校验码之前所有字节的模256的和，即各字节二进制算术和，忽略超过256的溢出值
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
	
//	public static void main(String[] args) {
//		
//		DLT645_97MessageCreator c = new DLT645_97MessageCreator();
//		for (String s : c.generateTotalCall("400100010800")) {
//			System.out.println(s);
//		}
//	}
	
}
