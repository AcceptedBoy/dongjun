package com.gdut.dongjun.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.DataMonitor;
import com.gdut.dongjun.domain.po.UserDeviceMapping;
import com.gdut.dongjun.domain.vo.UserDeviceMappingDTO;
import com.gdut.dongjun.service.UserDeviceMappingService;
import com.gdut.dongjun.service.device.DataMonitorService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

@Controller
@RequestMapping("/dongjun/user_device_mapping")
public class UserDeviceMappingController {

	@Autowired
	private UserDeviceMappingService mappingService;
	@Autowired
	private DataMonitorService monitorService;

	@ResponseBody
	@RequestMapping("/edit")
	public ResponseMessage edit(String deviceId, String deviceType, String userId, String type) {
		String[] ids = deviceId.split(",=");
		for (int i = 0; i < ids.length; i++) {
			UserDeviceMapping mapping = new UserDeviceMapping();
			mapping.setId(UUIDUtil.getUUID());
			mapping.setDeviceId(ids[i]);
			mapping.setUserId(userId);
			if (0 == mappingService.updateByPrimaryKey(mapping)) {
				return ResponseMessage.warning("操作失败");
			}
		}
		return ResponseMessage.success("操作成功");
	}

	@ResponseBody
	@RequestMapping("/del")
	public ResponseMessage del(String id) {
		if (!mappingService.deleteByPrimaryKey(id)) {
			return ResponseMessage.warning("操作失败");
		}
		return ResponseMessage.success("操作成功");
	}

	/**
	 * 返回用户对应的DataMonitor实体类
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/get_user_mapping")
	public ResponseMessage getUserMapping(String id) {
		List<UserDeviceMapping> list = mappingService.selectByParameters(MyBatisMapUtil.warp("user_id", id));
		return ResponseMessage.success(wrapIntoDTO(list));
	}

	private List<UserDeviceMappingDTO> wrapIntoDTO(List<UserDeviceMapping> mappings) {
		List<UserDeviceMappingDTO> dtos = new ArrayList<UserDeviceMappingDTO>();
		for (UserDeviceMapping mapping : mappings) {
			DataMonitor monitor = monitorService.selectByPrimaryKey(mapping.getDeviceId());
			UserDeviceMappingDTO dto = new UserDeviceMappingDTO();
			dto.setId(mapping.getId());
			dto.setName(monitor.getName());
			dtos.add(dto);
		}
		return dtos;
	}

}
