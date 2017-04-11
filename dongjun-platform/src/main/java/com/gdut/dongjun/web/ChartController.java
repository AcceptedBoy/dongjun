package com.gdut.dongjun.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.po.DataMonitorSubmodule;
import com.gdut.dongjun.domain.po.TemperatureMeasure;
import com.gdut.dongjun.domain.po.TemperatureSensor;
import com.gdut.dongjun.domain.vo.chart.ChartData;
import com.gdut.dongjun.domain.vo.chart.TemperatureChartData;
import com.gdut.dongjun.service.device.DataMonitorService;
import com.gdut.dongjun.service.device.DataMonitorSubmoduleService;
import com.gdut.dongjun.service.device.TemperatureModuleService;
import com.gdut.dongjun.service.device.TemperatureSensorService;
import com.gdut.dongjun.service.device.temperature.TemperatureMeasureService;
import com.gdut.dongjun.util.MyBatisMapUtil;

@Controller
@RequestMapping("/dongjun")
public class ChartController {

	@Autowired
	private TemperatureMeasureService measureService;
	@Autowired
	private TemperatureSensorService sensorService;
	@Autowired
	private TemperatureModuleService temModuleService;
	@Autowired
	private DataMonitorService monitorService;
	@Autowired
	private DataMonitorSubmoduleService submoduleService;

	/**
	 * 
	 * @Title: currentChart @Description: TODO @param @param
	 * switchId @param @return @return Object @throws
	 */
//	@RequiresAuthentication
//	@RequestMapping("/current_chart")
//	@ResponseBody
//	public Object currentChart(@RequestParam(required = true) String switchId) {
//
//		return currentService.selectBySwitchId(switchId);
//	}

	/**
	 * 
	 * @Title: voltageChart @Description: TODO @param @param
	 * switchId @param @return @return Object @throws
	 */
//	@RequiresAuthentication
//	@RequestMapping("/voltage_chart")
//	@ResponseBody
//	public Object voltageChart(@RequestParam(required = true) String switchId) {
//
//		return voltageService.selectBySwitchId(switchId);
//
//	}

	/**
	 * 
	 * @Title: selectChartByDate @Description: deporte @param @param
	 * search_date @param @return @return Object @throws
	 */
//	@RequiresAuthentication
//	@RequestMapping("/select_chart_by_date")
//	@ResponseBody
//	public Object selectChartByDate(@RequestParam(required = true) String switchId, String beginDate, String endDate) {
//
//		Map<String, Object> map = new HashMap<String, Object>();
//
//		map.put("current", currentService.selectByTime(switchId, beginDate, endDate));
//		map.put("voltage", voltageService.selectByTime(switchId, beginDate, endDate));
//		return map;
//	}

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
//	@RequiresAuthentication
//	@RequestMapping("/select_chart_by_switch_id")
//	@ResponseBody
//	public ChartData selectChartBySwitchId(
//			@RequestParam(required = true) String switchId,
//			@RequestParam(required = true) String type, 
//			@RequestParam(required = true) int cov, 
//			String beginDate,
//			String endDate) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException,
//			SecurityException, InstantiationException, ParseException {
//		
//		if (beginDate == null || beginDate.equals("")) {// 使用当前日期
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
//			beginDate = df.format(new Date());
//		}
//		if (endDate == null || endDate.equals("")) {// 使用当前日期下一天的日期
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
//			endDate = df.format(System.currentTimeMillis() + 60*60*24*1000);
//		}
//		
//		ChartData fullChart = new SwitchChartData();
//		SwitchChartDataDecorator fullChart0 = new SwitchChartDataDecorator(fullChart);
//		String[] types = type.trim().split(",=");
//		String[] ids = switchId.trim().split(",=");
//		for (int i = 0; i < ids.length; i++) {
//			String aType = types[i];
//			String id = ids[i];
//			fullChart0.add((SwitchChartData)getChartData(aType, cov, id, beginDate, endDate));
//		}
//		return fullChart0.getJsonChart(null);
//	}

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
//	private ChartData getChartData(String aType, int cov, String id, String beginDate, String endDate) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InstantiationException, ParseException {
//		
////		ChartData data = new ChartData();
//		switch (aType) {
//		case "0":
//			LowVoltageSwitch low = lowService.selectByPrimaryKey(id);
//			SwitchChartData lowData = new SwitchChartData(low.getName());
//			if (cov == 0) {
//				/*
//				 * map.put("current", );
//				 */
//				return lowData.getJsonChart(currentService.selectByTime(id, beginDate, endDate));
//			} else {
//				/*
//				 * map.put("voltage", voltageService.selectByTime(switchId,
//				 * beginDate, endDate));
//				 */
//				return lowData.getJsonChart(voltageService.selectByTime(id, beginDate, endDate));
//			}
//		case "1":
//			HighVoltageSwitch high = highService.selectByPrimaryKey(id);
//			SwitchChartData highData = new SwitchChartData(high.getName());
//			if (cov == 0) {
//				/*
//				 * map.put("current", currentService2.selectByTime(switchId,
//				 * beginDate, endDate));
//				 */
//				return highData.getJsonChart(currentService2.selectByTime(id, beginDate, endDate));
//			} else {
//				/*
//				 * map.put("voltage",
//				 */
//				return highData.getJsonChart(voltageService2.selectByTime(id, beginDate, endDate));
//			}
//		case "2":
//			ControlMearsureSwitch control = controlService.selectByPrimaryKey(id);
//			SwitchChartData controlData = new SwitchChartData(control.getName());
//			if (cov == 0) {
//				/*
//				 * map.put("current", currentService3.selectByTime(switchId,
//				 * beginDate, endDate));
//				 */
//				return controlData.getJsonChart(currentService3.selectByTime(id, beginDate, endDate));
//			} else {
//				/*
//				 * map.put("voltage", voltageService3.selectByTime(switchId,
//				 * beginDate, endDate));
//				 */
//				return controlData.getJsonChart(voltageService3.selectByTime(id, beginDate, endDate));
//			}
//		default:
//			return null;
//		}
//	}

//	@RequiresAuthentication
//	@RequestMapping("/select_table_by_id")
//	@ResponseBody
//	public Object selectTableById(@RequestParam(required = true) String switchId,
//			@RequestParam(required = true) int type, @RequestParam(required = true) int cov, String beginDate,
//			String endDate) {
//
//		if (beginDate == null || beginDate == "") {// 使用当前日期
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
//			beginDate = df.format(new Date());
//		}
//		if (endDate == null || endDate == "") {// 使用当前日期下一天的日期
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
//
//			Date date = new Date();
//			long time = (date.getTime() / 1000) + 60 * 60 * 24;// 秒
//			date.setTime(time * 1000);// 毫秒
//			endDate = df.format(date);
//		}
//
//		SwitchTableData data = new SwitchTableData();
//		switch (type) {
//		case 0:
//			if (cov == 0) {
//				return MapUtil.warp("data",
//						data.getJsonTable(currentService.selectByTime(switchId, beginDate, endDate)));
//			} else {
//				return MapUtil.warp("data",
//						data.getJsonTable(voltageService.selectByTime(switchId, beginDate, endDate)));
//			}
//		case 1:
//			if (cov == 0) {
//				return MapUtil.warp("data",
//						data.getJsonTable(currentService2.selectByTime(switchId, beginDate, endDate)));
//			} else {
//				return MapUtil.warp("data",
//						data.getJsonTable(voltageService2.selectByTime(switchId, beginDate, endDate)));
//			}
//		case 2:
//			if (cov == 0) {
//				return MapUtil.warp("data",
//						data.getJsonTable(currentService3.selectByTime(switchId, beginDate, endDate)));
//			} else {
//				return MapUtil.warp("data",
//						data.getJsonTable(voltageService3.selectByTime(switchId, beginDate, endDate)));
//			}
//		default:
//			return "";
//		}
//	}
	
	/**
	 * 返回温度报表
	 * @param id
	 * @param beginDate
	 * @param endDate
	 * @param sensorAddress
	 * @return
	 */
	@RequiresAuthentication
	@RequestMapping("/select_chart_by_device_id")
	@ResponseBody
	public ChartData getJsonChart(
			@RequestParam(required = true) String monitorId, 
			@RequestParam(required = true) String beginDate, 
			@RequestParam(required = true) String endDate) {
		List<DataMonitorSubmodule> mappings = submoduleService.selectByParameters(MyBatisMapUtil.warp("dataMonitorId", monitorId));
		String deviceId = null;
		for (DataMonitorSubmodule mapping : mappings) {
			if (3 == mapping.getModuleType()) {
				deviceId = mapping.getModuleId();
				break;
			}
		}
		TemperatureChartData chartData = new TemperatureChartData();
		Map<String, Object> measureMap = new HashMap<String, Object>();
		List<TemperatureSensor> sensors = sensorService.selectByParameters(MyBatisMapUtil.warp("deviceId", deviceId));
		for (TemperatureSensor sensor : sensors) {
			List<TemperatureMeasure> measures = measureService.selectByTime(deviceId, sensor.getTag(), beginDate, endDate);
			measureMap.put(sensor.getTag() + "", measures);
		}
		return chartData.getJsonChart(measureMap);
	}
	
}
