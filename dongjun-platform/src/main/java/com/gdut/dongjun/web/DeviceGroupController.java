package com.gdut.dongjun.web;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.ControlMearsureSwitch;
import com.gdut.dongjun.domain.po.DeviceGroup;
import com.gdut.dongjun.domain.po.DeviceGroupMapping;
import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.domain.po.LowVoltageSwitch;
import com.gdut.dongjun.domain.po.TemperatureDevice;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.dto.SwitchDTO;
import com.gdut.dongjun.service.DeviceGroupMappingService;
import com.gdut.dongjun.service.DeviceGroupService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.device.ControlMearsureSwitchService;
import com.gdut.dongjun.service.device.HighVoltageSwitchService;
import com.gdut.dongjun.service.device.LowVoltageSwitchService;
import com.gdut.dongjun.service.device.TemperatureDeviceService;
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

	@RequiresPermissions("device_group_admin:edit")
	@RequestMapping("/edit")
	@ResponseBody
	public ResponseMessage addGroup(DeviceGroup dGroup, HttpSession session) {
		User user = (User)session.getAttribute("currentUser");
		//TODO 通过当前用户获取PlatformId
		Integer result = deviceGroupService.updateByPrimaryKey(dGroup);
		if (result == 1) {
			return ResponseMessage.success("操作成功");
		}
		else
			return ResponseMessage.danger("操作失败");
	}
	
	@RequiresPermissions("device_group_admin:delete")
	@RequestMapping("/del")
	@ResponseBody
	//TODO 删除相关Mapping
	public ResponseMessage delGroup(Integer id) {
		if (null == id) {
			return ResponseMessage.danger("操作失败");
		}
		if (deviceGroupService.deleteByPrimaryKey(id + "")) {
			List<DeviceGroupMapping> mappings = deviceGroupMappingService.selectByParameters(MyBatisMapUtil.warp("device_group_id", id));
			for (DeviceGroupMapping m : mappings) {
				deviceGroupMappingService.deleteByPrimaryKey(m.getId());
			}
			return ResponseMessage.success("删除成功");
		}
		else {
			return ResponseMessage.danger("删除失败");
		}
	}
	
	@RequiresAuthentication
	@RequestMapping("/get_device_group")
	@ResponseBody
	public ResponseMessage getDeviceGroup(HttpSession session) {
		User user = (User)session.getAttribute("currentUser");
		//TODO 超管返回所有组别
		List<DeviceGroup> list = deviceGroupService.selectByParameters(MyBatisMapUtil.warp(null));
//		List<DeviceGroup> list = deviceGroupService.selectByParameters(MyBatisMapUtil.warp("user_id", user.getId()));
		return ResponseMessage.success(list);
	}
	
	/**
	 * 新增小组联系
	 * @param deviceId
	 * @param type
	 * @param deviceGroupId
	 * @return
	 */
	@RequiresPermissions("device_group_admin:edit")
	@RequestMapping(value = "/edit_device", method = RequestMethod.POST)
	@ResponseBody
	public ResponseMessage addDevice(@RequestParam(value="deviceId") String deviceId, 
			@RequestParam(value="type") String type, @RequestParam(value="deviceGroupId") String deviceGroupId) {
		//type 0低压 1高压 2管控 3温度
		String[] splitId = deviceId.split(",=");
		String[] types = type.split(",=");
		DeviceGroupMapping mapping = new DeviceGroupMapping();
		mapping.setDeviceGroupId(deviceGroupId);
		for (int i = 0; i < splitId.length; i++) {
			mapping.setType(Integer.parseInt(types[i]));
			mapping.setDeviceId(splitId[i]);
			if (deviceGroupMappingService.updateByPrimaryKey(mapping) == 0) {
				return ResponseMessage.danger("操作失败");
			}
		}
		return ResponseMessage.success("操作成功");
	}
	
	@RequiresPermissions("device_group_admin:delete")
	@RequestMapping("/del_device")
	@ResponseBody
	public ResponseMessage delDevice(int id) {
		if (deviceGroupMappingService.deleteByPrimaryKey(id + "")) {
			return ResponseMessage.success("操作成功"); 
		}
		return ResponseMessage.danger("操作失败");
	}
	
	@RequiresAuthentication
	@RequestMapping("/get_device_by_device_group_id")
	@ResponseBody
	public ResponseMessage getDeviceByDeviceGroupId(int groupId) {
		try {
		List<DeviceGroupMapping> mappingList = deviceGroupMappingService.selectByParameters(MyBatisMapUtil.warp("device_group_id", groupId));
		List<Object> devices = new LinkedList<Object>();
		for (DeviceGroupMapping mapping : mappingList) {
			switch(mapping.getType()) {
			case 0:
				//低压
				LowVoltageSwitch low = lowService.selectByPrimaryKey(mapping.getDeviceId());
				devices.add(SwitchDTO.wrap(low, 0));
				break;
			case 1:
				//高压
				HighVoltageSwitch high = highService.selectByPrimaryKey(mapping.getDeviceId());
				devices.add(SwitchDTO.wrap(high, 1));
				break;
			case 2:
				//管控
				ControlMearsureSwitch control = controlService.selectByPrimaryKey(mapping.getDeviceId());
				devices.add(SwitchDTO.wrap(control, 2));
				break;
			case 3:
				//温度
				TemperatureDevice device = temService.selectByPrimaryKey(mapping.getDeviceId());
				devices.add(SwitchDTO.wrap(device, 3));
				break;
			}
		}
		return ResponseMessage.success(devices);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * TODO，根据用户所在公司确认返回的设备
	 * @return
	 */
	@RequiresAuthentication
	@RequestMapping("/get_all_device")
	@ResponseBody
	public Object allDevice() {
		List<Object> devices = new LinkedList<Object>();
		List<LowVoltageSwitch> lows;
		List<HighVoltageSwitch> highs;
		List<TemperatureDevice> tems;
		
		List<SwitchDTO> low_dto = new ArrayList<SwitchDTO>();
		List<SwitchDTO> high_dto = new ArrayList<SwitchDTO>();
		List<SwitchDTO> tem_dto = new ArrayList<SwitchDTO>();
		
		highs = highService.selectByParameters(null);
		tems = temService.selectByParameters(null);
		lows = lowService.selectByParameters(null);

		for (LowVoltageSwitch s : lows) {
			low_dto.add(SwitchDTO.wrap(s, 0));
		}
		for (HighVoltageSwitch s : highs) {
			high_dto.add(SwitchDTO.wrap(s, 1));
		}
		for (TemperatureDevice s : tems) {
			tem_dto.add(SwitchDTO.wrap(s, 3));
		}
		
		devices.addAll(low_dto);
		devices.addAll(high_dto);
		devices.addAll(tem_dto);
		return ResponseMessage.success(devices);
	}
	
}
