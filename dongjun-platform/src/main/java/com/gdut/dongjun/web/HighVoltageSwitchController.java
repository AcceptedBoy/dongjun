package com.gdut.dongjun.web;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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

import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.dto.util.SwitchStatusUtil;
import com.gdut.dongjun.service.HighVoltageSwitchService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.rmi.HardwareService;
import com.gdut.dongjun.util.ClassLoaderUtil;
import com.gdut.dongjun.util.DownloadAndUploadUtil;
import com.gdut.dongjun.util.MapUtil;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.TimeUtil;
import com.gdut.dongjun.util.UUIDUtil;

@Controller
@RequestMapping("/dongjun")
public class HighVoltageSwitchController {

	@Autowired
	private HighVoltageSwitchService switchService;
	
	@Resource(name="hardwareService")
	private HardwareService hardwareService;
	
	@Autowired
	private UserService userService;
	
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
	public String getLineSwitchList(String platformId, Model model) {

		if (platformId != null) {

			/*model.addAttribute("switches", AvailableHighVoltageSwitch.change2VoList(
					switchService
							.selectByParameters(MyBatisMapUtil.warp("line_id", lineId))));*/
			model.addAttribute("switches",
					switchService
							.selectByParameters(MyBatisMapUtil.warp("group_id", platformId)));
		} else {
			/*model.addAttribute("switches", AvailableHighVoltageSwitch.change2VoList(
					switchService.selectByParameters(null)));*/
			model.addAttribute("switches",
					switchService.selectByParameters(null));
		}
		return "high_voltage_switch_manager";
	}

	/** ResponseMessage
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

		return switchService.selectByParameters(null);
	}

	/**
	 * @throws  
	 * 
	 * @Title: getLineSwitchList
	 * @Description: TODO
	 * @param @param lineId
	 * @param @param model
	 * @param @return
	 * @return String
	 * @throws
	 */
	@RequestMapping("/high_voltage_switch_list_by_platform_id")
	@ResponseBody
	public Object getLineSwitchListByLineId(String platformId, Model model) {
		if (null == platformId || "".equals(platformId)) {
			return "";
		}
		List<HighVoltageSwitch> switchs = switchService
				.selectByParameters(MyBatisMapUtil.warp("group_id", platformId));
		HashMap<String, Object> map = (HashMap<String, Object>) MapUtil.warp(
				"draw", 1);

		int size = switchs.size();
		map.put("recordsTotal", size);
//		map.put("data", updateDate(switchs));
		try {
			map.put("data", new SwitchStatusUtil().wrap(switchs));
		} catch (RemoteException e) {
			logger.info("获取硬件系统开关信息失败");
			e.printStackTrace();
			return ResponseMessage.danger("获取硬件系统开关信息失败");
		}
		map.put("recordsFiltered", size);
		return map;
	}
	
	@RequestMapping("/online_order")
	@ResponseBody
	public Object getOnlineOrder(@RequestParam(required = false) String platformId) {
		
		int[] result = new int[20];
		List<HighVoltageSwitch> switchs = switchService
				.selectByParameters(MyBatisMapUtil.warp("group_id", platformId));
		
		for(int length = switchs.size(), i = 0, j = 0; i < length; ++i) {
			
			if(search(switchs.get(i).getId())) {
				result[j] = i + 1;
				++j;
			}
		}
		return result;
	}
	
	private List<HighVoltageSwitch> updateDate(List<HighVoltageSwitch> switchs) {
		
		String date = TimeUtil.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
		for(HighVoltageSwitch hvSwitch : switchs) {
			
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
		try {
			List<SwitchGPRS> list = hardwareService.getCtxInstance();
			for(SwitchGPRS gprs : list) {
				if(gprs.getId() != null && gprs.getId().equals(id)) {
					return true;
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
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
	@RequestMapping("/selectHVByPlatformIdInAsc")
	@ResponseBody
	public Object selectHVByLineIdInAsc(
			@RequestParam(required = true) String platformId, Model model) {

		List<HighVoltageSwitch> switchs = switchService
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
	@RequestMapping("/del_high_voltage_switch")
	@ResponseBody
	public ResponseMessage delSwitch(
			@RequestParam(required = true) String switchId,
			Model model, RedirectAttributes redirectAttributes) {

		HighVoltageSwitch delSwitch = switchService.selectByPrimaryKey(switchId);
		try {
			if(delSwitch != null) {
				// 删除这个开关
				switchService.deleteByPrimaryKey(switchId);
			}
		} catch (Exception e) {
			logger.error("删除高压开关失败");
			e.printStackTrace();
			return ResponseMessage.danger("删除高压开关失败");
		}
		return ResponseMessage.success(delSwitch != null ? delSwitch.getGroupId() + "" : "");
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
	public Object editSwitch(
			@Valid HighVoltageSwitch switch1,
			String platformId,
			Model model, 
			RedirectAttributes redirectAttributes) {

		try {
		switch1.setGroupId(Integer.parseInt(platformId));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.danger("小组id必须是数字！");
		}
		// @RequestParam(required = true)
		// 进不来
		if (null == switch1.getId() || switch1.getId().equals("")) {
			switch1.setId(UUIDUtil.getUUID());
		}
		
		try {
			switchService.updateByPrimaryKeySelective(switch1);
		} catch (Exception e) {
			logger.info("修改高压开关失败");
			e.printStackTrace();
			return ResponseMessage.danger("修改高压开关失败");
		}
		return ResponseMessage.success(switch1.getGroupId());
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
	@RequestMapping(value = "/downloadhvExcel")
	public ResponseEntity<byte[]> downloadlvExcel(HttpServletRequest request,
			HttpServletResponse respone, String clazzId) throws Exception {

		List<HighVoltageSwitch> sapis = switchService.selectByParameters(null);

		// 3.处理目标文件路径
		String fileName = "高压开关信息";
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
	@ResponseBody
	@RequestMapping(value = "/uploadhvSwitchExcel")
	public Object uploadlvSwitchExcel(@RequestParam("file") MultipartFile file,
			Model model, HttpServletRequest request, String platformId)
			throws Exception {

		MultipartFile[] files = { file };

		String realPath = request.getSession().getServletContext()
				.getRealPath("/uploadhvSwitchExcel");
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
	
	@RequestMapping("/close_switch")
	@ResponseBody
	public ResponseMessage closeSwitch(String id, String controlCode, HttpSession session) {
		User user = userService.getCurrentUser(session);
		if (user.getControlCode().equals(controlCode)) {
			
			return ResponseMessage.success("操作成功");
		}
		return ResponseMessage.warning("控制码输入错误");
	}

}
