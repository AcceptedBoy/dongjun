package com.gdut.dongjun;

public class test {

	public static void main(String[] args) {
		int a = 104;
		String b = Integer.toHexString(a & 0xFF);
		System.out.println(b);
	}
}
