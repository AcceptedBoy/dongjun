package com.gdut.dongjun.domain.vo.chart.decorator;

import com.gdut.dongjun.domain.vo.chart.ChartData;

/**
 * 以后报表有什么特殊需求可以继承这个类
 * @author Gordan_Deng
 * @date 2017年1月12日
 */
public abstract class ChartDataDecorator extends ChartData {
	
	protected ChartData chart;
	
	public ChartDataDecorator(ChartData chart) {
		this.chart = chart;
	}

	public void setChart(ChartData chart) {
		this.chart = chart;
	}

	public ChartData getChart() {
		return chart;
	}
	
}
