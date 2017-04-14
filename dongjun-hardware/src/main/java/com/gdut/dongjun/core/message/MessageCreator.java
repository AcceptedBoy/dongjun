package com.gdut.dongjun.core.message;

public interface MessageCreator {

	String CODE_00 = "00";
	String CODE_68 = "68";
	String CODE_16 = "16";
	
	String generateTotalCall(char[] data);
}
