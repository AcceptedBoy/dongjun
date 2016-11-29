package com.gdut.dongjun.web;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.ControlMearsureSwitch;
import com.gdut.dongjun.domain.po.DeviceGroup;
import com.gdut.dongjun.domain.po.DeviceGroupMapping;
import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.domain.po.LowVoltageSwitch;
import com.gdut.dongjun.domain.po.TemperatureDevice;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.service.ControlMearsureSwitchService;
import com.gdut.dongjun.service.DeviceGroupMappingService;
import com.gdut.dongjun.service.DeviceGroupService;
import com.gdut.dongjun.service.HighVoltageSwitchService;
import com.gdut.dongjun.service.LowVoltageSwitchService;
import com.gdut.dongjun.service.TemperatureDeviceService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.util.MyBatisMapUtil;

@Controller
@RequestMapping("/dongjun/device_group")
public class DeviceGroupController {
	
	@Autowired
	UserService userService;
	@Autowired
	DeviceGroupService deviceGroupService;
	@Autowired
	DeviceGroupMappingService deviceGroupMappingService;
	@Autowired
	private LowVoltageSwitchService lowService;
	@Autowired
	private HighVoltageSwitchService highService;
	@Autowired
	private ControlMearsureSwitchService controlService;
	@Autowired
	private TemperatureDeviceService temService;

	@RequestMapping("/edit")
	@ResponseBody
	public ResponseMessage addGroup(DeviceGroup dGroup, HttpSession session) {
		User user = userService.getCurrentUser(session);
		//TODO 通过当前用户获取PlatformId
		Integer result = deviceGroupService.updateByPrimaryKey(dGroup);
		if (result == 1) {
			return ResponseMessage.success("操作成功");
		}
		else
			return ResponseMessage.danger("操作失败");
	}
	
	@RequestMapping("/del")
	@ResponseBody
	public ResponseMessage delGroup(Integer id) {
		if (null == id) {
			return ResponseMessage.danger("操作失败");
		}
		if (deviceGroupService.deleteByPrimaryKey(id + "")) {
			return ResponseMessage.success("删除成功");
		}
		else {
			return ResponseMessage.danger("删除失败");
		}
	}
	
	@RequestMapping("/get_device_group")
	@ResponseBody
	public ResponseMessage getDeviceGroup(HttpSession session) {
		User user = userService.getCurrentUser(session);
		//TODO 超管返回所有组别
		List<DeviceGroup> list = deviceGroupService.selectByParameters(MyBatisMapUtil.warp(null));
//		List<DeviceGroup> list = deviceGroupService.selectByParameters(MyBatisMapUtil.warp("user_id", user.getId()));
		return ResponseMessage.success(list);
	}
	
	@RequestMapping("/edit_device")
	@ResponseBody
	public ResponseMessage addDevice(DeviceGroupMapping mapping) {
		//type 0低压 1高压 2管控 3温度
		if (deviceGroupMappingService.updateByPrimaryKey(mapping) == 0) {
			return ResponseMessage.danger("操作失败");
		}
		return ResponseMessage.success("操作成功");
	}
	
	@RequestMapping("/del_device")
	@ResponseBody
	public ResponseMessage delDevice(int id) {
		if (deviceGroupMappingService.deleteByPrimaryKey(id)) {
			return ResponseMessage.success("操作成功"); 
		}
		return ResponseMessage.danger("操作失败");
	}
	
	@RequestMapping("/get_device_by_device_group_id")
	@ResponseBody
	public ResponseMessage getDeviceByDeviceGroupId(int id) {
		List<DeviceGroupMapping> mappingList = deviceGroupMappingService.selectByParameters(MyBatisMapUtil.warp("device_group_id", id));
		List<Object> devices = new LinkedList<Object>();
		for (DeviceGroupMapping mapping : mappingList) {
			switch(mapping.getType()) {
			case 0:
				//低压
				LowVoltageSwitch low = lowService.selectByPrimaryKey(mapping.getDeviceId());
				devices.add(low);
				break;
			case 1:
				//高压
				HighVoltageSwitch high = highService.selectByPrimaryKey(mapping.getDeviceId());
				devices.add(high);
				break;
			case 2:
				//管控
				ControlMearsureSwitch control = controlService.selectByPrimaryKey(mapping.getDeviceId());
				devices.add(control);
				break;
			case 3:
				//温度
				TemperatureDevice device = temService.selectByPrimaryKey(mapping.getDeviceGroupId());
				devices.add(device);
				break;
			}
		}
		return ResponseMessage.success(devices);
	}
	
}
