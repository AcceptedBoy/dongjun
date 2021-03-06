package com.gdut.dongjun.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.TemperatureSensor;
import com.gdut.dongjun.dto.TemperatureSensorDTO;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.device.TemperatureSensorService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

@Controller
@RequestMapping("/dongjun/temperature_sensor")
public class TemperatureSensorController {

	@Autowired
	private TemperatureSensorService sensorService;
	@Autowired
	private UserService userService;
	
	@RequiresAuthentication
	@RequestMapping("/list")
	@ResponseBody
	public ResponseMessage getSensorByDeviceId(String deviceId) {
		List<TemperatureSensor> list = sensorService.selectByParameters(MyBatisMapUtil.warp("device_id", deviceId));
		List<TemperatureSensorDTO> dtoList = new ArrayList<TemperatureSensorDTO>();
		for (TemperatureSensor s : list) {
			dtoList.add(TemperatureSensorDTO.wrap(s));
		}
		return ResponseMessage.success(dtoList);
	}
	
//	@RequiresPermissions("platform_group_admin:device")
	@RequestMapping("/edit")
	@ResponseBody
	public ResponseMessage editSensor(TemperatureSensor sensor) {
		if (null == sensor.getId()) {
			sensor.setId(UUIDUtil.getUUID());
		}
		if (sensorService.updateByPrimaryKeySelective(sensor) == 0) {
			return ResponseMessage.warning("操作失败");
		}
		return ResponseMessage.success("操作成功");
	}
	
//	@RequiresPermissions("platform_group_admin:device")
	@RequestMapping("/del")
	@ResponseBody
	public ResponseMessage delSensor(String id) {
		if (!sensorService.deleteByPrimaryKey(id)) {
			return ResponseMessage.warning("操作失败");
		}
		return ResponseMessage.success("操作成功");
	}
	
	
}
