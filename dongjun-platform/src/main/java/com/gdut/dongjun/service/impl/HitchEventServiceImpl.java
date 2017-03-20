package com.gdut.dongjun.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;
import com.gdut.dongjun.domain.vo.HitchEventVO;
import com.gdut.dongjun.dto.HitchEventDTO;
import com.gdut.dongjun.dto.TemperatureMeasureHitchEventDTO;
import com.gdut.dongjun.service.HitchEventService;
import com.gdut.dongjun.service.device.TemperatureDeviceService;
import com.gdut.dongjun.service.device.event.TemperatureMeasureHitchEventService;

@Component
public class HitchEventServiceImpl implements HitchEventService {
	
	@Autowired
	private TemperatureMeasureHitchEventService temEventService;
	@Autowired
	private TemperatureDeviceService temService;
	
	Logger logger = Logger.getLogger(HitchEventServiceImpl.class);

	@Override
	public HitchEventDTO wrapIntoDTO(HitchEventVO vo) {
		HitchEventDTO d = null;
		switch (vo.getType()) {
		case 0: {
			break;
		}
		case 1: {
			break;
		}
		case 2: {
			break;
		}
		case 3: {
			String name = temService.selectNameById(vo.getSwitchId());
			TemperatureMeasureHitchEvent temEvent = temEventService.selectByPrimaryKey(vo.getId());
			TemperatureMeasureHitchEventDTO dto = new TemperatureMeasureHitchEventDTO();
			dto.setId(temEvent.getId());
			dto.setHitchReason(temEvent.getHitchReason());
			dto.setHitchTime(temEvent.getHitchTime());
			dto.setMaxHitchValue(temEvent.getMaxHitchValue().doubleValue() + "");
			dto.setMinHitchValue(temEvent.getMinHitchValue().doubleValue() + "");
			dto.setName(name);
			dto.setTag(temEvent.getTag());
			dto.setValue(temEvent.getValue().doubleValue() + "");
			dto.setType(returnType(vo.getType()));
			dto.setGroupId(vo.getGroupId());
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
		case 1 : return "低压设备";
		case 2 : return "管控设备";
		case 3 : return "温度设备";
		default : return "";
		}
	}

}
