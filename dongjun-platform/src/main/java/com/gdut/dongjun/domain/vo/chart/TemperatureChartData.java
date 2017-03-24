package com.gdut.dongjun.domain.vo.chart;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;

import com.gdut.dongjun.domain.po.TemperatureMeasure;
import com.gdut.dongjun.util.NumberUtil;
import com.gdut.dongjun.util.TimeUtil;

/**
 * 返回温度报表
 * @author Gordan_Deng
 * @date 2017年2月22日
 */
public class TemperatureChartData extends ChartData {

	public TemperatureChartData() {
		super();
	}

	@Override
	public ChartData doGetJsonChart(Object obj) {
		Map<String, Object> data = (Map<String, Object>) obj;
		List<String> legendData = new ArrayList<String>();
		TemperatureChartData chartData = new TemperatureChartData();
		HashSet<Timestamp> set = new HashSet<Timestamp>();
		List<Timestamp> timeList = new ArrayList<Timestamp>();
		List<String> xData = new ArrayList<String>();
		//时间去重，排序
		for (Entry<String, Object> entry : data.entrySet()) {
			List<TemperatureMeasure> measureList = (List<TemperatureMeasure>) (entry.getValue());
			for (TemperatureMeasure measure : measureList) {
				set.add(measure.getDate());
			}
		}
		
		timeList.addAll(set);
		Collections.sort(timeList);
		
		//没有数据即返回
		if (CollectionUtils.isEmpty(timeList)) {
			return null;
		}
		Iterator i = null;
		Iterator j = null;
		Timestamp i_time = null;
		Timestamp j_time = null;
		
		for (Entry<String, Object> entry : data.entrySet()) {
			int flag = 1;
			ChaseData chaseData = null;
			if (!NumberUtil.isNumeric(entry.getKey())) {
				chaseData = new ChaseData(entry.getKey());
				legendData.add(entry.getKey());
			} else {
				chaseData = new ChaseData(entry.getKey() + "号");
				legendData.add(entry.getKey() + "号");
			}
			List<TemperatureMeasure> measureList = (List<TemperatureMeasure>) (entry.getValue());
			List<Float> chartValue = new ArrayList<Float>();
			i = timeList.iterator();
			j = measureList.iterator();
			
			if (!j.hasNext()) {
				flag = 0;
			}
			j_time = ((TemperatureMeasure)j.next()).getDate();
			int count = 0;
			for (; ;) {
				if (i.hasNext()) {
					i_time = (Timestamp)i.next();
				} else {
					//如果完整的时间集合遍历完毕，则退出
					break;
				}
				if (flag == 1 && i_time.getTime() == j_time.getTime()) {
					chartValue.add(getFloatValue(Integer.valueOf(measureList.get(count).getValue())));
					if (j.hasNext()) {
						j_time = ((TemperatureMeasure)j.next()).getDate();
					} else {
						//时间集合遍历完毕，设置标志位为0
						flag = 0;
					}
					count++;
				} else {
					//没有该时间点的设备，其测量值设为前一个值，如果没有就设为空。如果设备的时间集论讯完，设为空。
					if (chartValue.size() != 0 && flag == 1) {
						chartValue.add(chartValue.get(chartValue.size() - 1));
					} else {
						chartValue.add(null);
					}
				}
			}
			chaseData.setData(chartValue);
			chartData.series.add(chaseData);
		}
		//初始化x轴
		for (Timestamp time : timeList) {
			xData.add(TimeUtil.timeFormat(transformToDate(time)));
		}
		chartData.getxAxis().get(0).setData(xData);
		chartData.legend.put("data", legendData);
		return chartData;
	}

	private float getFloatValue(Integer value) {

		BigDecimal decimal = new BigDecimal(value);
		return decimal.divide(new BigDecimal(100)).floatValue();
	}

	public Date transformToDate(Timestamp time) {
		Date date = new Date(time.getTime());
		return date;
	}
}
