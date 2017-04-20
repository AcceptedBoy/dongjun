package com.gdut.dongjun.service.device.event.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.TemperatureMeasureHitchEventMapper;
import com.gdut.dongjun.domain.po.ModuleHitchEvent;
import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;
import com.gdut.dongjun.dto.TemperatureMeasureHitchEventDTO;
import com.gdut.dongjun.service.base.impl.EnhancedServiceImpl;
import com.gdut.dongjun.service.device.DataMonitorService;
import com.gdut.dongjun.service.device.event.ModuleHitchEventService;
import com.gdut.dongjun.service.device.event.TemperatureMeasureHitchEventService;
import com.gdut.dongjun.util.MyBatisMapUtil;

@Service
public class TemperatureMeasureHitchEventServiceImpl extends EnhancedServiceImpl<TemperatureMeasureHitchEvent>
		implements TemperatureMeasureHitchEventService{

	@Autowired
	private TemperatureMeasureHitchEventMapper mapper;
	@Autowired
	private ModuleHitchEventService moduleHitchService;
	@Autowired
	private DataMonitorService monitorService;
	
	@Override
	protected boolean isExist(TemperatureMeasureHitchEvent record) {
		if (record != null && null != record.getId()
				&& mapper.selectByPrimaryKey(record.getId()) != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<TemperatureMeasureHitchEventDTO> selectMeasureHitch(String companyId) {
		//搜索type为301的报警事件
		List<ModuleHitchEvent> list = moduleHitchService.selectByType(301, 302, companyId);
		List<TemperatureMeasureHitchEventDTO> dtoList = new ArrayList<TemperatureMeasureHitchEventDTO>();
		for (ModuleHitchEvent e : list) {
			TemperatureMeasureHitchEvent event = mapper.selectByParameters(MyBatisMapUtil.warp("hitch_id", e.getId())).get(0);
			TemperatureMeasureHitchEventDTO dto = new TemperatureMeasureHitchEventDTO(e, event);
			dto.setName(monitorService.selectByPrimaryKey(e.getModuleId()).getName());
			dtoList.add(dto);
		}
		return dtoList;
	}

}
