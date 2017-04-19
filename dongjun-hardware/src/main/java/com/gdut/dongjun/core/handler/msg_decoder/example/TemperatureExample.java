package com.gdut.dongjun.core.handler.msg_decoder.example;

/**
 * 温度报文例子
 * 高压 0901遥测变化20字节 
 * 0902??? 
 * 09全遥测131字节   09遥测变位20字节
 * 01全遥信90字节  
 * 03双点遥信  
 * 1F双点遥信变位事件
 * 680e0e68f4c90009010301c9000c409eff007d16  0901什么鬼
 * 单点遥信 682b2b68f4700001a014017000010000000000000000000001000000000000000001010000000000000000000000008e16
 * 未知报文
 * 684d4d68f40300000001b7140103000000002802140d01e2010001010101010101000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000fd16
 * 684d4d68f40400000001b714010400000000080c160701e2010001010101010101000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000e516
 * 684d4d68f40500000001b714010500000000382f171601e20100010101010101010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000004a16
 * @author Gordan_Deng
 * @date 2017年3月13日
 */
public class TemperatureExample {
	
	private static final int BYTE = 2;
	
//	public static void main(String[] args) {
//		quan_yao_xin();
//	}

	public static void quan_yao_ce() {
		String[] strs = new String[65];
//		String a = "68d9d968f40100000009c1140101000000000339100701e2004000000000aa0000a40000a70000ad0000aa0000a600000000000000000000000000000000000000000000000000000000000000ffb900ffc500ffa900ffcb00ffcc00ffc2000000000000000000000000000000000000000000000000000000000000000022000022000021000021000022000022000000000000000000000000000000000000000000000000000000000000000001000001000001000001000001000001000000000000000000000000000000000000000000000000000000000000008716";
		String a = "68d9d968f40100000009c1140101000000001e38100701e2004000000000aa0000a40000aa0000ad0000aa0000a600000000000000000000000000000000000000000000000000000000000000ffba00ffc500ffaa00ffcc00ffcc00ffc200000000000000000000000000000000000000000000000000000000000000002200002200002100002100002200002200000000000000000000000000000000000000000000000000000000000000000100000100000100000100000100000100000000000000000000000000000000000000000000000000000000000000a716";
		System.out.println(a.substring(24 * 2, 24 * 2 + 4));
		String b = a.substring(24 * 2 + 4, a.length() - 4);
		int i = 0;
		for (; i < b.length() / 6; i++) {
			strs[i] = b.substring(i * 6, i * 6 + 6);
		}
		
		System.out.println("-----------------传感器温度值-----------------");
		for (i = 0; i < 16; i++) {
			System.out.println(strs[i]);
		}
		System.out.println("-----------------传感器RSSI信号值-----------------");
		for (; i < 16*2; i++) {
			System.out.println(strs[i]);
		}
		System.out.println("-----------------传感器电压值-----------------");
		for (; i < 16*3; i++) {
			System.out.println(strs[i]);
		}
		System.out.println("-----------------传感器在线情况-----------------");
		for (; i < 16*4; i++) {
			System.out.println(strs[i]);
		}
		
		System.out.println("未知 " + strs[i]);
	}
	
	public static void quan_yao_xin() {
		String a = "684d4d68f40300000001b7140103000000002802140d01e2010001010101010101000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000fd16";
		int i = 0;
		System.out.println("报文：\n" + a);
		System.out.println("68 长度 长度 68 f4 : \n" + a.substring(0, 5 * BYTE));
		System.out.println("地址 : \n" + a.substring(5 * BYTE, 9 * BYTE));
		System.out.println("遥信值，一个字节一个值 : \n" + a.substring(26 * BYTE, a.length() - 2 * BYTE));
		System.out.println("校验值 : \n" + a.substring(a.length() - 2 * BYTE, a.length() - 1 * BYTE));
		
	}
}
