package com.gdut.dongjun.domain.vo.chart;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.gdut.dongjun.util.GenericUtil;
import com.gdut.dongjun.util.TimeUtil;

/**
 * @Author link xiaoMian <972192420@qq.com>
 * @ClassName ChartDataFormat.java
 * @Time 2016年3月2日下午3:34:56
 * @Description 将前端所需要的json返回
 * @Version 1.0 Topview
 */
public class SwitchChartData extends ChartData {

	private String switchName;
	
	private final Logger logger = Logger.getLogger(SwitchChartData.class);

	public SwitchChartData() {
		super();
	}

	public SwitchChartData(String switchName) {
		super();
		this.switchName = switchName;
		List<String> data = (List<String>) this.legend.get("data");
		data.add(switchName + "A相");
		data.add(switchName + "B相");
		data.add(switchName + "C相");
		series.add(new ChaseData(switchName + "A相"));
		series.add(new ChaseData(switchName + "B相"));
		series.add(new ChaseData(switchName + "C相"));
	}

	@Override
	public SwitchChartData doGetJsonChart(Object data) {
		List<Object> data1  = (List<Object>)data;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SwitchChartData chartData = new SwitchChartData(this.switchName);
		List<ChaseData> list = chartData.getSeries();
		List<Float> chaseA = list.get(0).getData();
		List<Float> chaseB = list.get(1).getData();
		List<Float> chaseC = list.get(2).getData();
		List<String> xData = new ArrayList<>();
		Object chase;

		for (Object o : data1) {

			chase = GenericUtil.getPrivateObjectValue(o, "phase");

			if (chase == null) {
				return null;
			}
			switch (chase.toString().charAt(0)) {
			case 'A':
				chaseA.add(getFloatValue(GenericUtil.getPrivatyIntegerValue(o, "value")));
				chaseB.add(null);
				chaseC.add(null);
				break;
			case 'B':
				chaseB.add(getFloatValue(GenericUtil.getPrivatyIntegerValue(o, "value")));
				chaseA.add(null);
				chaseC.add(null);
				break;
			case 'C':
				chaseC.add(getFloatValue(GenericUtil.getPrivatyIntegerValue(o, "value")));
				chaseA.add(null);
				chaseB.add(null);
				break;
			}
			try {
				chartData.getTimeList()
						.add(format.parse(TimeUtil.timeFormat((Date) GenericUtil.getPrivateObjectValue(o, "time"))));
			} catch (ParseException e) {
				logger.warn("出现异常：" + e.getMessage());
				return null;
			}
			// timeList.add((Date)GenericUtil.getPrivateObjectValue(o, "time"));
			xData.add(TimeUtil.timeFormat((Date) GenericUtil.getPrivateObjectValue(o, "time")));
		}
		chartData.getxAxis().get(0).setData(xData);
		return chartData;
	}

	private float getFloatValue(Integer value) {

		BigDecimal decimal = new BigDecimal(value);
		return decimal.divide(new BigDecimal(100)).floatValue();
	}
	
}
