package com.gdut.dongjun.core.handler.msg_decoder.example;

/**
 * 温度报文例子
 * @author Gordan_Deng
 * @date 2017年3月13日
 */
public class TemperatureExample {
	
//	public static void main(String[] args) {
//		run();
//	}

	public static void run() {
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
}
