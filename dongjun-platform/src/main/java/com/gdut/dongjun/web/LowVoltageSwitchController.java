package com.gdut.dongjun.web;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.LowVoltageSwitch;
import com.gdut.dongjun.service.device.LowVoltageSwitchService;
import com.gdut.dongjun.service.webservice.client.HardwareServiceClient;
import com.gdut.dongjun.util.ClassLoaderUtil;
import com.gdut.dongjun.util.DownloadAndUploadUtil;
import com.gdut.dongjun.util.MapUtil;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

@Controller
@RequestMapping("/dongjun")
public class LowVoltageSwitchController {

	@Autowired
	private LowVoltageSwitchService switchService;
	
	@Resource(name="hardwareServiceClient")
	private HardwareServiceClient hardwareServiceClient;
	
	private static final Logger logger = Logger
			.getLogger(LowVoltageHitchEventController.class);

	/**
	 * 
	 * @Title: getLineSwitchList
	 * @Description: TODO
	 * @param @param lineId
	 * @param @param model
	 * @param @return
	 * @return String
	 * @throws high_voltage_
	 */
	@RequestMapping("/low_voltage_switch_manager")
	public String getLineSwitchList(String platformId, Model model) {

		if (platformId != null) {

			model.addAttribute("switches", switchService
					.selectByParameters(MyBatisMapUtil.warp("group_id", platformId)));
		} else {
			model.addAttribute("switches",
					switchService.selectByParameters(null));
		}
		return "low_voltage_switch_manager";
	}

	/**
	 * 
	 * @Title: getAllLowVoltage_Switch
	 * @Description: 用于百度地图的描点
	 * @param @param model
	 * @param @return
	 * @return Object
	 * @throws
	 */
	@RequiresAuthentication
	@RequestMapping("/get_all_low_voltage_switch")
	@ResponseBody
	public Object getAllLowVoltage_Switch(Model model) {

		return switchService.selectByParameters(null);
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
	@RequiresAuthentication
	@RequestMapping("/switch_list_by_platform_id")
	@ResponseBody
	public Object getLineSwitchListByLineId(String platformId, Model model) {
		if (null == platformId || "".equals(platformId)) {
			return "";
		}
		List<LowVoltageSwitch> switchs = switchService
				.selectByParameters(MyBatisMapUtil.warp("group_id", platformId));
		HashMap<String, Object> map = (HashMap<String, Object>) MapUtil.warp(
				"draw", 1);
		int size = switchs.size();
		map.put("recordsTotal", size);
		map.put("data", switchs);
		map.put("recordsFiltered", size);
		return map;
	}
	

	/**
	 * 
	 * @Title: selectByLineIdInAsc
	 * @Description: TODO
	 * @param @param lineId
	 * @param @param model
	 * @param @return
	 * @return Object
	 * @throws
	 */
	@RequiresAuthentication
	@RequestMapping("/selectLVByPlatformIdInAsc")
	@ResponseBody
	public Object selectByLineIdInAsc(
			@RequestParam(required = true) String platformId, Model model) {

		List<LowVoltageSwitch> switchs = switchService
				.selectByParameters(MyBatisMapUtil.warp("group_id", platformId));

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
	@RequiresPermissions("platform_group_admin:device")
	@RequestMapping("/del_switch")
	@ResponseBody
	public ResponseMessage delSwitch(
			@RequestParam(required = true) String switchId,
			Model model, RedirectAttributes redirectAttributes) {

		String platformId = switchService.selectByPrimaryKey(switchId).getGroupId();
		try {
			switchService.deleteByPrimaryKey(switchId);// 删除这个开关
		} catch (Exception e) {
			logger.error("删除低压开关失败");
			e.printStackTrace();
			return ResponseMessage.danger("删除低压开关失败");
		}
		return ResponseMessage.success(platformId);
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
	@RequiresPermissions("platform_group_admin:device")
	@RequestMapping("/edit_switch")
	@ResponseBody
	public ResponseMessage editSwitch(
			LowVoltageSwitch switch1, 
			String platformId,
			Model model,
			RedirectAttributes redirectAttributes) {

		// @RequestParam(required = true)
		// 进不来
		if (null == switch1.getId() || "".equals(switch1.getId())) {
			switch1.setId(UUIDUtil.getUUID());
		}
		try {
			switch1.setGroupId(platformId);
			switchService.updateByPrimaryKeySelective(switch1);
		} catch (Exception e) {
			logger.error("修改低压开关失败");
			e.printStackTrace();
			return ResponseMessage.danger("修改低压开关失败");
		}
		return ResponseMessage.success(platformId);
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
	@RequiresAuthentication
	@RequestMapping(value = "/downloadEmptylvExcel")
	public ResponseEntity<byte[]> downloadEmptylvExcel(
			HttpServletRequest request, HttpServletResponse respone,
			String clazzId) throws Exception {

		// 3.处理目标文件路径
		String fileName = "低压开关信息";
		String relativePath = ClassLoaderUtil.getExtendResource("../",
				"spring-boot_mybatis_bootstrap").toString();

//		if ("".equals(relativePath)) {
//
//			return null;
//		}

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
	@RequiresAuthentication
	@RequestMapping(value = "/downloadlvExcel")
	public ResponseEntity<byte[]> downloadlvExcel(HttpServletRequest request,
			HttpServletResponse respone, String clazzId) throws Exception {

		List<LowVoltageSwitch> sapis = switchService.selectByParameters(null);
		System.out.println(sapis.size());

		// 3.处理目标文件路径
		String fileName = "低压开关信息";
		String relativePath = ClassLoaderUtil.getExtendResource("../",
				"spring-boot_mybatis_bootstrap").toString();
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
	 * @Title: saveStudentAndParent
	 * @Description: 上传学生家长excel
	 * @param @param file
	 * @param @param request
	 * @param @param session
	 * @param @return
	 * @param @throws Exception
	 * @return String
	 * @throws
	 */
	@RequiresAuthentication
	@ResponseBody
	@RequestMapping(value = "/uploadlvSwitchExcel")
	public Object uploadlvSwitchExcel(@RequestParam("file") MultipartFile file,
			Model model, HttpServletRequest request, String platformId)
			throws Exception {

		MultipartFile[] files = { file };

		String realPath = request.getSession().getServletContext()
				.getRealPath("/uploadlvSwitchExcel");
		realPath = realPath.replace("/", "\\");

		// 1.保存文件到服务器
		String[] fileNames = DownloadAndUploadUtil.fileUpload(files, realPath);
		String f = realPath + "\\" + fileNames[0];

		// 2.解析excel并保存到数据库
		if (platformId == null || "".equals(platformId)) {

			return false;
		} else {

			switchService.uploadSwitch(f, platformId);
		}
		// 3.数据读取完后删除掉文件
		new File(f).delete();
		return ResponseMessage.success(platformId);
	}

}
