package com.gdut.dongjun.web;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.DeviceGroup;
import com.gdut.dongjun.domain.po.DeviceGroupMapping;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.service.DeviceGroupMappingService;
import com.gdut.dongjun.service.DeviceGroupService;
import com.gdut.dongjun.service.UserDeviceMappingService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.device.ElectronicModuleService;
import com.gdut.dongjun.service.device.TemperatureModuleService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;
import com.gdut.dongjun.web.vo.DeviceGroupMappingVO;

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
	private UserDeviceMappingService userMappingService;
	@Autowired
	private ElectronicModuleService elecService;
	@Autowired
	private TemperatureModuleService temService;

	@RequiresAuthentication
	@RequestMapping("/edit")
	@ResponseBody
	public ResponseMessage addGroup(DeviceGroup dGroup, HttpSession session) {
		User user = (User) session.getAttribute("currentUser");
		
		if (null == dGroup.getId() || "".equals(dGroup.getId())) {
			dGroup.setId(UUIDUtil.getUUID());
		}
		Integer result = deviceGroupService.updateByPrimaryKeySelective(dGroup);
		if (result == 1) {
			return ResponseMessage.success("操作成功");
		} else
			return ResponseMessage.warning("操作失败");
	}

	// @RequiresPermissions("device_group_admin:delete")
	@RequiresAuthentication
	@RequestMapping("/del")
	@ResponseBody
	public ResponseMessage delGroup(String id) {
		if (null == id) {
			return ResponseMessage.danger("操作失败");
		}
		List<DeviceGroupMapping> mappings = deviceGroupMappingService
				.selectByParameters(MyBatisMapUtil.warp("device_group_id", id));
		if (null == mappings || mappings.size() == 0) {
			if (deviceGroupService.deleteByPrimaryKey(id)) {
				return ResponseMessage.success("删除成功");
			} else {
				return ResponseMessage.danger("删除失败");
			}
		}
		return ResponseMessage.warning("请检查该组别下是否还有设备没有删除"); 
	}
	
	/**
	 * 根据companyId返回对应公司所有设备组别
	 * @param companyId
	 * @return
	 */
	@RequiresAuthentication
	@RequestMapping("/list")
	@ResponseBody
	public ResponseMessage getDeviceGroup(String companyId, HttpSession session) {
		if (null == companyId) {
			companyId = userService.getCurrentUser(session).getCompanyId();
		}
		List<DeviceGroup> list = deviceGroupService
				.selectByParameters(MyBatisMapUtil.warp("company_id", companyId));
		return ResponseMessage.success(list);
	}

	/**
	 * 新增小组联系
	 * 
	 * @param deviceId
	 * @param type
	 * @param deviceGroupId
	 * @return
	 */
//	@RequiresAuthentication
//	@RequestMapping(value = "/edit_device", method = RequestMethod.POST)
//	@ResponseBody
//	public ResponseMessage addDevice(
//			@RequestParam(value = "deviceId") String deviceId,
//			@RequestParam(value = "deviceGroupId") String deviceGroupId) {
//		// type 0低压 1高压 2管控 3温度
//		String[] splitId = deviceId.split(",=");
//		DeviceGroupMapping mapping = new DeviceGroupMapping();
//		mapping.setDeviceGroupId(deviceGroupId);
//		List<DeviceGroupMapping> list = null;
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("device_group_id", deviceGroupId);
//		for (int i = 0; i < splitId.length; i++) {
//			map.put("device_id", splitId[i]);
//			list = deviceGroupMappingService.selectByParameters(MyBatisMapUtil.warp(map));
//			if (null != list && 0 != list.size()) {
//				continue;
//			}
//			// 保留字段type，默认0
//			mapping.setType(0);
//			mapping.setModuleId(splitId[i]);
//			mapping.setId(UUIDUtil.getUUID());
//			if (deviceGroupMappingService.updateByPrimaryKey(mapping) == 0) {
//				return ResponseMessage.danger("操作失败");
//			}
//		}
//		return ResponseMessage.success("操作成功");
//	}

	// @RequiresPermissions("device_group_admin:delete")
	@RequiresAuthentication
	@RequestMapping("/del_device")
	@ResponseBody
	public ResponseMessage delDevice(
			@RequestParam(value = "deviceGroupMappingId") String id) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		List<DeviceGroupMapping> list = 
//				deviceGroupMappingService.selectByParameters(MyBatisMapUtil.warp(map));
//		if (null == list || 1 != list.size()) {
//			return ResponseMessage.danger("操作失败");
//		}
//		if (deviceGroupMappingService.deleteByPrimaryKey(list.get(0).getId())) {
//			return ResponseMessage.success("操作成功");
//		}
		DeviceGroupMapping m = deviceGroupMappingService.selectByPrimaryKey(id);
		switch (m.getType()) {
		//	低压 0
		//	高压 1
		case 1 : elecService.deleteWithTag(m.getModuleId()); break;
		//	管控 2
		//	温度 3
		case 3 : temService.deleteWithTag(m.getModuleId()); break;
		default : break;
		}
		if (deviceGroupMappingService.deleteByPrimaryKey(id)) {
			return ResponseMessage.success("操作成功");
		}
		return ResponseMessage.danger("操作失败");
	}

	/**
	 * 根据DeviceGroup的id返回旗下的设备
	 * 
	 * @param groupId
	 * @return
	 */
	@RequiresAuthentication
//	@RequestMapping("/get_device_by_device_group_id")
	@RequestMapping("/list_device")
	@ResponseBody
	public ResponseMessage getDeviceByDeviceGroupId(
			@RequestParam(value="id") String groupId, HttpSession session) {
		List<DeviceGroupMapping> mappingList = deviceGroupMappingService
				.selectByParameters(MyBatisMapUtil.warp("device_group_id", groupId));
		List<DeviceGroupMappingVO> voList = new ArrayList<>();
		for (DeviceGroupMapping mp : mappingList) {
			DeviceGroupMappingVO vo = new DeviceGroupMappingVO();
			vo.setId(mp.getId());
			vo.setType(mp.getType());
			vo.setDeviceGroupId(mp.getDeviceGroupId());
			vo.setDeviceId(mp.getModuleId());
			switch (mp.getType()) {
			//	低压
			case 0 :  break;
			//	高压
			case	1 : vo.setName(elecService.selectByPrimaryKey(mp.getModuleId()).getName()); break;
			//	管控
			case 2 : break;
			//	温度
			case 3 : vo.setName(temService.selectByPrimaryKey(mp.getModuleId()).getName()); break;
			default : break;
			}
			voList.add(vo);
		}
		return ResponseMessage.success(voList);
	}

//	/**
//	 * TODO，根据用户所在公司确认返回的设备
//	 * 
//	 * @return
//	 */
//	@RequiresAuthentication
//	@RequestMapping("/list_device_auth")
//	@ResponseBody
//	public ResponseMessage allDevice(HttpSession session) {
//		
////		User user = userService.getCurrentUser(session);
////		List<DataMonitor> devices = monitorService
////				.selectByParameters(MyBatisMapUtil.warp("group_id", user.getCompanyId()));
////		Subject subject = SecurityUtils.getSubject();
////		if (subject.hasRole("platform_group_admin")) {
////			return ResponseMessage.success(devices);
////		}
////		List<String> ids = userMappingService.selectMonitorIdByUserId(userService.getCurrentUser(session).getId());
////		List<DataMonitor> dtos = new ArrayList<DataMonitor>();
////		for (DataMonitor d : devices) {
////			if (ids.contains(d.getId())) {
////				dtos.add(d);
////			}
////		}
//		return ResponseMessage.success(dtos);
//	}

}