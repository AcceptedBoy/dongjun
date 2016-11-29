package com.gdut.dongjun.domain.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChaseData {

	private String name;

	private String type = "line";

//	private String stack = "容量";

//	private Map<String, Object> areaStyle = new HashMap<>(1);

	private List<Float> data = new ArrayList<>();

	public ChaseData() {
//		areaStyle.put("normal", new HashMap<>());
	}

	public ChaseData(String name) {
//		areaStyle.put("normal", new HashMap<>());
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

//	public Map<String, Object> getAreaStyle() {
//		return areaStyle;
//	}
//
//	public void setAreaStyle(Map<String, Object> areaStyle) {
//		this.areaStyle = areaStyle;
//	}

	public List<Float> getData() {
		return data;
	}

	public void setData(List<Float> data) {
		this.data = data;
	}
}
