package com.gdut.dongjun.domain.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.formula.functions.T;

import com.gdut.dongjun.domain.po.TemperatureMeasure;
import com.gdut.dongjun.util.GenericUtil;
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
		TemperatureChartData chartData = new TemperatureChartData();
		List<ChaseData> list = chartData.getSeries();
		Object chase;

		List<String> xData = new ArrayList<String>();
		for (Entry<String, Object> entry : data.entrySet()) {
			ChaseData chaseData = new ChaseData(entry.getKey());
			List<Float> floatList = chaseData.getData();
			List<TemperatureMeasure> measureList = (List<TemperatureMeasure>)(entry.getValue());
			for (TemperatureMeasure measure : measureList) {
				xData.add(TimeUtil.timeFormat(measure.getDate()));
				floatList.add(getFloatValue(Integer.valueOf(measure.getValue())));
			}
			this.series.add(chaseData);
		}
		chartData.getxAxis().get(0).setData(xData);
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

		private String stack = "容量";

		private Map<String, Object> areaStyle = new HashMap<>(1);

		private List<Float> data = new ArrayList<>();

		public ChaseData() {
			areaStyle.put("normal", new HashMap<>());
		}

		public ChaseData(String name) {
			areaStyle.put("normal", new HashMap<>());
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

		public String getStack() {
			return stack;
		}

		public void setStack(String stack) {
			this.stack = stack;
		}

		public Map<String, Object> getAreaStyle() {
			return areaStyle;
		}

		public void setAreaStyle(Map<String, Object> areaStyle) {
			this.areaStyle = areaStyle;
		}

		public List<Float> getData() {
			return data;
		}

		public void setData(List<Float> data) {
			this.data = data;
		}
	}
}
