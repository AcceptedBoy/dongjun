package com.gdut.dongjun.domain.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;

import com.gdut.dongjun.domain.po.HighVoltageVoltage;
import com.gdut.dongjun.util.TimeUtil;

public class HighVoltageVoltageChartData extends ChartData {

	@Override
	public ChartData doGetJsonChart(Object obj) {
		Map<String, Object> data = (Map<String, Object>) obj;
		List<String> legendData = new ArrayList<String>();
		HighVoltageVoltageChartData chartData = new HighVoltageVoltageChartData();
		HashSet<Date> set = new HashSet<Date>();
		List<Date> timeList = new ArrayList<Date>();
		List<String> xData = new ArrayList<String>();
		// 时间去重，排序
		for (Entry<String, Object> entry : data.entrySet()) {
			List<HighVoltageVoltage> measureList = null;
			if (entry.getValue() instanceof List) {
				measureList = (List<HighVoltageVoltage>) (entry.getValue());
			}
			for (HighVoltageVoltage measure : measureList) {
				set.add(measure.getTime());
			}
		}

		timeList.addAll(set);
		Collections.sort(timeList);

		// 没有数据即返回
		if (CollectionUtils.isEmpty(timeList)) {
			return null;
		}
		Iterator i = null;
		Iterator j = null;
		Date i_time = null;
		Date j_time = null;

		for (Entry<String, Object> entry : data.entrySet()) {
			int flag = 1;
			ChaseData chaseData = null;
			chaseData = new ChaseData(entry.getKey());
			legendData.add(entry.getKey());

			List<HighVoltageVoltage> measureList = (List<HighVoltageVoltage>) (entry.getValue());
			List<Float> chartValue = new ArrayList<Float>();
			i = timeList.iterator();
			j = measureList.iterator();

			if (!j.hasNext()) {
				flag = 0;
			} else {
				j_time = ((HighVoltageVoltage) j.next()).getTime();
			}
			int count = 0;
			for (;;) {
				if (i.hasNext()) {
					i_time = (Date) i.next();
				} else {
					// 如果完整的时间集合遍历完毕，则退出
					break;
				}
				if (flag == 1 && i_time.getTime() == j_time.getTime()) {
					chartValue.add(measureList.get(count).getValue().floatValue());
					if (j.hasNext()) {
						j_time = ((HighVoltageVoltage) j.next()).getTime();
					} else {
						// 时间集合遍历完毕，设置标志位为0
						flag = 0;
					}
					count++;
				} else {
					// 没有该时间点的设备，其测量值设为前一个值，如果没有就设为空。如果设备的时间集论讯完，设为空。
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
		// 初始化x轴
		for (Date time : timeList) {
			xData.add(TimeUtil.timeFormat(time));
		}
		chartData.getxAxis().get(0).setData(xData);
		chartData.legend.put("data", legendData);
		return chartData;
	}

}
