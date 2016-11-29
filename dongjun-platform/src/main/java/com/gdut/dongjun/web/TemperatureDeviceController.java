package com.gdut.dongjun.web;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.gdut.dongjun.domain.po.TemperatureDevice;
import com.gdut.dongjun.service.TemperatureDeviceService;
import com.gdut.dongjun.service.rmi.HardwareService;
import com.gdut.dongjun.util.ClassLoaderUtil;
import com.gdut.dongjun.util.DownloadAndUploadUtil;
import com.gdut.dongjun.util.MapUtil;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.TimeUtil;
import com.gdut.dongjun.util.UUIDUtil;

@Controller
@RequestMapping("/dongjun")
public class TemperatureDeviceController {

	@Autowired
	private TemperatureDeviceService deviceService;

	@Autowired
	private HardwareService hardwareService;
	
	private static final Logger logger = Logger.getLogger(TemperatureDeviceController.class);

	
	@RequestMapping("/temperature")
	public String getTemperature(String platformId, Model model) {

		if (platformId != null) {
			model.addAttribute("devices",
					deviceService.selectByParameters(MyBatisMapUtil.warp("group_id", platformId)));
		} else {
			model.addAttribute("devices", deviceService.selectByParameters(null));
		}
		//TODO 通知前端需要返回的模板
		return "temperature";
	}
	
	/**
	 * 
	 * @param platformGroupId
	 * @param model
	 * @return
	 */
	@RequestMapping("/temperature_device_manager")
	public String getLineSwitchList(String platformId, Model model) {

		if (platformId != null) {
			model.addAttribute("devices",
					deviceService.selectByParameters(MyBatisMapUtil.warp("group_id", platformId)));
		} else {
			model.addAttribute("devices", deviceService.selectByParameters(null));
		}
		//TODO 通知前端需要返回的模板
		return "temperature_device_manager";
	}

	/**
	 * 根据platformGroupId搜索温度设备
	 * 
	 * @param platformGroupId
	 * @param model
	 * @return
	 */
	@RequestMapping("/temperature_device_list_by_platform_group_id")
	@ResponseBody
	public Object getDeviceByPlatforxmGroupId(String platformId) {
		List<TemperatureDevice> devices;
		
		if (platformId == null || platformId.equals("")) {
			return "";
		}
		else
			devices = deviceService
			.selectByParameters(MyBatisMapUtil.warp("group_id", platformId));
		
		HashMap<String, Object> map = (HashMap<String, Object>) MapUtil.warp("draw", 1);
		int size = devices.size();
		map.put("recordsTotal", size);
//		map.put("data", updateDate(devices));
		map.put("data", devices);
		map.put("recordsFiltered", size);
		return map;
	}

	private List<TemperatureDevice> updateDate(List<TemperatureDevice> devices) {

		String date = TimeUtil.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
		for (TemperatureDevice device : devices) {
			if (search(device.getId())) {
				device.setOnlineTime(date);
			}
		}
		return devices;
	}

	private boolean search(String id) {

		if (id == null) {
			return false;
		}
		try {
			List<SwitchGPRS> list = hardwareService.getCtxInstance();
			for (SwitchGPRS gprs : list) {
				if (gprs.getId() != null && gprs.getId().equals(id)) {
					return true;
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}

//	@RequestMapping("/online_order")
//	@ResponseBody
//	public Object getOnlineOrder(@RequestParam(required = false) String platformId) {
//
//		int[] result = new int[20];
//		List<TemperatureDevice> devices = deviceService.selectByParameters(MyBatisMapUtil.warp("group_id", platformId));
//
//		for (int length = devices.size(), i = 0, j = 0; i < length; ++i) {
//
//			if (search(devices.get(i).getId())) {
//				result[j] = i + 1;
//				++j;
//			}
//		}
//		return result;
//	}
	
	/**
	 * 删除温度设备
	 * @param deviceId
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("/del_temperature_device")
	@ResponseBody
	public ResponseMessage delDevice(
			@RequestParam(required = true) String deviceId,
			Model model, RedirectAttributes redirectAttributes) {

		TemperatureDevice delDevice = deviceService.selectByPrimaryKey(deviceId);
		try {
			if(delDevice != null) {
				// 删除这个开关
				deviceService.deleteByPrimaryKey(deviceId);
			}
		} catch (Exception e) {
			logger.error("删除温度路由器");
			e.printStackTrace();
			return ResponseMessage.danger("删除温度路由器");
		}
		return new ResponseMessage(ResponseMessage.Type.SUCCESS, delDevice != null ? "" + delDevice.getGroupId() : "", true);
	}
	
	/**
	 * 更新温度设备
	 * @param switch1
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("/edit_temperature_device")
	@ResponseBody
	public Object editDevice(
			@Valid TemperatureDevice device,
			String platformId,
			Model model, 
			RedirectAttributes redirectAttributes) {
		
		if (device.getId() == null || device.getId().equals("")) {
			device.setId(UUIDUtil.getUUID());
		}
		device.setGroupId(Integer.parseInt(platformId));
		try {
			deviceService.updateByPrimaryKeySelective(device);
		} catch (Exception e) {
			logger.error("修改温度路由器失败");
			e.printStackTrace();
			return ResponseMessage.danger("修改温度路由器失败");
		}
		return new ResponseMessage(ResponseMessage.Type.SUCCESS, device.getGroupId(), true);
	}

	@RequestMapping(value = "/downloadEmptytemExcel")
	public ResponseEntity<byte[]> downloadEmptytemExcel(
			HttpServletRequest request, 
			HttpServletResponse respone,
			String clazzId) throws Exception {

		// 3.处理目标文件路径
		String fileName = "温度设备信息";
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

		deviceService.createDeviceExcel(filePath, null);

		File targetFile = new File(filePath);
		return DownloadAndUploadUtil.download(request, targetFile, fileName);
	}
	

	@RequestMapping(value = "/downloadtemExcel")
	public ResponseEntity<byte[]> downloadtemExcel(HttpServletRequest request,
			HttpServletResponse respone, String clazzId) throws Exception {

		List<TemperatureDevice> sapis = deviceService.selectByParameters(null);

		// 3.处理目标文件路径
		String fileName = "温度设备信息";
		String relativePath = ClassLoaderUtil.getExtendResource("../",
				"spring-boot_mybatis_bootstrap").toString();
		String realPath = relativePath.replace("/", "\\");
		File file = new File(realPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		String filePath = realPath + "\\" + fileName;
		// 4.生成excel文件
		deviceService.createDeviceExcel(filePath, sapis);

		File targetFile = new File(filePath);
		return DownloadAndUploadUtil.download(request, targetFile, fileName);
	}
	
	@ResponseBody
	@RequestMapping(value = "/uploadtemDeviceExcel")
	public Object uploadtemDeviceExcel(
			@RequestParam("file") MultipartFile file,
			Model model, 
			HttpServletRequest request, 
			String platformId)
			throws Exception {

		MultipartFile[] files = { file };

		String realPath = request.getSession().getServletContext()
				.getRealPath("/uploadtemDeviceExcel");
		realPath = realPath.replace("/", "\\");

		// 1.保存文件到服务器
		String[] fileNames = DownloadAndUploadUtil.fileUpload(files, realPath);
		String f = realPath + "\\" + fileNames[0];

		// 2.解析excel并保存到数据库
		if (platformId == null || "".equals(platformId)) {
			return false;
		} else {
			deviceService.uploadDevice(f, platformId);
		}
		// 3.数据读取完后删除掉文件
		new File(f).delete();
		return ResponseMessage.success(platformId);
	}
}
