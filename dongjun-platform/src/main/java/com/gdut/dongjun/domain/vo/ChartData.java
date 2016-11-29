package com.gdut.dongjun.domain.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.gdut.dongjun.util.GenericUtil;
import com.gdut.dongjun.util.TimeUtil;

/**
 *@Author link xiaoMian <972192420@qq.com>
 *@ClassName ChartDataFormat.java
 *@Time 2016年3月2日下午3:34:56
 *@Description 将前端所需要的json返回
 *@Version 1.0 Topview
 */
public class ChartData {

	private Map<String, Object> title = new HashMap<>(1);
	
	private Map<String, Object> tooltip = new HashMap<>(1);
	
	private Map<String, Object> legend = new HashMap<>();
	 
	private Map<String, Object> toolbox = new HashMap<>(1);
	
	private Map<String, Object> grid = new HashMap<>();
	
	private List<XAxis> xAxis = new ArrayList<>();
	
	private List<YAxis> yAxis = new ArrayList<>();
	
	private List<ChaseData> series = new ArrayList<>();
	
	private List<Date> timeList = new ArrayList<Date>();
	
	private String switchName;
	
	public ChartData() {
		
		title.put("text", "");
		tooltip.put("trigger", "axis");
		List<String> data = new ArrayList<>();
		data.add("A相"); 
		data.add("B相");
		data.add("C相");
		legend.put("data", data);
		Map<String, Object> feature = new HashMap<>(1);
		feature.put("saveAsImage", new HashMap<>());
		toolbox.put("feature", feature);
		grid.put("left", "");
		grid.put("right", "");
		grid.put("buttom", "");
		grid.put("containLabel", true);
		xAxis.add(new XAxis());
		yAxis.add(new YAxis());
	}
	
	public ChartData(String switchName) {
		
		title.put("text", "");
		tooltip.put("trigger", "axis");
		List<String> data = new ArrayList<>();
		data.add(switchName + "A相"); 
		data.add(switchName + "B相");
		data.add(switchName + "C相");
		legend.put("data", data);
		Map<String, Object> feature = new HashMap<>(1);
		feature.put("saveAsImage", new HashMap<>());
		toolbox.put("feature", feature);
		grid.put("left", "");
		grid.put("right", "");
		grid.put("buttom", "");
		grid.put("containLabel", true);
		xAxis.add(new XAxis());
		yAxis.add(new YAxis());
		this.switchName = switchName;
		series.add(new ChaseData(switchName + "A相"));
		series.add(new ChaseData(switchName + "B相"));
		series.add(new ChaseData(switchName + "C相"));
	}
	
	public <T> ChartData getJsonChart(List<T> data) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InstantiationException, ParseException {
		SimpleDateFormat  format = new SimpleDateFormat ("yyyy-MM-dd HH:mm");
		ChartData chartData = new ChartData(this.switchName);
		List<ChaseData> list = chartData.getSeries();
		List<Float> chaseA = list.get(0).getData();
		List<Float> chaseB = list.get(1).getData();
		List<Float> chaseC = list.get(2).getData(); 
		List<String> xData = new ArrayList<>();
		Object chase;
		
		for(T o : data) {
			
			chase = GenericUtil.getPrivateObjectValue(o, "phase");
			
			if(chase == null) {
				return null;
			}
			switch(chase.toString().charAt(0)) {
			case 'A' : 
				chaseA.add(getFloatValue(
					GenericUtil.getPrivatyIntegerValue(o, "value")));
				chaseB.add(null);
				chaseC.add(null);
				break;
			case 'B' : 
				chaseB.add(getFloatValue(
					GenericUtil.getPrivatyIntegerValue(o, "value")));
				chaseA.add(null);
				chaseC.add(null);
				break;
			case 'C' : 
				chaseC.add(getFloatValue(
					GenericUtil.getPrivatyIntegerValue(o, "value")));
				chaseA.add(null);
				chaseB.add(null);
				break;
			}
			chartData.getTimeList().add(format.parse(TimeUtil.timeFormat((Date)GenericUtil.getPrivateObjectValue(o, "time"))));
//			timeList.add((Date)GenericUtil.getPrivateObjectValue(o, "time"));
			xData.add(TimeUtil.timeFormat((Date)GenericUtil.getPrivateObjectValue(o, "time")));
		}
		chartData.getxAxis().get(0).setData(xData);
		return chartData;
	}
	
	private float getFloatValue(Integer value) {
		
		BigDecimal decimal = new BigDecimal(value);
		return decimal.divide(new BigDecimal(100)).floatValue();
	}

	public List<XAxis> getxAxis() {
		return xAxis;
	}

	public void setxAxis(List<XAxis> xAxis) {
		this.xAxis = xAxis;
	}

	public List<YAxis> getyAxis() {
		return yAxis;
	}

	public void setyAxis(List<YAxis> yAxis) {
		this.yAxis = yAxis;
	}

	public List<ChaseData> getSeries() {
		return series;
	}

	public void setSeries(List<ChaseData> series) {
		this.series = series;
	}

	public Map<String, Object> getToolbox() {
		return toolbox;
	}

	public void setToolbox(Map<String, Object> toolbox) {
		this.toolbox = toolbox;
	}

	public Map<String, Object> getGrid() {
		return grid;
	}

	public void setGrid(Map<String, Object> grid) {
		this.grid = grid;
	}

	public Map<String, Object> getTitle() {
		return title;
	}

	public void setTitle(Map<String, Object> title) {
		this.title = title;
	}

	public Map<String, Object> getTooltip() {
		return tooltip;
	}

	public void setTooltip(Map<String, Object> tooltip) {
		this.tooltip = tooltip;
	}

	public Map<String, Object> getLegend() {
		return legend;
	}

	public void setLegend(Map<String, Object> legend) {
		this.legend = legend;
	}
	
	public List<Date> getTimeList() {
		return timeList;
	}

	public void setTimeList(List<Date> timeList) {
		this.timeList = timeList;
	}

	class XAxis {
		
		private String type = "category";
		
		private boolean boundaryGap = false;
		
		private List<String> data = new ArrayList<>();

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public boolean getBoundaryGap() {
			return boundaryGap;
		}

		public void setBoundaryGap(boolean boundaryGap) {
			this.boundaryGap = boundaryGap;
		}

		public List<String> getData() {
			return data;
		}

		public void setData(List<String> data) {
			this.data = data;
		}
	}
	
	class YAxis {
		
		private String type = "value";
		
		public void setType(String type) {
			this.type = type;
		}
		
		public String getType() {
			return type;
		}
	}
	
}
