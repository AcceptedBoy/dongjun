package com.gdut.dongjun.web;

import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.domain.vo.AvailableHighVoltageSwitch;

import com.gdut.dongjun.service.common.CommonSwitch;
import com.gdut.dongjun.service.device.HighVoltageSwitchService;
import com.gdut.dongjun.service.webservice.client.CentorServiceClient;
import com.gdut.dongjun.service.webservice.client.HardwareServiceClient;
import com.gdut.dongjun.service.webservice.client.po.SwitchGPRS;
import com.gdut.dongjun.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/dongjun")
public class HighVoltageSwitchController {

	@Autowired
	private HighVoltageSwitchService switchService;

	@Autowired
	private HardwareServiceClient hardwareClient;

	@Autowired
	private CentorServiceClient centorServiceClient;

	@Autowired
	private CommonSwitch commonSwitch;
	
	private static final Logger logger = Logger.getLogger(HighVoltageHitchEventController.class);

	/**
	 * 
	 * @Title: getLineSwitchList
	 * @Description: TODO
	 * @param @param lineId
	 * @param @param model
	 * @param @return
	 * @return String
	 * @throws
	 */
	@RequestMapping("/high_voltage_switch_manager")
	public String getLineSwitchList(String lineId, Model model) {

		if(commonSwitch.canService()) {
			model.addAttribute("switches",
					centorServiceClient.getService().switchsOfLine(1, lineId));
		} else {
			model.addAttribute("switches", switchService.selectByParameters(
					MyBatisMapUtil.warp("line_id", lineId)));
		}

		return "high_voltage_switch_manager";
	}

	/**
	 * 
	 * @Title: getAllLowVoltage_Switch
	 * @Description: TODO
	 * @param @param model
	 * @param @return
	 * @return Object
	 * @throws
	 */
	@RequestMapping("/get_all_high_voltage_switch")
	@ResponseBody
	public Object getAllLowVoltage_Switch() {

		if(commonSwitch.canService()) {
			return centorServiceClient.getService().switchsOfLine(1, null);
		} else {
			return switchService.selectByParameters(null);
		}
	}

	/**
	 * 
	 * @Title: getLineSwitchList
	 * @Description: TODO
	 * @param @param lineId
	 * @param @param model
	 * @param @return
	 * @return String
	 * @throws
	 */
	@RequestMapping("/high_voltage_switch_list_by_line_id")
	@ResponseBody
	public Object getLineSwitchListByLineId(
			@RequestParam(required = true) String lineId, Model model) {

		List<AvailableHighVoltageSwitch> switchs;
		if(commonSwitch.canService()) {
			switchs = centorServiceClient.getService().switchsOfLine(1, lineId);
		} else {
			switchs = AvailableHighVoltageSwitch.change2default(
					switchService.selectByParameters(MyBatisMapUtil.warp("line_id", lineId)));
		}

		HashMap<String, Object> map = (HashMap<String, Object>) MapUtil.warp(
				"draw", 1);
		map.put("recordsTotal", switchs.size());
		map.put("data", updateDate(switchs));
		map.put("data", switchs);
		map.put("recordsFiltered", switchs.size());
		return map;
	}
	
	@RequestMapping("/online_order")
	@ResponseBody
	public Object getOnlineOrder(@RequestParam(required = false) String lineId) {
		
		int[] result = new int[20];
		List<HighVoltageSwitch> switchs = switchService
				.selectByParameters(MyBatisMapUtil.warp("line_id", lineId));
		
		for(int length = switchs.size(), i = 0, j = 0; i < length; ++i) {
			
			if(search(switchs.get(i).getId())) {
				result[j] = i + 1;
				++j;
			}
		}
		return result;
	}
	
	private List<AvailableHighVoltageSwitch> updateDate(List<AvailableHighVoltageSwitch> switchs) {
		
		String date = TimeUtil.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
		for(AvailableHighVoltageSwitch hvSwitch : switchs) {
			
			if(search(hvSwitch.getId())) {
				hvSwitch.setOnlineTime(date);
			}
		}
		return switchs;
	}
	
	private boolean search(String id) {
		
		if(id == null) {
			return false;
		}

		//List<SwitchGPRS> list = hardwareService.getCtxInstance();
		List<SwitchGPRS> list = hardwareClient.getService().getCtxInstance();
		for(SwitchGPRS gprs : list) {
			if(gprs.getId() != null && gprs.getId().equals(id)) {
				return true;
			}
		}

		return false;
 	}
	
	public void updateOnlineTime(List<HighVoltageSwitch> switchs) {
		
		String date = TimeUtil.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
		for(HighVoltageSwitch hvSwitch : switchs) {
			
			if(search(hvSwitch.getId())) {
				hvSwitch.setOnlineTime(date);
			}
			switchService.updateSwitch(hvSwitch);
		}
	}
	
	public void updateOnlineTime(HighVoltageSwitch highVoltageSwitch) {
		
		List<HighVoltageSwitch> list = new ArrayList<>(1);
		list.add(highVoltageSwitch);
		updateOnlineTime(list);
	}

	/**
	 * 
	 * @Title: selectHVByLineIdInAsc
	 * @Description: TODO
	 * @param @param lineId
	 * @param @param model
	 * @param @return
	 * @return Object
	 * @throws
	 */
	@RequestMapping("/selectHVByLineIdInAsc")
	@ResponseBody
	public Object selectHVByLineIdInAsc(
			@RequestParam(required = true) String lineId, Model model) {

		List<HighVoltageSwitch> switchs = switchService
				.selectByParameters(MyBatisMapUtil.warp("line_id", lineId));

		return switchs;
	}

	/**
	 * 
	 * @Title: delSwitch
	 * @Description: TODO
	 * @param @param switchId
	 * @param @param model
	 * @param @param redirectAttributes
	 * @param @return
	 * @return String
	 * @throws
	 */
	@RequestMapping("/del_high_voltage_switch")
	@ResponseBody
	public String delSwitch(@RequestParam(required = true) String switchId,
			Model model, RedirectAttributes redirectAttributes) {

		HighVoltageSwitch delSwitch = switchService.selectByPrimaryKey(switchId);
		try {
			if(delSwitch != null) {
				// 删除这个开关
				switchService.deleteByPrimaryKey(switchId);
			}
		} catch (Exception e) {
			logger.error("删除开关失败！");
			return null;
		}
		return delSwitch != null ? delSwitch.getLineId() : "";
	}

	/**
	 * 
	 * @Title: editSwitch
	 * @Description: TODO
	 * @param @param switch1
	 * @param @param model
	 * @param @param redirectAttributes
	 * @param @return
	 * @return String
	 * @throws
	 */
	@RequestMapping("/edit_high_voltage_switch")
	@ResponseBody
	public Object editSwitch(@Valid HighVoltageSwitch switch1,
			Model model, RedirectAttributes redirectAttributes) {

		// @RequestParam(required = true)
		// 进不来
		if (switch1.getId().equals("")) {
			switch1.setId(UUIDUtil.getUUID());
		}
		
		try {
			
			switchService.updateByPrimaryKey(switch1);
		} catch (Exception e) {
			
			logger.error("修改开关失败！");
			return null;
		}
		return switch1.getLineId();
	}

	/**
	 * 
	 * @Title: downloadlvExcel
	 * @Description: 导出模板
	 * @param @param request
	 * @param @param respone
	 * @param @param clazzId
	 * @param @return
	 * @param @throws Exception
	 * @return ResponseEntity<byte[]>
	 * @throws
	 */
	@RequestMapping(value = "/downloadEmptyhvExcel")
	public ResponseEntity<byte[]> downloadEmptylvExcel(
			HttpServletRequest request, HttpServletResponse respone,
			String clazzId) throws Exception {

		// 3.处理目标文件路径
		String fileName = "高压开关信息";
		String relativePath = ClassLoaderUtil.getExtendResource("../",
				"dongjun-website").toString();

		if ("".equals(relativePath)) {

			return null;
		}

		String realPath = relativePath.replace("/", "\\");
		File file = new File(realPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		String filePath = realPath + "\\" + fileName;
		// 4.生成excel文件
		switchService.createSwitchExcel(filePath, null);

		File targetFile = new File(filePath);
		return DownloadAndUploadUtil.download(request, targetFile, fileName);
	}

	/**
	 * 
	 * @Title: downloadStudentAndParent
	 * @Description: 下载学生和家长信息Excel表
	 * @param @param request
	 * @param @param respone
	 * @param @return
	 * @param @throws Exception
	 * @return ResponseEntity<byte[]>
	 * @throws
	 */
	@RequestMapping(value = "/downloadhvExcel")
	public ResponseEntity<byte[]> downloadlvExcel(HttpServletRequest request,
			HttpServletResponse respone, String clazzId) throws Exception {

		List<HighVoltageSwitch> sapis = switchService.selectByParameters(null);

		// 3.处理目标文件路径
		String fileName = "高压开关信息";
		String relativePath = ClassLoaderUtil.getExtendResource("../",
				"dongjun-website").toString();
		String realPath = relativePath.replace("/", "\\");
		File file = new File(realPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		String filePath = realPath + "\\" + fileName;
		// 4.生成excel文件
		switchService.createSwitchExcel(filePath, sapis);

		File targetFile = new File(filePath);
		return DownloadAndUploadUtil.download(request, targetFile, fileName);
	}

	/**
	 * 
	 * @Title: uploadlvSwitchExcel
	 * @Description: TODO
	 * @param @param file
	 * @param @param model
	 * @param @param request
	 * @param @param lineId
	 * @param @return
	 * @param @throws Exception
	 * @return Object
	 * @throws
	 */
	@RequestMapping(value = "/uploadhvSwitchExcel")
	public Object uploadlvSwitchExcel(@RequestParam("file") MultipartFile file,
			Model model, HttpServletRequest request, String lineId)
			throws Exception {

		MultipartFile[] files = { file };

		String realPath = request.getSession().getServletContext()
				.getRealPath("/uploadhvSwitchExcel");
		realPath = realPath.replace("/", "\\");

		// 1.保存文件到服务器
		String[] fileNames = DownloadAndUploadUtil.fileUpload(files, realPath);
		String f = realPath + "\\" + fileNames[0];

		// 2.解析excel并保存到数据库
		if (lineId == null || "".equals(lineId)) {

			return false;
		} else {

			switchService.uploadSwitch(f, lineId);
		}
		// 3.数据读取完后删除掉文件
		new File(f).delete();
		return "redirect:high_voltage_switch_manager";
	}

}
