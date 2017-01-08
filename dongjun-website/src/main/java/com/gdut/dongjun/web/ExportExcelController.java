package com.gdut.dongjun.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.po.HighVoltageHitchEvent;
import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.domain.po.HistoryHighVoltageCurrent;
import com.gdut.dongjun.domain.po.HistoryHighVoltageVoltage;
import com.gdut.dongjun.domain.po.Line;
import com.gdut.dongjun.domain.vo.HighVoltageHitchEventVo;
import com.gdut.dongjun.domain.vo.HighVoltageVo;
import com.gdut.dongjun.service.HistoryHighVoltageCurrentService;
import com.gdut.dongjun.service.HistoryHighVoltageVoltageService;
import com.gdut.dongjun.service.LineService;
import com.gdut.dongjun.service.device.HighVoltageSwitchService;
import com.gdut.dongjun.service.device.current.HighVoltageCurrentService;
import com.gdut.dongjun.service.device.event.HighVoltageHitchEventService;
import com.gdut.dongjun.service.device.voltage.HighVoltageVoltageService;
import com.gdut.dongjun.util.ExcelExport;
import com.gdut.dongjun.util.MyBatisMapUtil;



/**
 * @author yzh
 *
 */
@Controller
@RequestMapping("/dongjun")
public class ExportExcelController {

	@Autowired
	public HistoryHighVoltageCurrentService HistoryCurrentService;
	
	@Autowired
	public HistoryHighVoltageVoltageService HistoryVoltageService;
	
	@Autowired
	public HighVoltageCurrentService HighVoltageCurrent;
	
	@Autowired
	public HighVoltageVoltageService HighVoltageVoltage;
	
	@Autowired
	public LineService lineService;
	
	@Autowired
	public HighVoltageHitchEventService highVoltageHitchEventService;
	
	@Autowired
	public HighVoltageSwitchService highVoltageSwitchService;
	
	@ResponseBody
	@RequestMapping("/download/history_current_excel")
	public String downloadhistory_current_excel(String fileLocation,String switch_id, HttpServletRequest request) throws IOException {
		List<HistoryHighVoltageCurrent>HistoryHighVoltageCurrentlist= HistoryCurrentService.selectByParameters(
				MyBatisMapUtil.warp("switch_id", switch_id));
		HighVoltageSwitch highVoltageSwitch=highVoltageSwitchService.selectByPrimaryKey(switch_id);
		List<HighVoltageVo> HighVoltageVolist=new ArrayList<HighVoltageVo>();
		for (HistoryHighVoltageCurrent historyHighVoltageCurrent : HistoryHighVoltageCurrentlist) {
			HighVoltageVo a1=new HighVoltageVo();
			a1.setName(highVoltageSwitch.getName());
			a1.setPhase(historyHighVoltageCurrent.getPhase());
			a1.setTime(historyHighVoltageCurrent.getTime());
			a1.setValue(historyHighVoltageCurrent.getValue());
			HighVoltageVolist.add(a1);
		}
		String[] headers = { "时间", "原相", "确切值","开关名"};
		List<String> exception = new ArrayList<String>();
		String title = highVoltageSwitch.getName()+"-历史电流表";
		String path = fileLocation;
		ExcelExport<HighVoltageVo> ee = new ExcelExport<>();
		try {
			ee.excelExport(HighVoltageVolist, HighVoltageVo.class.getName(), request, highVoltageSwitch.getName()+"-历史电流表"+".xlsx", headers, title, path, exception);
		} catch (IOException e) {
			// 异常页面
			e.printStackTrace();
			throw e;
		}
		
		return "下载成功";
	}
	

	@ResponseBody
	@RequestMapping("/download/history_Voltage_excel")
	public String downloadhistory_Voltage_excel(String fileLocation,String switch_id, HttpServletRequest request) throws IOException {
		List<HistoryHighVoltageVoltage>HistoryHighVoltageVoltagelist= HistoryVoltageService.selectByParameters(
				MyBatisMapUtil.warp("switch_id", switch_id));
		HighVoltageSwitch highVoltageSwitch=highVoltageSwitchService.selectByPrimaryKey(switch_id);
		List<HighVoltageVo> HighVoltageVolist=new ArrayList<HighVoltageVo>();
		for (HistoryHighVoltageVoltage historyHighVoltageVoltage : HistoryHighVoltageVoltagelist) {
			HighVoltageVo a1=new HighVoltageVo();
			a1.setName(highVoltageSwitch.getName());
			a1.setPhase(historyHighVoltageVoltage.getPhase());
			a1.setTime(historyHighVoltageVoltage.getTime());
			a1.setValue(historyHighVoltageVoltage.getValue());
			HighVoltageVolist.add(a1);
		}
		String[] headers = { "时间", "原相", "确切值","开关名"};
		List<String> exception = new ArrayList<String>();
		String title = highVoltageSwitch.getName()+"-历史电压表";
		String path = fileLocation;
		ExcelExport<HighVoltageVo> ee = new ExcelExport<>();
		try {
			ee.excelExport(HighVoltageVolist, HighVoltageVo.class.getName(), request, highVoltageSwitch.getName()+"-历史电流表"+".xlsx", headers, title, path, exception);
		} catch (IOException e) {
			// 异常页面
			e.printStackTrace();
			throw e;
		}
		return "下载成功";
	}
	
	
	@ResponseBody
	@RequestMapping("/download/switch_event_excel")
	public String downloadswitch_event_excel(String line_id,String fileLocation, HttpServletRequest request) throws IOException {
		Line line=lineService.selectByPrimaryKey(line_id);
		List<HighVoltageSwitch>highVoltageVoltagelist= highVoltageSwitchService.selectByParameters(
				MyBatisMapUtil.warp("line_id", line_id));
		List<HighVoltageHitchEventVo> HighVoltageHitchEventVolist=new ArrayList<HighVoltageHitchEventVo>();
		for (HighVoltageSwitch highVoltageSwitch : highVoltageVoltagelist) {
			List<HighVoltageHitchEvent>HighVoltageHitchEventlist= highVoltageHitchEventService.selectByParameters(
					MyBatisMapUtil.warp("switch_id", highVoltageSwitch.getId()));
			for (HighVoltageHitchEvent highVoltageHitchEvent : HighVoltageHitchEventlist) {
				HighVoltageHitchEventVo a1=new HighVoltageHitchEventVo();
				a1.setChangeType(highVoltageHitchEvent.getChangeType());
				a1.setHitchPhase(highVoltageHitchEvent.getHitchPhase());
				a1.setHitchReason(highVoltageHitchEvent.getHitchReason());
				a1.setHitchTime(highVoltageHitchEvent.getHitchTime());
				a1.setSolvePeople(highVoltageHitchEvent.getSolvePeople());
				a1.setSolveTime(highVoltageHitchEvent.getSolveTime());
				a1.setSolveWay(highVoltageHitchEvent.getSolveWay());
				a1.setSwitchName(highVoltageSwitch.getName());
				a1.setLine_name(line.getName());
				HighVoltageHitchEventVolist.add(a1);
			}
		}
		String[] headers = {"事件类型", "事件原因","事件相位","事件发生时间","解决人员","解决时间","解决方式","开关名","线路名"};
		List<String> exception = new ArrayList<String>();
		String title = line.getName()+"操作记录";
		String path = fileLocation;
		exception.add("switchAddress");
		exception.add("id");
		ExcelExport<HighVoltageHitchEventVo> ee = new ExcelExport<>();
		try {
			ee.excelExport(HighVoltageHitchEventVolist, HighVoltageHitchEventVo.class.getName(), request, line.getName()+"-操作记录"+".xlsx", headers, title, path, exception);
		} catch (IOException e) {
			// 异常页面
			e.printStackTrace();
			throw e;
		}
		
		return "下载成功";
	}
	
	@ResponseBody
	@RequestMapping("/download/line_current_Voltage_excel")
	public String downloadline_current_Voltage_excel(String fileLocation, String line_id,HttpServletRequest request) throws IOException {
		Line line=lineService.selectByPrimaryKey(line_id);
		List<HighVoltageSwitch>highVoltageVoltagelist= highVoltageSwitchService.selectByParameters(
				MyBatisMapUtil.warp("line_id", line_id));
		List<HighVoltageVo> HighVoltageVolist=new ArrayList<HighVoltageVo>();
		for (HighVoltageSwitch highVoltageSwitch : highVoltageVoltagelist) {
			List<com.gdut.dongjun.domain.po.HighVoltageVoltage>HistoryHighVoltageVoltagelist= HighVoltageVoltage.selectByParameters(
					MyBatisMapUtil.warp("switch_id", highVoltageSwitch.getId()));
			for (com.gdut.dongjun.domain.po.HighVoltageVoltage historyHighVoltageVoltage : HistoryHighVoltageVoltagelist) {
				HighVoltageVo a1=new HighVoltageVo();
				a1.setName(highVoltageSwitch.getName());
				a1.setPhase(historyHighVoltageVoltage.getPhase());
				a1.setTime(historyHighVoltageVoltage.getTime());
				a1.setValue(historyHighVoltageVoltage.getValue());
				HighVoltageVolist.add(a1);
			}
		}
		String[] headers = { "时间", "原相", "确切值","开关名","线路名"};
		List<String> exception = new ArrayList<String>();
		
		String title = line.getName()+"当前电压记录";
		String path = fileLocation;
		ExcelExport<HighVoltageVo> ee = new ExcelExport<>();
		try {
			ee.excelExport(HighVoltageVolist, HighVoltageVo.class.getName(), request, line.getName()+"-当前电压"+".xlsx", headers, title, path, exception);
		} catch (IOException e) {
			// 异常页面
			e.printStackTrace();
			throw e;
		}
		
		
		return "下载成功";
	}
	
	@ResponseBody
	@RequestMapping("/download/line_current_current_excel")
	public String downloadline_current_current_excel(String line_id,String fileLocation, HttpServletRequest request) throws IOException {
		Line line=lineService.selectByPrimaryKey(line_id);
		List<HighVoltageSwitch>highVoltageVoltagelist= highVoltageSwitchService.selectByParameters(
				MyBatisMapUtil.warp("line_id", line_id));
		List<HighVoltageVo> HighVoltageVolist=new ArrayList<HighVoltageVo>();
		for (HighVoltageSwitch highVoltageSwitch : highVoltageVoltagelist) {
			List<com.gdut.dongjun.domain.po.HighVoltageCurrent>HistoryHighVoltageVoltagelist= HighVoltageCurrent.selectByParameters(
					MyBatisMapUtil.warp("switch_id", highVoltageSwitch.getId()));
			for (com.gdut.dongjun.domain.po.HighVoltageCurrent historyHighVoltageVoltage : HistoryHighVoltageVoltagelist) {
				HighVoltageVo a1=new HighVoltageVo();
				a1.setName(highVoltageSwitch.getName());
				a1.setPhase(historyHighVoltageVoltage.getPhase());
				a1.setTime(historyHighVoltageVoltage.getTime());
				a1.setValue(historyHighVoltageVoltage.getValue());
				HighVoltageVolist.add(a1);
			}
		}
		String[] headers = { "时间", "原相", "确切值","开关名","线路名"};
		List<String> exception = new ArrayList<String>();
		String title = line.getName()+"当前电流记录";
		
		String path = fileLocation;
		ExcelExport<HighVoltageVo> ee = new ExcelExport<>();
		try {
			ee.excelExport(HighVoltageVolist, HighVoltageVo.class.getName(), request, line.getName()+"-当前电流"+".xlsx", headers, title, path, exception);
		} catch (IOException e) {
			// 异常页面
			e.printStackTrace();
			throw e;
		}
		
		return "下载成功";
	}
	
	
}
