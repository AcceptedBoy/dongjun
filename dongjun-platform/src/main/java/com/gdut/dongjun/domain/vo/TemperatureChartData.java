package com.gdut.dongjun.domain.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.gdut.dongjun.domain.po.TemperatureMeasure;
import com.gdut.dongjun.domain.po.TemperatureMeasureHistory;
import com.gdut.dongjun.util.TimeUtil;

public class TemperatureChartData {

	public TemperatureChartData() {
		title.put("text", "");
		tooltip.put("trigger", "axis");
		legend.put("data", "温度");
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

	private Map<String, Object> title = new HashMap<>(1);

	private Map<String, Object> tooltip = new HashMap<>(1);

	private Map<String, Object> legend = new HashMap<>();

	private Map<String, Object> toolbox = new HashMap<>(1);

	private Map<String, Object> grid = new HashMap<>();

	private List<XAxis> xAxis = new ArrayList<>();

	private List<YAxis> yAxis = new ArrayList<>();

	private List<ChaseData> series = new ArrayList<>();

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

	public TemperatureChartData getJsonChart(Map<String, Object> data) {
		List<String> legendData = new ArrayList<String>();
		TemperatureChartData chartData = new TemperatureChartData();
		HashSet<Timestamp> set = new HashSet<Timestamp>();
		List<String> xData = new ArrayList<String>();
		Map<String, Map<String, Float>> valueMap = new HashMap<String, Map<String, Float>>();
		//初始化x轴
		for (Entry<String, Object> entry : data.entrySet()) {

			List<TemperatureMeasureHistory> measureList = (List<TemperatureMeasureHistory>) (entry.getValue());

			for (TemperatureMeasureHistory measure : measureList) {
				set.add(measure.getDate());
			}
			Map<String, Float> map = new HashMap<String, Float>();
			valueMap.put(entry.getKey(), map);
		}
		
		for (Entry<String, Object> entry : data.entrySet()) {
			Map<String, Float> map = valueMap.get(entry.getKey());
			//新建空置map
			for (Timestamp time : set) {
				map.put(TimeUtil.timeFormat(transformToDate(time)), null);
			}
			//赋值
			List<TemperatureMeasureHistory> measureList = (List<TemperatureMeasureHistory>) (entry.getValue());
			for (TemperatureMeasureHistory measure : measureList) {
				map.put(TimeUtil.timeFormat(transformToDate(measure.getDate())), 
						getFloatValue(Integer.valueOf(measure.getValue())));
			}
			//转换为List
			ChaseData chaseData = new ChaseData(entry.getKey() + "号");
			legendData.add(entry.getKey() + "号");
			List<Float> floatList = new ArrayList<Float>();
			for (Timestamp time : set) {
				floatList.add(map.get(TimeUtil.timeFormat(transformToDate(time))));
			}
			chaseData.setData(floatList);
			chartData.series.add(chaseData);
		}
		
		//构建xData
		for (Timestamp time : set) {
			xData.add(TimeUtil.timeFormat(transformToDate(time)));
		}
		chartData.getxAxis().get(0).setData(xData);
		chartData.legend.put("data", legendData);
		return chartData;
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

	public class ChaseData {

		private String name;

		private String type = "line";

//		private String stack = "";

//		private Map<String, Object> areaStyle = new HashMap<>(1);

		private List<Float> data = new ArrayList<>();

		public ChaseData() {
//			areaStyle.put("normal", new HashMap<>());
		}

		public ChaseData(String name) {
//			areaStyle.put("normal", new HashMap<>());
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

//		public Map<String, Object> getAreaStyle() {
//			return areaStyle;
//		}
//
//		public void setAreaStyle(Map<String, Object> areaStyle) {
//			this.areaStyle = areaStyle;
//		}

		public List<Float> getData() {
			return data;
		}

		public void setData(List<Float> data) {
			this.data = data;
		}
	}
	
	public Date transformToDate(Timestamp time) {
		Date date = new Date(time.getTime());
		return date;
	}
}
