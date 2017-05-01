package com.gdut.dongjun.web;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.DataMonitor;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.domain.po.UserDeviceMapping;
import com.gdut.dongjun.service.DeviceGroupMappingService;
import com.gdut.dongjun.service.PlatformGroupService;
import com.gdut.dongjun.service.UserDeviceMappingService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.authc.RoleService;
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
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private PlatformGroupService pgService;

	@ResponseBody
	@RequestMapping("/edit")
	public ResponseMessage edit(DataMonitor monitor, HttpSession session) {
		monitor.setAvailable(1);
		if (null == monitor.getId() || "".equals(monitor.getId())) {
			monitor.setId(UUIDUtil.getUUID());
			Subject currentUser = SecurityUtils.getSubject();
			if (!currentUser.hasRole("platform_admin")) {
				//更新UserDeviceMapping
				User user = userService.getCurrentUser(session);
				UserDeviceMapping m = new UserDeviceMapping();
				m.setId(UUIDUtil.getUUID());
				m.setDeviceId(monitor.getId());
				m.setUserId(user.getId());
				userDeviceMappingService.updateByPrimaryKey(m);
			}
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
	public Object list(String platformGroupId, HttpSession session) {
		User user = userService.getCurrentUser(session);
		Subject subject = SecurityUtils.getSubject();
		if (subject.hasRole("platform_admin")) {
			return ResponseMessage.success(
					monitorService.selectByParameters(MyBatisMapUtil.warp("group_id", platformGroupId))
					);
		} else {
			return ResponseMessage.success(monitorService.selectByUserMapping(user));
		}
//		User user = userService.getCurrentUser(session);
//		List<DataMonitor> list = null;
//		List<Role> roles = roleService.selectByUserId(user.getId());
//		List<String> roleList = new ArrayList<String>();
//		for (Role r : roles) {
//			roleList.add(r.getRole());
//		}
//		if (roleList.contains("super_admin")) {
//			list = monitorService.selectByParameters(MyBatisMapUtil.warp("group_id", platformGroupId));
//		}
//		else {
//			list = monitorService.selectByParameters(MyBatisMapUtil.warp("group_id", platformGroupId));
//			List<String> ids = userDeviceMappingService.selectMonitorIdByUserId(user.getId());
//			for (DataMonitor m : list) {
//				if (!ids.contains(m.getId())) {
//					list.remove(m);
//				}
//			}
//		}
//		return ResponseMessage.success(list);
	}
	
//	/**
//	 * 用户能看什么就返回什么
//	 * @param session
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping("/list_authc")
//	public ResponseMessage listByAuth(HttpSession session) {
//		User user = userService.getCurrentUser(session);
//		return ResponseMessage.success(monitorService.selectByUserMapping(user));
//	}
	
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
