package com.gdut.dongjun.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.DataMonitor;
import com.gdut.dongjun.service.DeviceGroupMappingService;
import com.gdut.dongjun.service.UserDeviceMappingService;
import com.gdut.dongjun.service.device.DataMonitorService;
import com.gdut.dongjun.service.device.DataMonitorSubmoduleService;
import com.gdut.dongjun.service.device.TemperatureModuleService;
import com.gdut.dongjun.service.device.TemperatureSensorService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

@Controller
@RequestMapping("/dongjun/data_monitor")
public class DataMonitorController {
	
	@Autowired
	private DataMonitorService monitorService;
	@Autowired
	private TemperatureModuleService moduleService;
	@Autowired
	private DataMonitorSubmoduleService submoduleService;
	@Autowired
	private TemperatureSensorService sensorSerivce;
	@Autowired
	private DeviceGroupMappingService deviceGroupMappingService;
	@Autowired
	private UserDeviceMappingService userDeviceMappingService;
	

	@ResponseBody
	@RequestMapping("/edit")
	public ResponseMessage edit(DataMonitor monitor) {
		monitor.setAvailable(1);
		if (null == monitor.getId() || "".equals(monitor.getId())) {
			monitor.setId(UUIDUtil.getUUID());
		}
		if (0 == monitorService.updateByPrimaryKeySelective(monitor)) {
			return ResponseMessage.warning("操作失败");
		}
		return ResponseMessage.success("操作成功");
	}
	
	//TODO 删除monitor要删除什么，至少要删除devicegroupmapping，userdevicemapping,删除所有子模块，删除监控数据
	@ResponseBody
	@RequestMapping("/del")
	public ResponseMessage del(String id) {
		//删除DataMonitor
		if (!monitorService.deleteByPrimaryKey(id)) {
			return ResponseMessage.warning("操作失败");
		}
		//删除DeviceGroupMapping
		if (0 == deviceGroupMappingService.deleteByParameters(MyBatisMapUtil.warp("device_id", id))) {
			return ResponseMessage.warning("操作失败");
		}
		//删除UserDeviceMapping
		if (0 == userDeviceMappingService.deleteByParameters(MyBatisMapUtil.warp("device_id", id))) {
			return ResponseMessage.warning("操作失败");
		}
		//删除DataMonitorSubmodule
		if (0 == submoduleService.deleteByParameters(MyBatisMapUtil.warp("data_monitor_id", id))) {
			return ResponseMessage.warning("操作失败");
		}
		return ResponseMessage.success("操作成功");
	}
	
	/**
	 * 根据platformId搜索
	 * @param platformId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/list")
	public Object list(String platformGroupId) {
		List<DataMonitor> list = monitorService.selectByParameters(MyBatisMapUtil.warp("group_id", platformGroupId));
//		HashMap<String, Object> map = (HashMap<String, Object>) MapUtil.warp("draw", 1);
//		int size = list.size();
//		map.put("recordsTotal", size);
//		map.put("data", list);
//		map.put("recordsFiltered", size);
//		return map;
		return ResponseMessage.success(list);
	}
	
//	@RequiresAuthentication
//	@RequestMapping(value = "/downloadEmptytemExcel")
//	public ResponseEntity<byte[]> downloadEmptytemExcel(
//			HttpServletRequest request, 
//			HttpServletResponse respone,
//			String clazzId) throws Exception {
//
//		// 3.处理目标文件路径
//		String fileName = "温度设备信息";
//		String relativePath = ClassLoaderUtil.getExtendResource("../",
//				"spring-boot_mybatis_bootstrap").toString();
//
//		if ("".equals(relativePath)) {
//
//			return null;
//		}
//
//		String realPath = relativePath.replace("/", "\\");
//		File file = new File(realPath);
//		if (!file.exists()) {
//			file.mkdirs();
//		}
//		String filePath = realPath + "\\" + fileName;
//		// 4.生成excel文件
//
//		deviceService.createDeviceExcel(filePath, null);
//
//		File targetFile = new File(filePath);
//		return DownloadAndUploadUtil.download(request, targetFile, fileName);
//	}
//	
//	@RequiresAuthentication
//	@RequestMapping(value = "/downloadtemExcel")
//	public ResponseEntity<byte[]> downloadtemExcel(HttpServletRequest request,
//			HttpServletResponse respone, String clazzId) throws Exception {
//
//		List<TemperatureDevice> sapis = deviceService.selectByParameters(null);
//
//		// 3.处理目标文件路径
//		String fileName = "温度设备信息";
//		String relativePath = ClassLoaderUtil.getExtendResource("../",
//				"spring-boot_mybatis_bootstrap").toString();
//		String realPath = relativePath.replace("/", "\\");
//		File file = new File(realPath);
//		if (!file.exists()) {
//			file.mkdirs();
//		}
//		String filePath = realPath + "\\" + fileName;
//		// 4.生成excel文件
//		deviceService.createDeviceExcel(filePath, sapis);
//
//		File targetFile = new File(filePath);
//		return DownloadAndUploadUtil.download(request, targetFile, fileName);
//	}
//	
//	@RequiresPermissions("platform_group_admin:device")
//	@ResponseBody
//	@RequestMapping(value = "/uploadtemDeviceExcel")
//	public Object uploadtemDeviceExcel(
//			@RequestParam("file") MultipartFile file,
//			Model model, 
//			HttpServletRequest request, 
//			String platformId)
//			throws Exception {
//
//		MultipartFile[] files = { file };
//
//		String realPath = request.getSession().getServletContext()
//				.getRealPath("/uploadtemDeviceExcel");
//		realPath = realPath.replace("/", "\\");
//
//		// 1.保存文件到服务器
//		String[] fileNames = DownloadAndUploadUtil.fileUpload(files, realPath);
//		String f = realPath + "\\" + fileNames[0];
//
//		// 2.解析excel并保存到数据库
//		if (platformId == null || "".equals(platformId)) {
//			return false;
//		} else {
//			deviceService.uploadDevice(f, platformId);
//		}
//		// 3.数据读取完后删除掉文件
//		new File(f).delete();
//		return ResponseMessage.success(platformId);
//	}
	
//	@RequiresAuthentication
//	@RequestMapping("/temperature_hitch/list")
//	@ResponseBody
//	public ResponseMessage getAllHitchEvent(HttpSession session) {
//		User user = userService.getCurrentUser(session);
//		List<TemperatureMeasureHitchEvent> events = eventService.selectByParameters(MyBatisMapUtil.warp("group_id", user.getCompanyId()));
//		List<TemperatureMeasureHitchEventDTO> dtoes = new ArrayList<TemperatureMeasureHitchEventDTO>();
//		for (TemperatureMeasureHitchEvent e : events) {
//			dtoes.add(wrapIntoDTO(e));
//		}
//		return ResponseMessage.success(dtoes);
//	}
//	
//	@RequiresAuthentication
//	@RequestMapping("/temperature_hitch/del")
//	@ResponseBody
//	public ResponseMessage ignodeEvent(String id) {
//		if (!eventService.deleteByPrimaryKey(id)) {
//			return ResponseMessage.warning("操作失败");
//		}
//		return ResponseMessage.success("操作成功");
//	}
//	
//	public TemperatureMeasureHitchEventDTO wrapIntoDTO(TemperatureMeasureHitchEvent event) {
//		TemperatureMeasureHitchEventDTO dto = new TemperatureMeasureHitchEventDTO();
//		dto.setId(event.getId());
//		dto.setHitchReason(event.getHitchReason());
//		dto.setHitchTime(event.getHitchTime());
//		dto.setName(deviceService.selectNameById(event.getSwitchId()));
//		dto.setTag(event.getTag());
//		dto.setValue(event.getValue().toString());
//		if (null != event.getMaxHitchValue()) {
//			dto.setMaxHitchValue(event.getMaxHitchValue().toString());
//		}
//		if (null != event.getMinHitchValue()) {
//			dto.setMinHitchValue(event.getMinHitchValue().toString());
//		}
//		return dto;
//	}
	
}
