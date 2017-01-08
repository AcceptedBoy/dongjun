package com.gdut.dongjun.util;

import java.util.Arrays;

/**
 * TODO
 */
public class CharUtils {

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
        return equals(src, src.length - end.length, end.length, end);
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
}
