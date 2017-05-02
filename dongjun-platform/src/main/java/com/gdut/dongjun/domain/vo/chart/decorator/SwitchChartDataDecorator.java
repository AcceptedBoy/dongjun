package com.gdut.dongjun.domain.vo.chart.decorator;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.gdut.dongjun.domain.vo.chart.ChartData;
import com.gdut.dongjun.domain.vo.chart.SwitchChartData;
import com.gdut.dongjun.util.MapUtil;
import com.gdut.dongjun.util.TimeUtil;

/**
 * 这个装饰类其实已经脱离了{@code ChartData}的职责，
 * 可以考虑将{@code ChartData}的{@code getJsonChart(Object obj)}分离出来
 * @author Gordan_Deng
 * @date 2017年1月12日
 */
@Deprecated
public class SwitchChartDataDecorator extends ChartDataDecorator {
	
	private List<SwitchChartData> chartList;

	private List<Date> timeList;

	public SwitchChartDataDecorator(ChartData chart) {
		super(chart);
		this.chartList = new ArrayList<SwitchChartData>();
		this.timeList = new ArrayList<Date>();
	}

	@Override
	public ChartData doGetJsonChart(Object obj) {
		return this.doGetChartData();
	}
	
	public ChartData doGetChartData() {
		ChartData fullChart = this.getChart();
		List<String> legend = new ArrayList<String>();
		for (SwitchChartData chart : this.chartList) {
			
			this.timeList = doSort(this.timeList, chart.getTimeList());
		}
		if (this.timeList.size() == 0) {
			return null;
		}

		//各个设备的图表
		for (SwitchChartData chart : chartList) {
			
			if (chart.getTimeList().isEmpty()) {
				continue;
			}
			
			legend.addAll((List<String>) (chart.getLegend().get("data")));
			//各个设备的A、B、C相
			for (ChaseData chaseData : chart.getSeries()) {
				//初始化ChaseData的同时赋予名字
				ChaseData newData = new ChaseData(chaseData.getName());

				List<Float> chartValue = new ArrayList<Float>();
				List<Float> chaseValue = chaseData.getData();
				List<Date> chartTime = chart.getTimeList();

				Date xTime = null, yTime = null;
				Iterator i = this.timeList.iterator(), j = chartTime.iterator();
				
				if (!i.hasNext()) {
					break;
				}
				int count;
				// 得到带有空值的完整List<Float>
				for (count = 0, yTime = (Date) j.next(); ;) {
					//开始轮询this.timeSet的元素
					if (i.hasNext()) {
						xTime = (Date) i.next();
					} else {
						break; // 如果轮询完毕，即跳出
					}
					if (xTime == yTime) { // xTime是完整TimeSet和yTime是非完整TimeSet，如果时间相等则加数据，不等加null

						chartValue.add(chaseValue.get(count));
						count++;
						if (j.hasNext()) {
							yTime = (Date) j.next();
						} else {
							break;
						}
					} else {
						//如果前端有什么图表的特殊需求可以在这里改。
						chartValue.add(null);
					}
				}
				newData.setData(chartValue);
				//把新构造的ChaseData塞到fullChart中
				fullChart.getSeries().add(newData);
			}
		}
		fullChart.setLegend(MapUtil.warp("data", legend));
		
		List<String> xData = new ArrayList<String>();
		for (Date date : timeList) {
			xData.add(TimeUtil.timeFormat(date));
		}
		fullChart.getxAxis().get(0).setData(xData);
		return fullChart;
	}
	
	public void add(SwitchChartData src) {
		this.chartList.add(src);
	}

	public List<Date> doSort(List<Date> origin, List<Date> src) {
		List<Date> newList = new ArrayList<Date>();
		boolean isSrcEnd = false;
		int i, j;

		for (i = 0, j = 0;;) {

			if (i >= origin.size()) {
				break;
			}
			if (j >= src.size()) {
				isSrcEnd = true;
				break;
			}

			if (origin.get(i).getTime() > src.get(j).getTime()) {
				newList.add(src.get(j));
				j++;
			} else if (origin.get(i).getTime() == src.get(j).getTime()) {
				newList.add(src.get(j));
				i++;
				j++;
			} else {
				newList.add(origin.get(i));
				i++;
			}
		}

		if (isSrcEnd) {
			for (; i < origin.size(); i++) {
				newList.add(origin.get(i));
			}
		} else
			for (; j < src.size(); j++) {
				newList.add(src.get(j));
			}

		return newList;
	}
	
	

}
