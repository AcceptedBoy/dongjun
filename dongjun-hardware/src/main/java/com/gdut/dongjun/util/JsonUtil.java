package com.gdut.dongjun.util;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil {

	public static <T> JSONArray listToJSON(List<T> t) {
		
		return JSONArray.fromObject(t);
	}
	
	public static <T> JSONArray listToJSON(T t) {
		
		return JSONArray.fromObject(t);
	}
	
	public static <T> JSONObject objectToJSON(T t) {
		
		return JSONObject.fromObject(t);
	}
}
