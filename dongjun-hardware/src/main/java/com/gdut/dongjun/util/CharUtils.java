package com.gdut.dongjun.util;

import java.util.Arrays;

/**
 * TODO
 */
public class CharUtils {
	
	private static final int BYTE = 2;
	
	private static final String HEX_STR = "0123456789abcdef";

    public static char[] removeSpecChar(char[] src, char c) {
        int i = 0, j = 0;
        for(int length = src.length; i < length; ++i) {
            if(src[i] != c) { //equals
                src[j++] = src[i];
            }
        }
        while(j < i) {
            src[j++] = '\0'; //help gc
        }
        return src;
    }

    /**
     * 数组移除空格
     * @param src
     * @return
     */
    public static char[] removeSpace(char[] src) {
        return removeSpecChar(src, ' ');
    }

    /**
     * 比较从src数组的开始索引到结束索引是否都跟compareStr一致
     * @param src
     * @param beginIndex
     * @param endIndex
     * @param compareStr
     */
    public static boolean equals(char[] src, int beginIndex, int endIndex, char[] compareStr) {

        if (beginIndex < 0) {
            throw new StringIndexOutOfBoundsException(beginIndex);
        }
        if (endIndex > src.length) {
            throw new StringIndexOutOfBoundsException(endIndex);
        }
        if(endIndex - beginIndex != compareStr.length) {
            return false;
        }
        for(int i = 0; beginIndex < endIndex; ++beginIndex, ++i) {
            if(src[beginIndex] != compareStr[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 比较字符数组是否相等
     */
    public static boolean equals(char[] src, char[] compareStr) {
        return equals(src, 0, src.length, compareStr);
    }

    /**
     * 生成字符串，从{@code beginIndex}开始，到{@code endIndex}结束
     * @param src
     * @param beginIndex
     * @param endIndex
     * @return
     */
    public static String newString(char[] src, int beginIndex, int endIndex) {
       return String.valueOf(Arrays.copyOfRange(src, beginIndex, endIndex));
    }

    /**
     * 生成字符串，从{@code beginIndex}开始，到{@code endIndex}结束
     * @param src
     * @param beginIndex
     * @param endIndex
     * @return
     */
    public static String newStringWithIntern(char[] src, int beginIndex, int endIndex) {
        return null;
    }

    /**
     * 生成字符串
     */
    public static String newString(char[] src) {
        return String.valueOf(Arrays.copyOfRange(src, 0, src.length));
    }

    /**
     * {@code src}是否以{@code pre}开头
     * @param src
     * @param pre
     * @return
     */
    public static boolean startWith(char[] src, char[] pre) {
        return equals(src, 0, pre.length, pre);
    }

    /**
     * {@code src}是否以{@code pre}开头
     * @param src
     * @param pre
     * @return
     */
    public static boolean startWith(char[] src, String pre) {
        return String.valueOf(src).startsWith(pre);
    }
    
    /**
     * {@code src}是否以{@code end}结尾
     * @param src
     * @param pre
     * @return
     */
    public static boolean endsWith(char[] src, char[] end) {
        return equals(src, src.length - end.length, src.length, end);
    }
    
    /**
     * {@code src}是否以{@code end}结尾
     * @param src
     * @param pre
     * @return
     */
    public static boolean endsWith(char[] src, String pre) {
    	return String.valueOf(src).endsWith(pre);
    }
    
    /**
     * 截取char数组一部分并返回
     * @param src
     * @param begin
     * @param len
     * @return
     */
    public  static char[] subChars(char[] src, int begin, int len) {
    	//非法字符串
    	if (null == src) {
    		return null;
    	}
    	//非法开头
    	if (begin < 0 || begin >= src.length) {
    		return null;
    	}
    	//非法长度
    	if (len <= 0 || len > src.length) {
    		return null;
    	}
    	//截太多
    	if (begin + len > src.length) {
    		return null;
    	}
    	char[] tar = new char[len];
    	for (int i = begin,  j = 0; j < len; i++, j++) {
    		tar[j] = src[i];
    	}
    	return tar;
    }
    
    /**
     * 16进制char型数据转为int型
     * @param c
     * @return
     */
	public static int charToInt(char c) {
		return  HEX_STR.indexOf(c);
	}
	
	/**
	 * byte转16进制，用10进制数字表示
	 * @param data
	 * @return
	 */
	public static int byteToHex(char[] data) {
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
	public static int byteToHex(char pre, char post) {
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
	public static int charToHex(char c) {
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
	
	public static char intToHex(int num) {
		return HEX_STR.charAt(num);
	}
	
	/**
	 * char变10进制数字
	 * 0 48
	 * 9 57
	 * @param c
	 * @return
	 */
	public static int charToDecimal(char c) {
		int num = (int)c;
		if (num >= 48 && num <= 57) {
			return num - 48;
		}
		throw new IllegalArgumentException("参数错误，char转换十进制数字出错：" + c);
	}
	
	public static int charToDecimal(char pre, char post) {
		return 10 * charToDecimal(pre) + charToDecimal(post);
	}
	
	/**
	 * 以字节为单位反转char数组
	 * @param data
	 * @return
	 */
	public static char[] reverse(char[] data) {
		char temp;
		int length = data.length / BYTE / 2;
		int flag = (data.length / BYTE) % 2; // 0是偶数个字节，1为奇数个字节
		int mid = data.length / 2 - flag;
		for (int i = 0; i < length; i++) {
			temp = data[mid - BYTE * (i + 1)];
			data[mid - BYTE * (i + 1)] = data[mid + BYTE * (i + flag)];
			data[mid + BYTE * (i + flag)] = temp;

			temp = data[mid - BYTE * (i + 1) + 1];
			data[mid - BYTE * (i + 1) + 1] = data[mid + BYTE * (i + flag) + 1];
			data[mid + BYTE * (i + flag) + 1] = temp;
		}
		return data;
	}
	
	/**
	 * 将char数组中每隔char降低对应数字
	 * @param data
	 * @param po
	 * @return
	 */
	public static char[] lowerText(char[] data, int dec) {
		char[] hexArray = HEX_STR.toCharArray();
		for (int i = 0; i < data.length; i++) {
			int prePo = charToInt(data[i]);
			if (prePo < dec) {
				throw new IllegalArgumentException("参数错误，char数组每位降低对应数字时出错：" + String.valueOf(data));
			}
			data[i] = hexArray[prePo - 3];
		}
		return data;
	}

}
