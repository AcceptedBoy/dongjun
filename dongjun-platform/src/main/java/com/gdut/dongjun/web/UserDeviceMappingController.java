package com.gdut.dongjun.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.annotation.NeededTest;
import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.TemperatureDevice;
import com.gdut.dongjun.domain.po.UserDeviceMapping;
import com.gdut.dongjun.domain.vo.UserDeviceMappingDTO;
import com.gdut.dongjun.service.UserDeviceMappingService;
import com.gdut.dongjun.service.device.TemperatureDeviceService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

@Controller
@RequestMapping("/dongjun/user_device_mapping")
public class UserDeviceMappingController {

	@Autowired
	private UserDeviceMappingService mappingService;
	@Autowired
	private TemperatureDeviceService temDeviceService;
	
	@ResponseBody
	@RequestMapping("/edit")
	public ResponseMessage edit(UserDeviceMapping mapping) {
		if (null == mapping.getId()) {
			mapping.setId(UUIDUtil.getUUID());
		}
		if (0 == mappingService.updateByPrimaryKey(mapping)) {
			return ResponseMessage.warning("操作失败");
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
	
	@ResponseBody
	@RequestMapping("/get_user_mapping")
	public ResponseMessage getUserMapping(String id) {
		List<UserDeviceMapping> list = mappingService.selectByParameters(MyBatisMapUtil.warp("user_id", id));
//		HashMap<String, Object> map = (HashMap<String, Object>) MapUtil.warp("draw", 1);
//		map.put("recordsTotal", list.size());
//		map.put("data", list);
//		map.put("recordsFiltered", list.size());
		return ResponseMessage.success(wrapIntoDTO(list));
	}
	
	private List<UserDeviceMappingDTO> wrapIntoDTO(List<UserDeviceMapping> mappings) {
		List<UserDeviceMappingDTO> dtos = new ArrayList<UserDeviceMappingDTO>();
		for (UserDeviceMapping mapping : mappings) {
			switch(mapping.getDeviceType()) {
			case 3 : {
				TemperatureDevice device = temDeviceService.selectByPrimaryKey(mapping.getDeviceId());
				UserDeviceMappingDTO dto = new UserDeviceMappingDTO();
				dto.setId(mapping.getId());
				dto.setName(device.getName());
				dto.setType(mapping.getType());
				dto.setDeviceNumber(device.getDeviceNumber());
				dtos.add(dto);
				break;
			}
			default : break;
			}
		}
		return dtos;
	}
}
