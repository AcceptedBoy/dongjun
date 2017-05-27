package com.gdut.dongjun.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.HitchConst;
import com.gdut.dongjun.InfoConst;
import com.gdut.dongjun.domain.dto.HitchEventDTO;
import com.gdut.dongjun.domain.po.ModuleHitchEvent;
import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;
import com.gdut.dongjun.domain.po.TemperatureSensor;
import com.gdut.dongjun.service.GPRSModuleService;
import com.gdut.dongjun.service.RemoteEventService;
import com.gdut.dongjun.service.device.DataMonitorService;
import com.gdut.dongjun.service.device.ElectronicModuleService;
import com.gdut.dongjun.service.device.TemperatureModuleService;
import com.gdut.dongjun.service.device.TemperatureSensorService;
import com.gdut.dongjun.service.device.event.ModuleHitchEventService;
import com.gdut.dongjun.service.device.event.TemperatureMeasureHitchEventService;
import com.gdut.dongjun.service.webservice.client.po.InfoEventDTO;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.TimeUtil;
import com.gdut.dongjun.web.vo.HitchEventVO;
import com.gdut.dongjun.web.vo.InfoEventVO;
import com.gdut.dongjun.web.vo.TemperatureMeasureHitchEventVO;

/**
 * 等着重构把你
 * 
 * @author Gordan_Deng
 * @date 2017年4月20日
 */
@Component
public class RemoteEventServiceImpl implements RemoteEventService {

	@Autowired
	private TemperatureMeasureHitchEventService temEventService;
	@Autowired
	private TemperatureModuleService temModuleService;
	@Autowired
	private ElectronicModuleService elecModuleService;
	@Autowired
	private GPRSModuleService gprsModuleService;
	@Autowired
	private ModuleHitchEventService moduleHitchService;
	@Autowired
	private DataMonitorService monitorService;
	@Autowired
	private TemperatureSensorService sensorService;

	Logger logger = Logger.getLogger(RemoteEventServiceImpl.class);

	@Override
	public HitchEventVO wrapHitchVO(HitchEventDTO vo) {
		HitchEventVO d = null;
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
			// TODO
			String name = monitorService.selectByPrimaryKey(vo.getMonitorId()).getName();
			ModuleHitchEvent hitchEvent = moduleHitchService.selectByPrimaryKey(vo.getId());
			TemperatureMeasureHitchEvent temEvent = temEventService
					.selectByParameters(MyBatisMapUtil.warp("hitch_id", hitchEvent.getId())).get(0);
			TemperatureMeasureHitchEventVO dto = new TemperatureMeasureHitchEventVO(hitchEvent, temEvent);
			dto.setName(name);
			dto.setType(returnHitchType(vo.getType()));
			dto.setGroupId(vo.getGroupId());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("device_id", hitchEvent.getModuleId());
			map.put("tag", temEvent.getTag());
			List<TemperatureSensor> sensor = sensorService.selectByParameters(MyBatisMapUtil.warp(map));
			dto.setSensorType(sensor.get(0).getType());
			d = (HitchEventVO) dto;
			break;
		}
		default: {
			logger.info("HitchEventDTO数据异常");
			break;
		}
		}
		return d;
	}

	private String returnHitchType(Integer i) {
		int num = i / 100;
		switch (num) {
		case HitchConst.MODULE_ELECTRICITY:
			return "电能表监控";
		case HitchConst.MODULE_TEMPERATURE:
			return "温度监控";
		case HitchConst.MODULE_GPRS:
			return "GPRS模块";
		default:
			return "未知报警";
		}
	}

	@Override
	public InfoEventVO wrapIntoVO(InfoEventDTO dto) {
		InfoEventVO vo = new InfoEventVO();
		vo.setInfoTime(TimeUtil.timeFormat(new Date()));
		vo.setInfoReason(InfoConst.getInfo(dto.getType()));
		vo.setGroupId(dto.getGroupId());
		switch (dto.getType()) {
		case InfoConst.MODULE_ONLINE: {
			Integer moduleType = (Integer) dto.getText();
			switch (moduleType) {
			case HitchConst.MODULE_ELECTRICITY: {
				vo.setType("设备上线-电能表监控设备");
				vo.setName(elecModuleService.selectByPrimaryKey(dto.getModuleId()).getName());
				break;
			}
			case HitchConst.MODULE_GPRS: {
				vo.setType("设备上线-GPRS模块");
				vo.setName(gprsModuleService.selectByPrimaryKey(dto.getModuleId()).getName());
				break;
			}
			case HitchConst.MODULE_TEMPERATURE: {
				vo.setType("设备上线-温度监控设备");
				vo.setName(temModuleService.selectByPrimaryKey(dto.getModuleId()).getName());
				break;
			}
			default:
				break;
			}
		}
		case InfoConst.MODULE_OFFLINE: {
			Integer moduleType = (Integer) dto.getText();
			switch (moduleType) {
			case HitchConst.MODULE_ELECTRICITY: {
				vo.setType("电能表监控设备");
				vo.setName(elecModuleService.selectByPrimaryKey(dto.getModuleId()).getName());
				break;
			}
			case HitchConst.MODULE_GPRS: {
				vo.setType("GPRS模块");
				vo.setName(gprsModuleService.selectByPrimaryKey(dto.getModuleId()).getName());
				break;
			}
			case HitchConst.MODULE_TEMPERATURE: {
				vo.setType("温度监控设备");
				vo.setName(temModuleService.selectByPrimaryKey(dto.getModuleId()).getName());
				break;
			}
			default:
				break;
			}
		}
		case InfoConst.MODULE_NOT_DEFINED: {
			vo.setType("未知设备");
		}
		default:
			break;
		}
		return vo;
	}

}
