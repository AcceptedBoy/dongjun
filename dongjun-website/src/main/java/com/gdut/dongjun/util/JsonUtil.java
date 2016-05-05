package com.gdut.dongjun.util;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *	TODO 测试中，暂时不可用
 * 
 *
 */
public class JsonUtil {

	public static String toJsonString(@SuppressWarnings("rawtypes") List list) {
		JSONArray jsonObject = JSONArray.fromObject(list);
		return jsonObject.toString();
	}
	
	public static JSONObject JsonStringToJsonObject(String str) {
		
		return JSONObject.fromObject(str);
	}
	
	public static JSONArray JsonStringToJsonArray(String str) {
		
		return JSONArray.fromObject(str);
	}
	
	public static Object jsonStringToBean(String str, Class<?> clazz) {
		
		return JSONObject.toBean(JSONObject.fromObject(str), clazz);
	}
	
	public static Object jsonStringToArray(String str, Class<?> clazz) {
		
		return JSONArray.toArray(JsonStringToJsonArray(str), clazz);
	}

}
