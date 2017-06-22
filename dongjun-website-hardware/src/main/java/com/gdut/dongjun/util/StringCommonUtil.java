package com.gdut.dongjun.util;

import org.apache.commons.lang.StringUtils;

public class StringCommonUtil extends StringUtils {

	/**
	 * 返回在endTag在str第一次出现的位置，这个位置是在endTag的结尾字符所找到
	 * @param str	要进行切割的字符串
	 * @param endTag	结束标志位
	 * @return
	 */
	public static int getFirstIndexOfEndTag(String str, String endTag) {

		return getFirstIndexOfEndTag(str.toCharArray(), endTag);
	}

	public static int getFirstIndexOfEndTag(char[] strChar, String endTag) {

		char[] endTagChar = endTag.toCharArray();
		for(int length = strChar.length, i = 0; i < length; i++) {
			if(strChar[i] == endTagChar[0]) {
				int limit = endTagChar.length, j = 1;
				for(; j < limit; j++) {
					if(strChar[i + j] != endTagChar[j]) {
						break;
					}
				}
				if(j >= limit) {
					return i + j;
				}
			}
		}
		return -1;
	}
	
	public static int getFirstIndexOfEndTag(char[] strChar, int begin, String endTag) {

		char[] endTagChar = endTag.toCharArray();
		for(int length = strChar.length, i = begin; i < length; i++) {
			if(strChar[i] == endTagChar[0]) {
				int limit = endTagChar.length, j = 1;
				for(; j < limit; j++) {
					if(strChar[i + j] != endTagChar[j]) {
						break;
					}
				}
				if(j >= limit) {
					return i + j;
				}
			}
		}
		return -1;
	}
}
