package com.gdut.dongjun.web;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import com.gdut.dongjun.domain.po.DataMonitor;
import com.gdut.dongjun.domain.po.DeviceGroup;
import com.gdut.dongjun.domain.po.DeviceGroupMapping;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.service.DeviceGroupMappingService;
import com.gdut.dongjun.service.DeviceGroupService;
import com.gdut.dongjun.service.PlatformGroupService;
import com.gdut.dongjun.service.UserDeviceMappingService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.device.DataMonitorService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

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
	private PlatformGroupService pgService;
	@Autowired
	private DataMonitorService monitorService;
	@Autowired
	private UserDeviceMappingService userMappingService;

	@RequiresPermissions("device_group_admin:edit")
	@RequestMapping("/edit")
	@ResponseBody
	public ResponseMessage addGroup(DeviceGroup dGroup, HttpSession session) {
		User user = (User) session.getAttribute("currentUser");
		dGroup.setPlatformGroupId(user.getCompanyId());	//实际上是PlatformGroup的id
		if (null == dGroup.getId() || "".equals(dGroup.getId())) {
			dGroup.setId(UUIDUtil.getUUID());
		}
		Integer result = deviceGroupService.updateByPrimaryKeySelective(dGroup);
		if (result == 1) {
			return ResponseMessage.success("操作成功");
		} else
			return ResponseMessage.warning("操作失败");
	}

	@RequiresPermissions("device_group_admin:delete")
	@RequestMapping("/del")
	@ResponseBody
	public ResponseMessage delGroup(Integer id) {
		if (null == id) {
			return ResponseMessage.danger("操作失败");
		}
		if (deviceGroupService.deleteByPrimaryKey(id + "")) {
			List<DeviceGroupMapping> mappings = deviceGroupMappingService
					.selectByParameters(MyBatisMapUtil.warp("device_group_id", id));
			for (DeviceGroupMapping m : mappings) {
				deviceGroupMappingService.deleteByPrimaryKey(m.getId());
			}
			return ResponseMessage.success("删除成功");
		} else {
			return ResponseMessage.danger("删除失败");
		}
	}

	@RequiresAuthentication
	@RequestMapping("/get_device_group")
	@ResponseBody
	public ResponseMessage getDeviceGroup(HttpSession session) {
		User user = (User) session.getAttribute("currentUser");
		 List<DeviceGroup> list = deviceGroupService.selectByParameters(MyBatisMapUtil.warp("platform_group_id",
		 user.getCompanyId()));
		return ResponseMessage.success(list);
	}

	/**
	 * 新增小组联系，修改？不存在的
	 * 
	 * @param deviceId
	 * @param type
	 * @param deviceGroupId
	 * @return
	 */
	@RequiresPermissions("device_group_admin:edit")
	@RequestMapping(value = "/edit_device", method = RequestMethod.POST)
	@ResponseBody
	public ResponseMessage addDevice(@RequestParam(value = "deviceId") String deviceId,
			@RequestParam(value = "deviceGroupId") String deviceGroupId) {
		// type 0低压 1高压 2管控 3温度
		String[] splitId = deviceId.split(",=");
		DeviceGroupMapping mapping = new DeviceGroupMapping();
		mapping.setDeviceGroupId(deviceGroupId);
		for (int i = 0; i < splitId.length; i++) {
			//保留字段type，默认0
			mapping.setType(0);
			mapping.setDeviceId(splitId[i]);
			mapping.setId(UUIDUtil.getUUID());
			if (deviceGroupMappingService.updateByPrimaryKey(mapping) == 0) {
				return ResponseMessage.danger("操作失败");
			}
		}
		return ResponseMessage.success("操作成功");
	}

	@RequiresPermissions("device_group_admin:delete")
	@RequestMapping("/del_device")
	@ResponseBody
	public ResponseMessage delDevice(String id, String deviceGroupId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_group_id", deviceGroupId);
		map.put("device_id", id);
		List<DeviceGroupMapping> list = deviceGroupMappingService.selectByParameters(MyBatisMapUtil.warp(map));
		if (null == list || 1 != list.size()) {
			return ResponseMessage.danger("操作失败"); 
		}
		if (deviceGroupMappingService.deleteByPrimaryKey(list.get(0).getId())) {
			return ResponseMessage.success("操作成功");
		}
		return ResponseMessage.danger("操作失败");
	}

	/**
	 * 根据DeviceGroup的id返回DataMonitor
	 * TODO
	 * @param groupId
	 * @return
	 */
	@RequiresAuthentication
	@RequestMapping("/get_device_by_device_group_id")
	@ResponseBody
	public ResponseMessage getDeviceByDeviceGroupId(String groupId, HttpSession session) {
		List<DeviceGroupMapping> mappingList = deviceGroupMappingService
				.selectByParameters(MyBatisMapUtil.warp("device_group_id", groupId));
		List<DataMonitor> devices = new LinkedList<DataMonitor>();
		List<String> ids = userMappingService.selectMonitorIdByUserId(userService.getCurrentUser(session).getId());
		for (DeviceGroupMapping mapping : mappingList) {
			if (ids.contains(mapping.getDeviceId())) {
				devices.add(monitorService.selectByPrimaryKey(mapping.getDeviceId()));
			}
		}
		return ResponseMessage.success(devices);
	}

	/**
	 * TODO，根据用户所在公司确认返回的设备
	 * 
	 * @return
	 */
	@RequiresAuthentication
	@RequestMapping("/get_all_device")
	@ResponseBody
	public ResponseMessage allDevice(HttpSession session) {
		User user = userService.getCurrentUser(session);
		List<DataMonitor> devices = monitorService
				.selectByParameters(MyBatisMapUtil.warp("group_id", user.getCompanyId()));
		return ResponseMessage.success(devices);
	}

}