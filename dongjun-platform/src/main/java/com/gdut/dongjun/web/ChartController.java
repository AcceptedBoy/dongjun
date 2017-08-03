package com.gdut.dongjun.web;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.po.ElectronicModule;
import com.gdut.dongjun.domain.po.TemperatureMeasure;
import com.gdut.dongjun.domain.po.TemperatureModule;
import com.gdut.dongjun.domain.po.TemperatureSensor;
import com.gdut.dongjun.domain.vo.chart.ChartData;
import com.gdut.dongjun.domain.vo.chart.ElectronicCurrentChartData;
import com.gdut.dongjun.domain.vo.chart.ElectronicPowerChartData;
import com.gdut.dongjun.domain.vo.chart.ElectronicVoltageChartData;
import com.gdut.dongjun.domain.vo.chart.TemperatureChartData;
import com.gdut.dongjun.service.device.ElectronicModuleCurrentService;
import com.gdut.dongjun.service.device.ElectronicModulePowerService;
import com.gdut.dongjun.service.device.ElectronicModuleService;
import com.gdut.dongjun.service.device.ElectronicModuleVoltageService;
import com.gdut.dongjun.service.device.TemperatureModuleService;
import com.gdut.dongjun.service.device.TemperatureSensorService;
import com.gdut.dongjun.service.device.temperature.TemperatureMeasureService;
import com.gdut.dongjun.util.ClassLoaderUtil;
import com.gdut.dongjun.util.DownloadAndUploadUtil;

@Controller
@RequestMapping("/dongjun")
public class ChartController {

	@Autowired
	private TemperatureMeasureService temMeasureService;
	@Autowired
	private TemperatureSensorService sensorService;
	@Autowired
	private TemperatureModuleService temModuleService;
	@Autowired
	private ElectronicModuleCurrentService currentService;
	@Autowired
	private ElectronicModulePowerService powerService;
	@Autowired
	private ElectronicModuleVoltageService voltageService;
	@Autowired
	private ElectronicModuleService elecModuleService;

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
	@RequestMapping("/chart/temperature")
	@ResponseBody
	public ChartData getJsonChart(
			@RequestParam(required = true) String moduleId, 
			@RequestParam(required = true) String beginDate, 
			@RequestParam(required = true) String endDate) {
//		List<DataMonitorSubmodule> mappings = submoduleService.selectByParameters(MyBatisMapUtil.warp("data_monitor_id", monitorId));
//		String deviceId = null;
//		for (DataMonitorSubmodule mapping : mappings) {
//			if (3 == mapping.getModuleType()) {
//				deviceId = mapping.getModuleId();
//				break;
//			}
//		}
		TemperatureChartData chartData = new TemperatureChartData();
		Map<String, Object> measureMap = new HashMap<String, Object>();
		List<TemperatureSensor> sensors = sensorService.selectAllType(moduleId);
		for (TemperatureSensor sensor : sensors) {
			List<TemperatureMeasure> measures = temMeasureService.selectByTime(moduleId, sensor.getTag(), beginDate, endDate);
			measureMap.put(changeType(sensor.getType()), measures);
		}
		return chartData.getJsonChart(measureMap);
	}
	
	private String changeType(int num) {
		switch (num) {
		case 1: return "进线A相";
		case 2: return "进线B相";
		case 3: return "进线C相";
		case 4: return "出线A相";
		case 5: return "出线B相";
		case 6: return "出线C相";
		default: return null;
		}
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
	@RequestMapping("/chart/electronic_current")
	@ResponseBody
	public ChartData getElectronicCurrentJsonChart(
			@RequestParam(required = true) String moduleId, 
			@RequestParam(required = true) String beginDate, 
			@RequestParam(required = true) String endDate) {
//		List<DataMonitorSubmodule> mappings = submoduleService.selectByParameters(MyBatisMapUtil.warp("data_monitor_id", monitorId));
//		String deviceId = null;
//		for (DataMonitorSubmodule mapping : mappings) {
//			if (HitchConst.MODULE_ELECTRICITY == mapping.getModuleType()) {
//				deviceId = mapping.getModuleId();
//				break;
//			}
//		}
		Map<String, Object> measureMap = new HashMap<String, Object>();
		measureMap.put("A相", 
				currentService.selectByTime(moduleId, beginDate, endDate, "A"));
		measureMap.put("B相",
				currentService.selectByTime(moduleId, beginDate, endDate, "B"));
		measureMap.put("C相",
				currentService.selectByTime(moduleId, beginDate, endDate, "C"));
		ElectronicCurrentChartData chartData = new ElectronicCurrentChartData();
		return chartData.getJsonChart(measureMap);
	}
	
	/**
	 * 返回电压
	 * @param id
	 * @param beginDate
	 * @param endDate
	 * @param sensorAddress
	 * @return
	 */
	@RequiresAuthentication
	@RequestMapping("/chart/electronic_voltage")
	@ResponseBody
	public ChartData getElectronicVoltageJsonChart(
			@RequestParam(required = true) String moduleId, 
			@RequestParam(required = true) String beginDate, 
			@RequestParam(required = true) String endDate) {
//		List<DataMonitorSubmodule> mappings = submoduleService.selectByParameters(MyBatisMapUtil.warp("data_monitor_id", monitorId));
//		String deviceId = null;
//		for (DataMonitorSubmodule mapping : mappings) {
//			if (HitchConst.MODULE_ELECTRICITY == mapping.getModuleType()) {
//				deviceId = mapping.getModuleId();
//				break;
//			}
//		}
		Map<String, Object> measureMap = new HashMap<String, Object>();
		measureMap.put("A相", 
				voltageService.selectByTime(moduleId, beginDate, endDate, "A"));
		measureMap.put("B相",
				voltageService.selectByTime(moduleId, beginDate, endDate, "B"));
		measureMap.put("C相",
				voltageService.selectByTime(moduleId, beginDate, endDate, "C"));
		ElectronicVoltageChartData chartData = new ElectronicVoltageChartData();
		return chartData.getJsonChart(measureMap);
	}
	
	/**
	 * 返回功率
	 * @param id
	 * @param beginDate
	 * @param endDate
	 * @param sensorAddress
	 * @return
	 */
	@RequiresAuthentication
	@RequestMapping("/chart/electronic_power")
	@ResponseBody
	public ChartData getElectronicPowerJsonChart(
			@RequestParam(required = true) String moduleId, 
			@RequestParam(required = true) String beginDate, 
			@RequestParam(required = true) String endDate) {
//		List<DataMonitorSubmodule> mappings = submoduleService.selectByParameters(MyBatisMapUtil.warp("data_monitor_id", monitorId));
//		String deviceId = null;
//		for (DataMonitorSubmodule mapping : mappings) {
//			if (HitchConst.MODULE_ELECTRICITY == mapping.getModuleType()) {
//				deviceId = mapping.getModuleId();
//				break;
//			}
//		}
		Map<String, Object> measureMap = new HashMap<String, Object>();
		measureMap.put("A相", 
				powerService.selectByTime(moduleId, beginDate, endDate, "A"));
		measureMap.put("B相",
				powerService.selectByTime(moduleId, beginDate, endDate, "B"));
		measureMap.put("C相",
				powerService.selectByTime(moduleId, beginDate, endDate, "C"));
		measureMap.put("总功率",
				powerService.selectByTime(moduleId, beginDate, endDate, "D"));
		ElectronicPowerChartData chartData = new ElectronicPowerChartData();
		return chartData.getJsonChart(measureMap);
	}
	
	/**
	 * 返回功率
	 * @param id
	 * @param beginDate
	 * @param endDate
	 * @param sensorAddress
	 * @return
	 * @throws IOException 
	 */
	@RequiresAuthentication
	@RequestMapping("/chart/excel/voltage")
	@ResponseBody
	public ResponseEntity<byte[]> downloadVoltageExcel(
			@RequestParam(required = true) String moduleId, 
			@RequestParam(required = true) String beginDate, 
			@RequestParam(required = true) String endDate,
			HttpServletRequest request) throws IOException {
//		List<DataMonitorSubmodule> mappings = submoduleService.selectByParameters(MyBatisMapUtil.warp("data_monitor_id", monitorId));
//		String deviceId = null;
//		for (DataMonitorSubmodule mapping : mappings) {
//			if (HitchConst.MODULE_ELECTRICITY == mapping.getModuleType()) {
//				deviceId = mapping.getModuleId();
//				break;
//			}
//		}
		Map<String, Object> measureMap = new HashMap<String, Object>();
		measureMap.put("A相", 
				voltageService.selectByTime(moduleId, beginDate, endDate, "A"));
		measureMap.put("B相",
				voltageService.selectByTime(moduleId, beginDate, endDate, "B"));
		measureMap.put("C相",
				voltageService.selectByTime(moduleId, beginDate, endDate, "C"));
		ElectronicVoltageChartData chartData = new ElectronicVoltageChartData();
		ChartData d =  chartData.getJsonChart(measureMap);
		ElectronicModule module = elecModuleService.selectByPrimaryKey(moduleId);
		//文件名称
		String fileName = module.getName() + "-" + module.getDeviceNumber() + "-" + "电压";
		//处理文件目录
		String relativePath = ClassLoaderUtil.getExtendResource("../",
				"dongjun-platform").toString();
		String realPath = relativePath.replace("/", "\\");
		File file = new File(realPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		String filePath = realPath + "\\" + fileName;
		//生成文件
		voltageService.createExcel(d, filePath);
		File targetFile = new File(filePath);
		return DownloadAndUploadUtil.download(request, targetFile, fileName);
	}
	
	/**
	 * 返回功率
	 * @param id
	 * @param beginDate
	 * @param endDate
	 * @param sensorAddress
	 * @return
	 * @throws IOException 
	 */
	@RequiresAuthentication
	@RequestMapping("/chart/excel/current")
	@ResponseBody
	public ResponseEntity<byte[]> downloadCurrentExcel(
			@RequestParam(required = true) String moduleId, 
			@RequestParam(required = true) String beginDate, 
			@RequestParam(required = true) String endDate,
			HttpServletRequest request) throws IOException {
//		List<DataMonitorSubmodule> mappings = submoduleService.selectByParameters(MyBatisMapUtil.warp("data_monitor_id", monitorId));
//		String deviceId = null;
//		for (DataMonitorSubmodule mapping : mappings) {
//			if (HitchConst.MODULE_ELECTRICITY == mapping.getModuleType()) {
//				deviceId = mapping.getModuleId();
//				break;
//			}
//		}
		Map<String, Object> measureMap = new HashMap<String, Object>();
		measureMap.put("A相", 
				currentService.selectByTime(moduleId, beginDate, endDate, "A"));
		measureMap.put("B相",
				currentService.selectByTime(moduleId, beginDate, endDate, "B"));
		measureMap.put("C相",
				currentService.selectByTime(moduleId, beginDate, endDate, "C"));
		measureMap.put("总功率",
				currentService.selectByTime(moduleId, beginDate, endDate, "D"));
		ElectronicCurrentChartData chartData = new ElectronicCurrentChartData();
		ChartData d =  chartData.getJsonChart(measureMap);
		ElectronicModule module = elecModuleService.selectByPrimaryKey(moduleId);
		//文件名称
		String fileName = module.getName() + "-" + module.getDeviceNumber() + "-" + "电流";
		//处理文件目录
		String relativePath = ClassLoaderUtil.getExtendResource("../",
				"dongjun-platform").toString();
		String realPath = relativePath.replace("/", "\\");
		File file = new File(realPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		String filePath = realPath + "\\" + fileName;
		//生成文件
		currentService.createExcel(d, filePath);
		File targetFile = new File(filePath);
		return DownloadAndUploadUtil.download(request, targetFile, fileName);
	}
	
	/**
	 * 返回功率
	 * @param id
	 * @param beginDate
	 * @param endDate
	 * @param sensorAddress
	 * @return
	 * @throws IOException 
	 */
	@RequiresAuthentication
	@RequestMapping("/chart/excel/power")
	@ResponseBody
	public ResponseEntity<byte[]> downloadPowerExcel(
			@RequestParam(required = true) String moduleId, 
			@RequestParam(required = true) String beginDate, 
			@RequestParam(required = true) String endDate,
			HttpServletRequest request) throws IOException {
//		List<DataMonitorSubmodule> mappings = submoduleService.selectByParameters(MyBatisMapUtil.warp("data_monitor_id", monitorId));
//		String deviceId = null;
//		for (DataMonitorSubmodule mapping : mappings) {
//			if (HitchConst.MODULE_ELECTRICITY == mapping.getModuleType()) {
//				deviceId = mapping.getModuleId();
//				break;
//			}
//		}
		Map<String, Object> measureMap = new HashMap<String, Object>();
		measureMap.put("A相", 
				powerService.selectByTime(moduleId, beginDate, endDate, "A"));
		measureMap.put("B相",
				powerService.selectByTime(moduleId, beginDate, endDate, "B"));
		measureMap.put("C相",
				powerService.selectByTime(moduleId, beginDate, endDate, "C"));
		measureMap.put("总功率",
				powerService.selectByTime(moduleId, beginDate, endDate, "D"));
		ElectronicPowerChartData chartData = new ElectronicPowerChartData();
		ChartData d =  chartData.getJsonChart(measureMap);
		ElectronicModule module = elecModuleService.selectByPrimaryKey(moduleId);
		//文件名称
		String fileName = module.getName() + "-" + module.getDeviceNumber() + "-" + "功率";
		//处理文件目录
		String relativePath = ClassLoaderUtil.getExtendResource("../",
				"dongjun-platform").toString();
		String realPath = relativePath.replace("/", "\\");
		File file = new File(realPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		String filePath = realPath + "\\" + fileName;
		//生成文件
		powerService.createExcel(d, filePath);
		File targetFile = new File(filePath);
		return DownloadAndUploadUtil.download(request, targetFile, fileName);
	}
	
	/**
	 * 返回功率
	 * @param id
	 * @param beginDate
	 * @param endDate
	 * @param sensorAddress
	 * @return
	 * @throws IOException 
	 */
	@RequiresAuthentication
	@RequestMapping("/chart/excel/temperature")
	public ResponseEntity<byte[]> downloadTemperatureExcel(
			@RequestParam(required = true) String moduleId, 
			@RequestParam(required = true) String beginDate, 
			@RequestParam(required = true) String endDate,
			HttpServletRequest request) throws IOException {
//		List<DataMonitorSubmodule> mappings = submoduleService.selectByParameters(MyBatisMapUtil.warp("data_monitor_id", monitorId));
//		String deviceId = null;
//		for (DataMonitorSubmodule mapping : mappings) {
//			if (3 == mapping.getModuleType()) {
//				deviceId = mapping.getModuleId();
//				break;
//			}
//		}
		TemperatureChartData chartData = new TemperatureChartData();
		Map<String, Object> measureMap = new HashMap<String, Object>();
		List<TemperatureSensor> sensors = sensorService.selectAllType(moduleId);
		for (TemperatureSensor sensor : sensors) {
			List<TemperatureMeasure> measures = temMeasureService.selectByTime(moduleId, sensor.getTag(), beginDate, endDate);
			measureMap.put(changeType(sensor.getType()), measures);
		}
		ChartData d =  chartData.getJsonChart(measureMap);
		TemperatureModule module = temModuleService.selectByPrimaryKey(moduleId);
		//文件名称
		String fileName = module.getName() + "-" + module.getDeviceNumber() + "-" + "温度";
		//处理文件目录
		String relativePath = ClassLoaderUtil.getExtendResource("../",
				"dongjun-platform").toString();
		String realPath = relativePath.replace("/", "\\");
		File file = new File(realPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		String filePath = realPath + "\\" + fileName;
		//生成文件
		temModuleService.createExcel(d, filePath);
		File targetFile = new File(filePath);
		return DownloadAndUploadUtil.download(request, targetFile, fileName);
	}
	
	
}
