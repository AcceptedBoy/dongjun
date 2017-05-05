package com.gdut.dongjun.util;

import java.util.regex.Pattern;

import com.gdut.dongjun.exception.ValidatorException;

public class Assert {

	public static void notNull(Object obj, String message) {
		if (null == obj) {
			throw new ValidatorException(message);
		}
	}
	
	public static boolean notNull(Object obj) {
		if (null == obj) {
			return false;
		}
		return true;
	}
	
	private static final Pattern pattern = Pattern.compile("[0-9]*");  
	public static boolean isNumeric(String str){  
	    return pattern.matcher(str).matches();     
	}
	
}
