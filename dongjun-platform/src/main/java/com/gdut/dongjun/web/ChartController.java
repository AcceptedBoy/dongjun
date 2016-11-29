package com.gdut.dongjun.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.po.ControlMearsureSwitch;
import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.domain.po.LowVoltageSwitch;
import com.gdut.dongjun.domain.vo.ChartData;
import com.gdut.dongjun.domain.vo.ChartDataAdapter;
import com.gdut.dongjun.domain.vo.SwitchTableData;
import com.gdut.dongjun.domain.vo.TemperatureChartData;
import com.gdut.dongjun.service.ControlMearsureCurrentService;
import com.gdut.dongjun.service.ControlMearsureSwitchService;
import com.gdut.dongjun.service.ControlMearsureVoltageService;
import com.gdut.dongjun.service.HighVoltageCurrentService;
import com.gdut.dongjun.service.HighVoltageSwitchService;
import com.gdut.dongjun.service.HighVoltageVoltageService;
import com.gdut.dongjun.service.LowVoltageCurrentService;
import com.gdut.dongjun.service.LowVoltageSwitchService;
import com.gdut.dongjun.service.LowVoltageVoltageService;
import com.gdut.dongjun.service.TemperatureMeasureHistoryService;
import com.gdut.dongjun.service.TemperatureMeasureService;
import com.gdut.dongjun.util.MapUtil;

@Controller
@RequestMapping("/dongjun")
public class ChartController {

	@Autowired
	private LowVoltageCurrentService currentService;
	@Autowired
	private LowVoltageVoltageService voltageService;
	@Autowired
	private HighVoltageCurrentService currentService2;
	@Autowired
	private HighVoltageVoltageService voltageService2;
	@Autowired
	private ControlMearsureCurrentService currentService3;
	@Autowired
	private ControlMearsureVoltageService voltageService3;
	@Autowired
	private TemperatureMeasureService measureService;
	@Autowired
	private TemperatureMeasureHistoryService measureHistoryService;
	@Autowired
	private LowVoltageSwitchService lowService;
	@Autowired
	private HighVoltageSwitchService highService;
	@Autowired
	private ControlMearsureSwitchService controlService;

	/**
	 * 
	 * @Title: currentChart @Description: TODO @param @param
	 * switchId @param @return @return Object @throws
	 */
	@RequestMapping("/current_chart")
	@ResponseBody
	public Object currentChart(@RequestParam(required = true) String switchId) {

		return currentService.selectBySwitchId(switchId);
	}

	/**
	 * 
	 * @Title: voltageChart @Description: TODO @param @param
	 * switchId @param @return @return Object @throws
	 */
	@RequestMapping("/voltage_chart")
	@ResponseBody
	public Object voltageChart(@RequestParam(required = true) String switchId) {

		return voltageService.selectBySwitchId(switchId);

	}

	/**
	 * 
	 * @Title: selectChartByDate @Description: deporte @param @param
	 * search_date @param @return @return Object @throws
	 */
	@RequestMapping("/select_chart_by_date")
	@ResponseBody
	public Object selectChartByDate(@RequestParam(required = true) String switchId, String beginDate, String endDate) {

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("current", currentService.selectByTime(switchId, beginDate, endDate));
		map.put("voltage", voltageService.selectByTime(switchId, beginDate, endDate));
		return map;
	}

	/**
	 * @param switchId
	 * @param type
	 *            0低压， 1高压， 2管控
	 * @param cov
	 *            0为查询电流， 1为查询电压
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws ParseException 
	 */
	@RequestMapping("/select_chart_by_switch_id")
	@ResponseBody
	public ChartData selectChartBySwitchId(
			@RequestParam(required = true) String switchId,
			@RequestParam(required = true) String type, 
			@RequestParam(required = true) int cov, 
			String beginDate,
			String endDate) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException,
			SecurityException, InstantiationException, ParseException {
		
		if (beginDate == null || beginDate.equals("")) {// 使用当前日期
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
			beginDate = df.format(new Date());
		}
		if (endDate == null || endDate.equals("")) {// 使用当前日期下一天的日期
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
			endDate = df.format(System.currentTimeMillis() + 60*60*24*1000);
		}
		
		ChartDataAdapter adapter = new ChartDataAdapter();
		String[] types = type.trim().split(",=");
		String[] ids = switchId.trim().split(",=");
		for (int i = 0; i < ids.length; i++) {
			String aType = types[i];
			String id = ids[i];
			adapter.add(getChartData(aType, cov, id, beginDate, endDate));
		}
		return adapter.getChartData();
	}

	/**
	 * 得到ChartData表
	 * @param aType
	 * @param cov
	 * @param id
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws ParseException 
	 */
	private ChartData getChartData(String aType, int cov, String id, String beginDate, String endDate) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InstantiationException, ParseException {
		
//		ChartData data = new ChartData();
		switch (aType) {
		case "0":
			LowVoltageSwitch low = lowService.selectByPrimaryKey(id);
			ChartData lowData = new ChartData(low.getName());
			if (cov == 0) {
				/*
				 * map.put("current", );
				 */
				return lowData.getJsonChart(currentService.selectByTime(id, beginDate, endDate));
			} else {
				/*
				 * map.put("voltage", voltageService.selectByTime(switchId,
				 * beginDate, endDate));
				 */
				return lowData.getJsonChart(voltageService.selectByTime(id, beginDate, endDate));
			}
		case "1":
			HighVoltageSwitch high = highService.selectByPrimaryKey(id);
			ChartData highData = new ChartData(high.getName());
			if (cov == 0) {
				/*
				 * map.put("current", currentService2.selectByTime(switchId,
				 * beginDate, endDate));
				 */
				return highData.getJsonChart(currentService2.selectByTime(id, beginDate, endDate));
			} else {
				/*
				 * map.put("voltage",
				 */
				return highData.getJsonChart(voltageService2.selectByTime(id, beginDate, endDate));
			}
		case "2":
			ControlMearsureSwitch control = controlService.selectByPrimaryKey(id);
			ChartData controlData = new ChartData(control.getName());
			if (cov == 0) {
				/*
				 * map.put("current", currentService3.selectByTime(switchId,
				 * beginDate, endDate));
				 */
				return controlData.getJsonChart(currentService3.selectByTime(id, beginDate, endDate));
			} else {
				/*
				 * map.put("voltage", voltageService3.selectByTime(switchId,
				 * beginDate, endDate));
				 */
				return controlData.getJsonChart(voltageService3.selectByTime(id, beginDate, endDate));
			}
		default:
			return null;
		}
	}

	@RequestMapping("/select_table_by_id")
	@ResponseBody
	public Object selectTableById(@RequestParam(required = true) String switchId,
			@RequestParam(required = true) int type, @RequestParam(required = true) int cov, String beginDate,
			String endDate) {

		if (beginDate == null || beginDate == "") {// 使用当前日期
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			beginDate = df.format(new Date());
		}
		if (endDate == null || endDate == "") {// 使用当前日期下一天的日期
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式

			Date date = new Date();
			long time = (date.getTime() / 1000) + 60 * 60 * 24;// 秒
			date.setTime(time * 1000);// 毫秒
			endDate = df.format(date);
		}

		SwitchTableData data = new SwitchTableData();
		switch (type) {
		case 0:
			if (cov == 0) {
				return MapUtil.warp("data",
						data.getJsonTable(currentService.selectByTime(switchId, beginDate, endDate)));
			} else {
				return MapUtil.warp("data",
						data.getJsonTable(voltageService.selectByTime(switchId, beginDate, endDate)));
			}
		case 1:
			if (cov == 0) {
				return MapUtil.warp("data",
						data.getJsonTable(currentService2.selectByTime(switchId, beginDate, endDate)));
			} else {
				return MapUtil.warp("data",
						data.getJsonTable(voltageService2.selectByTime(switchId, beginDate, endDate)));
			}
		case 2:
			if (cov == 0) {
				return MapUtil.warp("data",
						data.getJsonTable(currentService3.selectByTime(switchId, beginDate, endDate)));
			} else {
				return MapUtil.warp("data",
						data.getJsonTable(voltageService3.selectByTime(switchId, beginDate, endDate)));
			}
		default:
			return "";
		}
	}
	
	/**
	 * 返回温度报表
	 * @param id
	 * @param beginDate
	 * @param endDate
	 * @param sensorAddress
	 * @return
	 */
	@RequestMapping("/select_chart_by_device_id")
	@ResponseBody
	public TemperatureChartData getJsonChart(
			String id, 
			String beginDate, 
			String endDate, 
			String tagList) {
		TemperatureChartData chartData = new TemperatureChartData();
		Map<String, Object> measureMap = new HashMap<String, Object>();
		String[] tags = tagList.split(",=");
		for (String tag : tags) {
			measureMap.put(tag, measureHistoryService.selectByTime(id, Integer.parseInt(tag), beginDate, endDate));
		}
		return chartData.getJsonChart(measureMap);
	}

}
