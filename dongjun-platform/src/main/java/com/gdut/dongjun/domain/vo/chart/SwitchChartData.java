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
		List<Object> data1 = (List<Object>) data;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SwitchChartData chartData = new SwitchChartData(this.switchName);
		List<ChaseData> list = chartData.getSeries();
		List<Float> chaseA = list.get(0).getData();
		List<Float> chaseB = list.get(1).getData();
		List<Float> chaseC = list.get(2).getData();
		List<String> xData = new ArrayList<>();
		Object chase = null;
		Date preDate = null;
		Date postDate = null;
		Float _chaseA = null;
		Float _chaseB = null;
		Float _chaseC = null;

		// 处理第一个节点
		Object firstObj = data1.get(0);
		if (0 != data1.size()) {
			preDate = (Date) GenericUtil.getPrivateObjectValue(firstObj, "time");
			chase = GenericUtil.getPrivateObjectValue(firstObj, "phase");
		}
		switch (chase.toString().charAt(0)) {
		case 'A':
			_chaseA = getFloatValue(GenericUtil.getPrivatyIntegerValue(firstObj, "value"));
			break;
		case 'B':
			_chaseB = getFloatValue(GenericUtil.getPrivatyIntegerValue(firstObj, "value"));
			break;
		case 'C':
			_chaseC = getFloatValue(GenericUtil.getPrivatyIntegerValue(firstObj, "value"));
			break;
		default:
			break;
		}

		// 从第二个结点开始处理
		for (int i = 1; i < data1.size(); i++) {
			Object o = data1.get(i);
			chase = GenericUtil.getPrivateObjectValue(o, "phase");

			if (chase == null) {
				return null;
			}
			postDate = (Date) GenericUtil.getPrivateObjectValue(o, "time");
			if (preDate.getTime() == postDate.getTime()) {
				// do nothing
			} else {
				xData.add(TimeUtil.timeFormat(preDate));
				chaseA.add(_chaseA);
				chaseB.add(_chaseB);
				chaseC.add(_chaseC);
				_chaseA = null;
				_chaseB = null;
				_chaseC = null;

				try {
					chartData.getTimeList().add(format.parse(TimeUtil.timeFormat(preDate)));
				} catch (ParseException e) {
					logger.warn("Exception : SwitchChartData转换TimeList出错");
				}
				preDate = (Date) GenericUtil.getPrivateObjectValue(o, "time");
				postDate = null;
			}
			switch (chase.toString().charAt(0)) {
			case 'A':
				_chaseA = getFloatValue(GenericUtil.getPrivatyIntegerValue(o, "value"));
				break;
			case 'B':
				_chaseB = getFloatValue(GenericUtil.getPrivatyIntegerValue(o, "value"));
				break;
			case 'C':
				_chaseC = getFloatValue(GenericUtil.getPrivatyIntegerValue(o, "value"));
				break;
			default:
				break;
			}
		}
		// 处理最后的节点
		xData.add(TimeUtil.timeFormat(preDate));
		chaseA.add(_chaseA);
		chaseB.add(_chaseB);
		chaseC.add(_chaseC);
		try {
			chartData.getTimeList().add(format.parse(TimeUtil.timeFormat(preDate)));
		} catch (ParseException e) {
			logger.warn("Exception : SwitchChartData转换TimeList出错");
		}

		chartData.getxAxis().get(0).setData(xData);
		return chartData;
	}

	private float getFloatValue(Integer value) {

		BigDecimal decimal = new BigDecimal(value);
		return decimal.divide(new BigDecimal(100)).floatValue();
	}

}
