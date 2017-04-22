package com.gdut.dongjun.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.HitchConst;
import com.gdut.dongjun.domain.po.ModuleHitchEvent;
import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;
import com.gdut.dongjun.domain.vo.HitchEventVO;
import com.gdut.dongjun.dto.HitchEventDTO;
import com.gdut.dongjun.dto.TemperatureMeasureHitchEventDTO;
import com.gdut.dongjun.service.HitchEventService;
import com.gdut.dongjun.service.device.DataMonitorService;
import com.gdut.dongjun.service.device.TemperatureModuleService;
import com.gdut.dongjun.service.device.event.ModuleHitchEventService;
import com.gdut.dongjun.service.device.event.TemperatureMeasureHitchEventService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.TimeUtil;

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
	
	Logger logger = Logger.getLogger(HitchEventServiceImpl.class);

	@Override
	public HitchEventDTO wrapIntoDTO(HitchEventVO vo) {
		HitchEventDTO d = null;
		int type = vo.getType() / 100;
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
		case 3: {
			//TODO
			String name = monitorService.selectByPrimaryKey(vo.getMonitorId()).getName();
			ModuleHitchEvent hitchEvent = moduleHitchService.selectByPrimaryKey(vo.getId());
//			TemperatureMeasureHitchEvent temEvent = temEventService.selectByPrimaryKey(vo.getId());
			TemperatureMeasureHitchEventDTO dto = new TemperatureMeasureHitchEventDTO();
			dto.setId(hitchEvent.getId());
			dto.setHitchReason(hitchEvent.getHitchReason());
			dto.setHitchTime(TimeUtil.timeFormat(hitchEvent.getHitchTime()));
			TemperatureMeasureHitchEvent temEvent = temEventService.selectByParameters(MyBatisMapUtil.warp("hitch_id", hitchEvent.getId())).get(0);
			
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
		case HitchConst.MODULE_ELECTRICITY : return "电能表设备";
		case HitchConst.MODULE_TEMPERATURE : return "温度设备";
		default : return "";
		}
	}

}
