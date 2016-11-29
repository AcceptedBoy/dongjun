package com.gdut.dongjun.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.TemperatureSensor;
import com.gdut.dongjun.service.TemperatureSensorService;
import com.gdut.dongjun.util.MyBatisMapUtil;

@Controller
@RequestMapping("/dongjun/temperature_sensor")
public class TemperatureSensorController {

	@Autowired
	TemperatureSensorService sensorService;
	
	@RequestMapping("/get_sensor_by_device_id")
	@ResponseBody
	public ResponseMessage getSensor(String deviceId) {
		List<TemperatureSensor> list = sensorService.selectByParameters(MyBatisMapUtil.warp("device_id", deviceId));
		return ResponseMessage.success(list);
	}
	
	@RequestMapping("/edit")
	@ResponseBody
	public ResponseMessage editSensor(TemperatureSensor sensor) {
		if (sensorService.updateByPrimaryKey(sensor) == 0) {
			return ResponseMessage.danger("操作失败");
		}
		return ResponseMessage.success("操作成功");
	}
	
	
	
}
