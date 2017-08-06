package com.gdut.dongjun.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.po.HighVoltageCurrent;
import com.gdut.dongjun.domain.vo.ChartData;
import com.gdut.dongjun.domain.vo.HighVoltageCurrentChartData;
import com.gdut.dongjun.domain.vo.HighVoltageVoltageChartData;
import com.gdut.dongjun.service.device.current.ControlMearsureCurrentService;
import com.gdut.dongjun.service.device.current.HighVoltageCurrentService;
import com.gdut.dongjun.service.device.current.LowVoltageCurrentService;
import com.gdut.dongjun.service.device.voltage.ControlMearsureVoltageService;
import com.gdut.dongjun.service.device.voltage.HighVoltageVoltageService;
import com.gdut.dongjun.service.device.voltage.LowVoltageVoltageService;

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

	/**
	 * 
	 * @Title: currentChart
	 * @Description: TODO
	 * @param @param switchId
	 * @param @return
	 * @return Object
	 * @throws
	 */
	@RequestMapping("/current_chart")
	@ResponseBody
	public Object currentChart(@RequestParam(required = true) String switchId) {

		return currentService.selectBySwitchId(switchId);
	}

	/**
	 * 
	 * @Title: voltageChart
	 * @Description: TODO
	 * @param @param switchId
	 * @param @return
	 * @return Object
	 * @throws
	 */
	@RequestMapping("/voltage_chart")
	@ResponseBody
	public Object voltageChart(@RequestParam(required = true) String switchId) {

		return voltageService.selectBySwitchId(switchId);

	}

	/**
	 * 
	 * @Title: selectChartByDate
	 * @Description: deporte
	 * @param @param search_date
	 * @param @return
	 * @return Object
	 * @throws
	 */
	@RequestMapping("/select_chart_by_date")
	@ResponseBody
	public Object selectChartByDate(
			@RequestParam(required = true) String switchId, String beginDate,
			String endDate) {

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("current",
				currentService.selectByTime(switchId, beginDate, endDate));
		map.put("voltage",
				voltageService.selectByTime(switchId, beginDate, endDate));
		return map;
	}

	/**
	 * @param switchId
	 * @param type	开关的类型
	 * @param cov	0为查询电流， 1为查询电压
	 * @return
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws InstantiationException 
	 */
//	@RequestMapping("/select_chart_by_switch_id")
//	@ResponseBody
//	public Object selectChartBySwitchId(
//			@RequestParam(required = true) String switchId,
//			@RequestParam(required = true) int type,
//			@RequestParam(required = true) int cov, 
//			String beginDate,
//			String endDate) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InstantiationException {
//
//		if (beginDate == null || beginDate.equals("")) {// 使用当前日期
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
//			beginDate = df.format(new Date());
//		}
//		if (endDate == null || endDate.equals("")) {// 使用当前日期下一天的日期
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
//			
//			Date date = new Date();
//			long time = (date.getTime() / 1000) + 60 * 60 * 24;//秒  
//            date.setTime(time * 1000);//毫秒 
//			endDate = df.format(date);
//		}
//
//		/*if (type == null) {
//			type = "0";
//		}*/
//		ChartData data = new ChartData();
//		switch (type) {
//		case 0:
//			if(cov == 0) {
//				/*map.put("current",
//						);*/
//				return data.getJsonChart(currentService.selectByTime(switchId, beginDate, endDate));
//			} else {
//				/*map.put("voltage",
//						voltageService.selectByTime(switchId, beginDate, endDate));*/
//				return data.getJsonChart(voltageService.selectByTime(switchId, beginDate, endDate));
//			}
//		case 1:
//			if(cov == 0) {
//				/*map.put("current",
//						currentService2.selectByTime(switchId, beginDate, endDate));*/
//				return data.getJsonChart(currentService2.selectByTime(switchId, beginDate, endDate));
//			} else {
//				/*map.put("voltage",
//						*/
//				return data.getJsonChart(voltageService2.selectByTime(switchId, beginDate, endDate));
//			}
//		case 2:
//			if(cov == 0) {
//				/*map.put("current",
//						currentService3.selectByTime(switchId, beginDate, endDate));*/
//				return data.getJsonChart(currentService3.selectByTime(switchId, beginDate, endDate));
//			} else {
//				/*map.put("voltage",
//						voltageService3.selectByTime(switchId, beginDate, endDate));*/
//				return data.getJsonChart(voltageService3.selectByTime(switchId, beginDate, endDate));
//			}
//		default:
//			return "";
//		}
//	}
	
//	@RequestMapping("/select_table_by_id")
//	@ResponseBody
//	public Object selectTableById(@RequestParam(required = true) String switchId,
//			@RequestParam(required = true) int type,
//			@RequestParam(required = true) int cov, String beginDate,
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
//			long time = (date.getTime() / 1000) + 60 * 60 * 24;//秒  
//            date.setTime(time * 1000);//毫秒 
//			endDate = df.format(date);
//		}
//		
//		SwitchTableData data = new SwitchTableData();
//		switch (type) {
//		case 0:
//			if(cov == 0) {
//				return MapUtil.warp("data", data.getJsonTable(currentService.selectByTime(switchId, beginDate, endDate)));
//			} else {
//				return MapUtil.warp("data", data.getJsonTable(voltageService.selectByTime(switchId, beginDate, endDate)));
//			}
//		case 1:
//			if(cov == 0) {
//				return MapUtil.warp("data", data.getJsonTable(currentService2.selectByTime(switchId, beginDate, endDate)));
//			} else {
//				return MapUtil.warp("data", data.getJsonTable(voltageService2.selectByTime(switchId, beginDate, endDate)));
//			}
//		case 2:
//			if(cov == 0) {
//				return MapUtil.warp("data", data.getJsonTable(currentService3.selectByTime(switchId, beginDate, endDate)));
//			} else {
//				return MapUtil.warp("data", data.getJsonTable(voltageService3.selectByTime(switchId, beginDate, endDate)));
//			}
//		default:
//			return "";
//		}
//	}
	
	/**
	 * 返回功率
	 * @param id
	 * @param beginDate
	 * @param endDate
	 * @param sensorAddress
	 * @return
	 */
	@RequiresAuthentication
	@RequestMapping("/chart/high_voltage_voltage")
	@ResponseBody
	public ChartData getElectronicPowerJsonChart(
			@RequestParam(required = true) String switchId, 
			@RequestParam(required = true) String beginDate, 
			@RequestParam(required = true) String endDate) {
		Map<String, Object> measureMap = new HashMap<String, Object>();
		measureMap.put("AB相", 
				voltageService2.selectByTime(switchId, beginDate, endDate, "A"));
		measureMap.put("BC相",
				voltageService2.selectByTime(switchId, beginDate, endDate, "B"));
		HighVoltageVoltageChartData chartData = new HighVoltageVoltageChartData();
		return chartData.getJsonChart(measureMap);
	}
	
	/**
	 * 返回电流
	 * @param id
	 * @param beginDate
	 * @param endDate
	 * @param sensorAddress
	 * @return
	 */
	@RequiresAuthentication
	@RequestMapping("/chart/high_voltage_current")
	@ResponseBody
	public ChartData getElectronicCurrentJsonChart(
			@RequestParam(required = true) String switchId, 
			@RequestParam(required = true) String beginDate, 
			@RequestParam(required = true) String endDate) {
		Map<String, Object> measureMap = new HashMap<String, Object>();
		measureMap.put("A相", 
				currentService2.selectByTime(switchId, beginDate, endDate, "A"));
		measureMap.put("B相",
				currentService2.selectByTime(switchId, beginDate, endDate, "B"));
		measureMap.put("C相",
				currentService2.selectByTime(switchId, beginDate, endDate, "C"));
		HighVoltageCurrentChartData chartData = new HighVoltageCurrentChartData();
		return chartData.getJsonChart(measureMap);
	}
	
}
