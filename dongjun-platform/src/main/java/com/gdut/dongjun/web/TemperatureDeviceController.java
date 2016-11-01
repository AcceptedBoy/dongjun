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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.domain.po.TemperatureDevice;
import com.gdut.dongjun.service.TemperatureDeviceService;
import com.gdut.dongjun.service.rmi.HardwareService;
import com.gdut.dongjun.util.ClassLoaderUtil;
import com.gdut.dongjun.util.DownloadAndUploadUtil;
import com.gdut.dongjun.util.MapUtil;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.TimeUtil;
import com.gdut.dongjun.util.UUIDUtil;

public class TemperatureDeviceController {

	@Autowired
	private TemperatureDeviceService deviceService;

	@Autowired
	private HardwareService hardwareService;
	
	private static final Logger logger = Logger.getLogger(TemperatureDeviceController.class);

	/**
	 * 
	 * @param platformGroupId
	 * @param model
	 * @return
	 */
	@RequestMapping("/temperature_device_manager")
	public String getLineSwitchList(String platformGroupId, Model model) {

		if (platformGroupId != null) {
			model.addAttribute("devices",
					deviceService.selectByParameters(MyBatisMapUtil.warp("group_id", platformGroupId)));
		} else {
			model.addAttribute("devices", deviceService.selectByParameters(null));
		}
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
	public Object getLineSwitchListByLineId(@RequestParam(required = true) String platformGroupId, Model model) {

		List<TemperatureDevice> switchs = deviceService
				.selectByParameters(MyBatisMapUtil.warp("group_id", platformGroupId));
		HashMap<String, Object> map = (HashMap<String, Object>) MapUtil.warp("draw", 1);
		int size = switchs.size();
		map.put("recordsTotal", size);
		map.put("data", updateDate(switchs));
		map.put("data", switchs);
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

	@RequestMapping("/online_order")
	@ResponseBody
	public Object getOnlineOrder(@RequestParam(required = false) String platformId) {

		int[] result = new int[20];
		List<TemperatureDevice> devices = deviceService.selectByParameters(MyBatisMapUtil.warp("group_id", platformId));

		for (int length = devices.size(), i = 0, j = 0; i < length; ++i) {

			if (search(devices.get(i).getId())) {
				result[j] = i + 1;
				++j;
			}
		}
		return result;
	}
	
	/**
	 * 删除温度设备
	 * @param deviceId
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("/del_temperature_device")
	@ResponseBody
	public String delDevice(
			@RequestParam(required = true) String deviceId,
			Model model, RedirectAttributes redirectAttributes) {

		TemperatureDevice delDevice = deviceService.selectByPrimaryKey(deviceId);
		try {
			if(delDevice != null) {
				// 删除这个开关
				deviceService.deleteByPrimaryKey(deviceId);
			}
		} catch (Exception e) {
			logger.error("删除开关失败！");
			return null;
		}
		return delDevice != null ? "" + delDevice.getGroupId() : "";
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
			Model model, 
			RedirectAttributes redirectAttributes) {

		// @RequestParam(required = true)
		// 进不来
		if (device.getId().equals("")) {
			device.setId(UUIDUtil.getUUID());
		}
		
		try {
			
			deviceService.updateByPrimaryKey(device);
		} catch (Exception e) {
			
			logger.error("修改开关失败！");
			return null;
		}
		return device.getGroupId();
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
		//TODO
		deviceService.createDeviceExcel(filePath, null);

		File targetFile = new File(filePath);
		return DownloadAndUploadUtil.download(request, targetFile, fileName);
	}
	
	@RequestMapping(value = "/uploadtemSwitchExcel")
	public Object uploadlvSwitchExcel(
			@RequestParam("file") MultipartFile file,
			Model model, 
			HttpServletRequest request, 
			String platformGroupId)
			throws Exception {

		MultipartFile[] files = { file };

		String realPath = request.getSession().getServletContext()
				.getRealPath("/uploadhvSwitchExcel");
		realPath = realPath.replace("/", "\\");

		// 1.保存文件到服务器
		String[] fileNames = DownloadAndUploadUtil.fileUpload(files, realPath);
		String f = realPath + "\\" + fileNames[0];

		// 2.解析excel并保存到数据库
		if (platformGroupId == null || "".equals(platformGroupId)) {

			return false;
		} else {
			//TODO
			deviceService.uploadSwitch(f, platformGroupId);
		}
		// 3.数据读取完后删除掉文件
		new File(f).delete();
		return "redirect:high_voltage_switch_manager";
	}
}
