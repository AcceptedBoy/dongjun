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
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.device.TemperatureSensorService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;
import com.gdut.dongjun.web.vo.TemperatureSensorVO;

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
//		List<TemperatureSensorVO> dtoList = new ArrayList<TemperatureSensorVO>();
//		for (TemperatureSensor s : list) {
//			dtoList.add(TemperatureSensorVO.wrap(s));
//		}
		return ResponseMessage.success(list);
	}
	
//	@RequiresPermissions("platform_group_admin:device")
	@RequestMapping("/edit")
	@ResponseBody
	public ResponseMessage editSensor(TemperatureSensor sensor) {
		//	新增
		if (null == sensor.getId()) {
			sensor.setId(UUIDUtil.getUUID());
			//	如果新增的传感器的tag已经存在，错误
			List<TemperatureSensor> list = sensorService.selectByParameters(MyBatisMapUtil.warp("device_id", sensor.getDeviceId()));
			for (TemperatureSensor s : list) {
				if (s.getTag() == sensor.getTag()) {
					return ResponseMessage.warning("操作失败"); 
				}
			}
			sensorService.insert(sensor);
			return ResponseMessage.success("操作成功");
		} else {
			List<TemperatureSensor> list = sensorService.selectByParameters(MyBatisMapUtil.warp("device_id", sensor.getDeviceId()));
			for (TemperatureSensor s : list) {
				if (s.getTag() == sensor.getTag()) {
					if (s.getId().equals(sensor.getId())) {
						//	相同的tag， 相同的id，判断为相同的两个实体
						sensorService.updateByPrimaryKeySelective(sensor);
						return ResponseMessage.success("操作成功"); 
					} else {
						//	相同的tag，不同的id，则两个实体产生冲突，报错
						return ResponseMessage.warning("操作失败"); 
					}
				}
			}
			//	数据库中没有一个记录拥有传入的tag，则认为是修改安全的记录
			sensorService.updateByPrimaryKeySelective(sensor);
			return ResponseMessage.success("操作成功"); 
		}
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
