package com.gdut.dongjun.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.HitchConst;
import com.gdut.dongjun.domain.po.ModuleHitchEvent;
import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;
import com.gdut.dongjun.domain.po.TemperatureSensor;
import com.gdut.dongjun.domain.vo.HitchEventVO;
import com.gdut.dongjun.dto.HitchEventDTO;
import com.gdut.dongjun.dto.TemperatureMeasureHitchEventDTO;
import com.gdut.dongjun.service.HitchEventService;
import com.gdut.dongjun.service.device.DataMonitorService;
import com.gdut.dongjun.service.device.TemperatureModuleService;
import com.gdut.dongjun.service.device.TemperatureSensorService;
import com.gdut.dongjun.service.device.event.ModuleHitchEventService;
import com.gdut.dongjun.service.device.event.TemperatureMeasureHitchEventService;
import com.gdut.dongjun.util.MyBatisMapUtil;

/**
 * 等着重构把你
 * @author Gordan_Deng
 * @date 2017年4月20日
 */
@Component
public class HitchEventServiceImpl implements HitchEventService {
	
	@Autowired
	private TemperatureMeasureHitchEventService temEventService;
	@Autowired
	private TemperatureModuleService temModuleService;
	@Autowired
	private ModuleHitchEventService moduleHitchService;
	@Autowired
	private DataMonitorService monitorService;
	@Autowired
	private TemperatureSensorService sensorService;
	
	Logger logger = Logger.getLogger(HitchEventServiceImpl.class);

	@Override
	public HitchEventDTO wrapIntoDTO(HitchEventVO vo) {
		HitchEventDTO d = null;
		int type = vo.getType();
		switch (type) {
		case 0: {
			break;
		}
		case 1: {
			break;
		}
		case 2: {
			break;
		}
		case 301: {
			//TODO
			String name = monitorService.selectByPrimaryKey(vo.getMonitorId()).getName();
			ModuleHitchEvent hitchEvent = moduleHitchService.selectByPrimaryKey(vo.getId());
			TemperatureMeasureHitchEvent temEvent =
						temEventService.selectByParameters(MyBatisMapUtil.warp("hitch_id", hitchEvent.getId())).get(0);
			TemperatureMeasureHitchEventDTO dto = 
					new TemperatureMeasureHitchEventDTO(hitchEvent, temEvent);
			dto.setName(name);
			dto.setType(returnType(vo.getType()));
			dto.setGroupId(vo.getGroupId());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("device_id", hitchEvent.getModuleId());
			map.put("tag", temEvent.getTag());
			List<TemperatureSensor> sensor = sensorService.selectByParameters(MyBatisMapUtil.warp(map));
			dto.setSensorType(sensor.get(0).getType());
			d = (HitchEventDTO)dto;
			break;
		}
		default: {
			logger.info("HitchEventVO数据异常");
			break;
		}
		}
		return d;
	}
	
	private String returnType(Integer i) {
		switch (i) {
		case 301 : return HitchConst.HITCH_OVER_TEMPERATURE;
		case 310 : return HitchConst.HITCH_ELECTRICITY_LACK;
		default : return "";
		}
	}

}
